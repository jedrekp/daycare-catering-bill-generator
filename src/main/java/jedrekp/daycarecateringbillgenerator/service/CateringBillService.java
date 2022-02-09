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
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CateringBillService {

    private final CateringBillRepository cateringBillRepository;

    private final AttendanceSheetRepository attendanceSheetRepository;

    private final ChildService childService;

    private final CateringOptionService cateringOptionService;

    private final EmailService emailService;

    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    public CateringBillResponse getCateringBillByChildIdAndMonth(long childId, Month month, Year year) {
        CateringBill cateringBill = cateringBillRepository.findByMonthAndYearAndChildIdWithAllDetails(month, year, childId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "{0} {1} catering bill has not been generated yet for child#{2}",
                        month.getDisplayName(TextStyle.FULL,Locale.ENGLISH), year, childId)));
        return mapExistingCateringBillToResponse(cateringBill);
    }

    @Transactional(readOnly = true)
    public Set<CateringBillResponse> getAllCateringBillsByDaycareGroupIdAndMonth(long daycareGroupId, Month month, Year year) {
        Collection<CateringBill> cateringBills = cateringBillRepository.findAllByMonthAndYearAndDaycareGroupId(month, year, daycareGroupId);
        Set<CateringBillResponse> cateringBillResponses = new HashSet<>();
        cateringBills.forEach(cateringBill -> cateringBillResponses.add(mapExistingCateringBillToResponse(cateringBill)));
        return cateringBillResponses;
    }

    @Transactional(readOnly = true)
    public CateringBillResponse generateCateringBillPreview(long childId, Month month, Year year) {

        Child child = childService.findSingleNotArchivedChildById(childId);
        List<AttendanceSheet> attendanceSheets = attendanceSheetRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue());

        CateringBillResponse cateringBillResponse = new CateringBillResponse(
                0L,
                childId,
                month.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                year);

        cateringBillResponse.setChildFullName(childService.getFullNameOfChild(child));
        cateringBillResponse.setCorrection(cateringBillRepository.existsByMonthAndYearAndChildId(month, year, childId));
        insertDailyOrdersIntoCateringBillPreview(attendanceSheets, cateringBillResponse, childId);
        cateringBillResponse.setTotalDue(calculateTotalDueFromDailyOrders(cateringBillResponse.getDailyCateringOrders()));

        return cateringBillResponse;
    }

    @Transactional
    public CateringBill addNewCateringBillToChild(long childId, CreateCateringBillRequest cateringBillRequest) {

        Child child = childService.findSingleNotArchivedChildById(childId);
        CateringBill newCateringBill = mapRequestToCateringBill(cateringBillRequest);

        cateringBillRepository
                .findByMonthAndYearAndChildId(cateringBillRequest.getMonth(), cateringBillRequest.getYear(), childId)
                .ifPresent(previousCateringBill -> deletePreviousVersionOfCateringBillAndMarkNewVersionAsCorrection(
                        previousCateringBill, newCateringBill, child));

        newCateringBill.setChild(child);
        child.getCateringBills().add(newCateringBill);

        return newCateringBill;
    }

    @Transactional(readOnly = true)
    public void sendBillToParentViaEmail(long cateringBillId) throws IOException, TemplateException, MessagingException {

        CateringBill cateringBill = findByIdWithAllDetails(cateringBillId);

        BigDecimal totalDue = calculateTotalDueFromDailyOrders(cateringBill.getDailyCateringOrders());

        emailService.sendEmailWithCateringBill(cateringBill, totalDue);

    }

    public CateringBillResponse mapExistingCateringBillToResponse(CateringBill cateringBill) {
        CateringBillResponse cateringBillResponse = new CateringBillResponse(
                cateringBill.getId(),
                cateringBill.getChild().getId(),
                cateringBill.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                cateringBill.getYear());

        cateringBillResponse.setChildFullName(childService.getFullNameOfChild(cateringBill.getChild()));
        cateringBillResponse.setDailyCateringOrders(new ArrayList<>(cateringBill.getDailyCateringOrders()));
        cateringBillResponse.setTotalDue(calculateTotalDueFromDailyOrders(cateringBillResponse.getDailyCateringOrders()));

        cateringBillResponse.getDailyCateringOrders().sort(Comparator.comparing(DailyCateringOrder::getOrderDate));

        return cateringBillResponse;
    }

    private CateringBill findByIdWithAllDetails(long cateringBillId) {
        return cateringBillRepository.findByIdWithAllDetails(cateringBillId).orElseThrow(() -> new EntityNotFoundException(
                MessageFormat.format("Catering bill #{0} does not exist.", cateringBillId)));
    }

    private void insertDailyOrdersIntoCateringBillPreview(List<AttendanceSheet> attendanceSheets, CateringBillResponse cateringBillResponse, long childId) {
        for (AttendanceSheet attendanceSheet : attendanceSheets) {
            CateringOption optionInEffect;
            try {
                optionInEffect = cateringOptionService.findOptionInEffectForChild(childId, attendanceSheet.getDate());
            } catch (EntityNotFoundException ex) {
                throw new IllegalStateException(MessageFormat.format("Cannot generate catering order for {0}. Child has no catering option in effect for that date.",
                        attendanceSheet.getDate()));
            }
            cateringBillResponse.getDailyCateringOrders()
                    .add(new DailyCateringOrder(attendanceSheet.getDate(), optionInEffect.getOptionName(), optionInEffect.getDailyCost()));
            cateringBillResponse.getDailyCateringOrders().sort(Comparator.comparing(DailyCateringOrder::getOrderDate));
        }
    }

    private CateringBill mapRequestToCateringBill(CreateCateringBillRequest cateringBillRequest) {
        CateringBill cateringBill = new CateringBill(
                cateringBillRequest.getMonth(),
                cateringBillRequest.getYear());

        cateringBillRequest.getDailyCateringOrders().forEach(dailyCateringOrder -> dailyCateringOrder.setCateringBill(cateringBill));
        cateringBill.setDailyCateringOrders(cateringBillRequest.getDailyCateringOrders());
        return cateringBill;
    }

    private void deletePreviousVersionOfCateringBillAndMarkNewVersionAsCorrection(
            CateringBill previousCateringBill, CateringBill newCateringBill, Child child) {

        child.getCateringBills().remove(previousCateringBill);
        previousCateringBill.setChild(null);

        /*
        flush session to make sure old bill is deleted via orphan removal before an attempt to save the new bill,
        as only one bill can exist for given child and month
        */
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
