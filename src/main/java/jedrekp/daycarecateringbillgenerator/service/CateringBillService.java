package jedrekp.daycarecateringbillgenerator.service;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.DTO.CateringBillDTO;
import jedrekp.daycarecateringbillgenerator.entity.*;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import jedrekp.daycarecateringbillgenerator.repository.CateringBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Month;
import java.time.Year;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CateringBillService {

    private final AttendanceSheetRepository attendanceSheetRepository;

    private final CateringBillRepository cateringBillRepository;

    private final ChildService childService;

    private final CateringOptionService cateringOptionService;

    private final EmailService emailService;

    @Transactional(readOnly = true)
    public CateringBill findByIdWithAllDetails(long cateringBillId) {
        return cateringBillRepository.findByIdWithAllDetails(cateringBillId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Set<CateringBillDTO> findCateringBillsByMonthAndDaycareGroupId(long daycareGroupId, Month month, Year year) {
        Collection<CateringBill> cateringBills = cateringBillRepository.findAllByMonthAndYearAndDaycareGroupId(month, year, daycareGroupId);
        Set<CateringBillDTO> cateringBillDTOs = new HashSet<>();
        cateringBills.forEach(cateringBill -> cateringBillDTOs.add(mapCateringBillToDto(cateringBill)));
        return cateringBillDTOs;
    }

    @Transactional(readOnly = true)
    public CateringBillDTO generateCateringBillPreview(long childId, Month month, Year year) {

        Child child = childService.findSingleNotArchivedChildById(childId);

        CateringBillDTO cateringBillDTO = new CateringBillDTO(childId, month, year);
        cateringBillDTO.setChildFullName(childService.getFullNameOfChild(child));
        cateringBillDTO.setCorrection(cateringBillRepository.existsByMonthAndYearAndChild_Id(month, year, childId));

        List<AttendanceSheet> attendanceSheets = attendanceSheetRepository
                .findByPresentChildIdForSpecificMonth(childId, month.getValue(), year.getValue());
        insertDailyOrdersIntoNewCateringBill(attendanceSheets, cateringBillDTO, childId);

        cateringBillDTO.setTotalDue(calculateTotalDueBasedOnDailyOrders(cateringBillDTO.getDailyCateringOrders()));

        return cateringBillDTO;
    }

    @Transactional
    public CateringBill saveOrEditCateringBill(long childId, CateringBillDTO cateringBillDTO) {

        checkIfCateringBillContainsAnyOrders(cateringBillDTO);

        CateringBill cateringBill;

        Optional<CateringBill> optionalCateringBill = cateringBillRepository
                .findByMonthAndYearAndChild_id(cateringBillDTO.getMonth(), cateringBillDTO.getYear(), childId);

        if (optionalCateringBill.isPresent()) {
            cateringBill = optionalCateringBill.get();
            cateringBill.setCorrection(true);
        } else {
            cateringBill = mapDtoToCateringBill(childId, cateringBillDTO);
        }

        cateringBillDTO.getDailyCateringOrders().forEach(dailyCateringOrder -> dailyCateringOrder.setCateringBill(cateringBill));
        cateringBill.setDailyCateringOrders(cateringBillDTO.getDailyCateringOrders());

        return cateringBill;
    }

    @Transactional(readOnly = true)

    public void sendBillToParentViaEmail(long cateringBillId) {

        CateringBill cateringBill = findByIdWithAllDetails(cateringBillId);

        BigDecimal totalDue = calculateTotalDueBasedOnDailyOrders(cateringBill.getDailyCateringOrders());

        try {
            emailService.sendEmailWithCateringBill(cateringBill, totalDue);
        } catch (IOException | TemplateException | MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void checkIfCateringBillContainsAnyOrders(CateringBillDTO cateringBillDTO) {
        if (cateringBillDTO.getDailyCateringOrders().isEmpty()) {
            throw new IllegalArgumentException("Cannot save catering bill with no daily orders");
        }
    }

    private void insertDailyOrdersIntoNewCateringBill(List<AttendanceSheet> attendanceSheets, CateringBillDTO cateringBillDTO, long childId) {
        for (AttendanceSheet attendanceSheet : attendanceSheets) {
            CateringOption optionInEffect = cateringOptionService.findOptionInEffectForChild(childId, attendanceSheet.getDate());
            cateringBillDTO.getDailyCateringOrders()
                    .add(new DailyCateringOrder(attendanceSheet.getDate(), optionInEffect.getOptionName(), optionInEffect.getDailyCost()));
        }
    }

    private CateringBill mapDtoToCateringBill(long childId, CateringBillDTO cateringBillDTO) {
        Child child = childService.findSingleNotArchivedChildById(childId);
        CateringBill cateringBill = new CateringBill(cateringBillDTO.getMonth(), cateringBillDTO.getYear());
        cateringBill.setChild(child);
        child.getCateringBills().add(cateringBill);
        return cateringBill;
    }

    private CateringBillDTO mapCateringBillToDto(CateringBill cateringBill) {
        CateringBillDTO cateringBillDTO = new CateringBillDTO(cateringBill.getChild().getId(), cateringBill.getMonth(),
                cateringBill.getYear());
        cateringBillDTO.setChildFullName(childService.getFullNameOfChild(cateringBill.getChild()));
        cateringBillDTO.setDailyCateringOrders(cateringBill.getDailyCateringOrders());
        cateringBillDTO.setTotalDue(calculateTotalDueBasedOnDailyOrders(cateringBillDTO.getDailyCateringOrders()));
        return cateringBillDTO;
    }

    private BigDecimal calculateTotalDueBasedOnDailyOrders(Collection<DailyCateringOrder> cateringOrders) {
        return cateringOrders
                .stream()
                .map(DailyCateringOrder::getCateringOptionPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
