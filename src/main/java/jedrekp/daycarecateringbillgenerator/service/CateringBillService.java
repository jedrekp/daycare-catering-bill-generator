package jedrekp.daycarecateringbillgenerator.service;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.DTO.request.CreateCateringBillRequest;
import jedrekp.daycarecateringbillgenerator.DTO.response.CateringBillResponse;
import jedrekp.daycarecateringbillgenerator.entity.*;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import jedrekp.daycarecateringbillgenerator.repository.CateringBillRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CateringBillService {

    private final AttendanceSheetRepository attendanceSheetRepository;

    private final CateringBillRepository cateringBillRepository;

    private final ChildService childService;

    private final CateringOptionService cateringOptionService;

    private final EmailService emailService;

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public Set<CateringBillResponse> getCateringBillsByMonthAndDaycareGroupId(long daycareGroupId, Month month, Year year) {
        Collection<CateringBill> cateringBills = cateringBillRepository.findAllByMonthAndYearAndDaycareGroupId(month, year, daycareGroupId);
        Set<CateringBillResponse> cateringBillResponses = new HashSet<>();
        cateringBills.forEach(cateringBill -> cateringBillResponses.add(mapCateringBillToResponse(cateringBill)));
        return cateringBillResponses;
    }

    @Transactional(readOnly = true)
    public CateringBillResponse generateCateringBillPreview(long childId, Month month, Year year) {

        Child child = childService.findSingleNotArchivedChildById(childId);

        CateringBillResponse cateringBillResponse = new CateringBillResponse(childId, month.getDisplayName(TextStyle.FULL, Locale.ENGLISH), year);
        cateringBillResponse.setChildFullName(childService.getFullNameOfChild(child));
        cateringBillResponse.setCorrection(cateringBillRepository.existsByMonthAndYearAndChild_Id(month, year, childId));

        List<AttendanceSheet> attendanceSheets = attendanceSheetRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue());
        insertDailyOrdersIntoNewCateringBill(attendanceSheets, cateringBillResponse, childId);

        cateringBillResponse.setTotalDue(calculateTotalDueFromDailyOrders(cateringBillResponse.getDailyCateringOrders()));

        return cateringBillResponse;
    }

    @Transactional
    public CateringBill addNewCateringBillToChild(long childId, CreateCateringBillRequest cateringBillRequest) {

        checkIfCateringBillRequestContainsAnyOrders(cateringBillRequest);

        Child child = childService.findSingleNotArchivedChildById(childId);
        CateringBill newCateringBill = mapRequestToCateringBill(cateringBillRequest);

        cateringBillRepository.findByMonthAndYearAndChild_id(cateringBillRequest.getMonth(), cateringBillRequest.getYear(), childId)
                .ifPresent(previousCateringBill -> deletePreviousVersionOfCateringBillAndMarkNewVersionAsCorrection(
                        previousCateringBill, newCateringBill, child));

        newCateringBill.setChild(child);
        child.getCateringBills().add(newCateringBill);

        return newCateringBill;
    }

    @Transactional(readOnly = true)

    public void sendBillToParentViaEmail(long cateringBillId) {

        CateringBill cateringBill = findByIdWithAllDetails(cateringBillId);

        BigDecimal totalDue = calculateTotalDueFromDailyOrders(cateringBill.getDailyCateringOrders());

        try {
            emailService.sendEmailWithCateringBill(cateringBill, totalDue);
        } catch (IOException | TemplateException | MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private CateringBill findByIdWithAllDetails(long cateringBillId) {
        return cateringBillRepository.findByIdWithAllDetails(cateringBillId).orElseThrow(EntityNotFoundException::new);
    }

    private void checkIfCateringBillRequestContainsAnyOrders(CreateCateringBillRequest cateringBillRequest) {
        if (cateringBillRequest.getDailyCateringOrders().isEmpty()) {
            throw new IllegalArgumentException("Cannot save catering bill with no daily orders");
        }
    }

    private void insertDailyOrdersIntoNewCateringBill(List<AttendanceSheet> attendanceSheets, CateringBillResponse cateringBillResponse, long childId) {
        for (AttendanceSheet attendanceSheet : attendanceSheets) {
            CateringOption optionInEffect = cateringOptionService.findOptionInEffectForChild(childId, attendanceSheet.getDate());
            cateringBillResponse.getDailyCateringOrders()
                    .add(new DailyCateringOrder(attendanceSheet.getDate(), optionInEffect.getOptionName(), optionInEffect.getDailyCost()));
            cateringBillResponse.getDailyCateringOrders().sort(Comparator.comparing(DailyCateringOrder::getOrderDate));
        }
    }

    private CateringBill mapRequestToCateringBill(CreateCateringBillRequest cateringBillRequest) {
        CateringBill cateringBill = new CateringBill(cateringBillRequest.getMonth(), cateringBillRequest.getYear());
        cateringBillRequest.getDailyCateringOrders().forEach(dailyCateringOrder -> dailyCateringOrder.setCateringBill(cateringBill));
        cateringBill.setDailyCateringOrders(cateringBillRequest.getDailyCateringOrders());
        return cateringBill;
    }

    private CateringBillResponse mapCateringBillToResponse(CateringBill cateringBill) {
        CateringBillResponse cateringBillResponse = new CateringBillResponse(cateringBill.getChild().getId(),
                cateringBill.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), cateringBill.getYear());
        cateringBillResponse.setCorrection(cateringBill.isCorrection());
        cateringBillResponse.setChildFullName(childService.getFullNameOfChild(cateringBill.getChild()));
        cateringBillResponse.setDailyCateringOrders(new ArrayList<>(cateringBill.getDailyCateringOrders()));
        cateringBillResponse.setTotalDue(calculateTotalDueFromDailyOrders(cateringBillResponse.getDailyCateringOrders()));
        cateringBillResponse.getDailyCateringOrders().sort(Comparator.comparing(DailyCateringOrder::getOrderDate));
        return cateringBillResponse;
    }

    private void deletePreviousVersionOfCateringBillAndMarkNewVersionAsCorrection(
            CateringBill previousCateringBill, CateringBill newCateringBill, Child child) {
        child.getCateringBills().remove(previousCateringBill);
        previousCateringBill.setChild(null);
        entityManager.unwrap(Session.class).flush();
        newCateringBill.setCorrection(true);
    }

    private BigDecimal calculateTotalDueFromDailyOrders(Collection<DailyCateringOrder> cateringOrders) {
        return cateringOrders
                .stream()
                .map(DailyCateringOrder::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
