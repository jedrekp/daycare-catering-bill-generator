package jedrekp.daycarecateringbillgenerator.service;

import jedrekp.daycarecateringbillgenerator.DTO.response.CateringBillResponse;
import jedrekp.daycarecateringbillgenerator.entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.entity.Child;
import jedrekp.daycarecateringbillgenerator.entity.DailyCateringOrder;
import jedrekp.daycarecateringbillgenerator.repository.AttendanceSheetRepository;
import jedrekp.daycarecateringbillgenerator.repository.CateringBillRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@SuppressWarnings("SpellCheckingInspection")
class CateringBillServiceTest {

    @Mock
    private CateringBillRepository cateringBillRepository;
    @Mock
    private AttendanceSheetRepository attendanceSheetRepository;
    @Mock
    private ChildService childService;
    @Mock
    private CateringOptionService cateringOptionService;
    @Mock
    private EmailService emailService;
    @InjectMocks
    private CateringBillService cateringBillService;


    //getCateringBillByChildIdAndMonth

    @Test
    void shouldReturnCateringBillResponseObject_WhenCateringBillFoundByChildIdAndMonth() {
        //given
        Child testChild = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild, "id", 1L);

        LocalDate testDate1 = LocalDate.of(2020, 3, 25);
        LocalDate testDate2 = LocalDate.of(2020, 3, 26);

        DailyCateringOrder testOrder1 = new DailyCateringOrder(testDate1, "Vegan", new BigDecimal("15.25"));
        DailyCateringOrder testOrder2 = new DailyCateringOrder(testDate2, "Milk-Free", new BigDecimal("12.75"));


        CateringBill testCateringBill = new CateringBill(Month.MARCH, Year.of(2020));
        testCateringBill.setCorrection(false);
        testCateringBill.setChild(testChild);
        testCateringBill.getDailyCateringOrders().addAll(Set.of(testOrder1, testOrder2));
        ReflectionTestUtils.setField(testCateringBill, "id", 11L);

        when(cateringBillRepository.findByMonthAndYearAndChildIdWithAllDetails(Month.MARCH, Year.of(2020), 1L))
                .thenReturn(Optional.of(testCateringBill));
        when(childService.getFullNameOfChild(testChild)).thenReturn("Son Gohan");

        //when
        CateringBillResponse cateringBillResponse = cateringBillService.getCateringBillByChildIdAndMonth(1L, Month.MARCH, Year.of(2020));

        //then
        assertAll(
                "Catering bill response has been created with correct values",
                () -> assertEquals(11L, cateringBillResponse.getBillId()),
                () -> assertEquals(1L, cateringBillResponse.getChildId()),
                () -> assertEquals("Son Gohan", cateringBillResponse.getChildFullName()),
                () -> assertEquals("March", cateringBillResponse.getMonth()),
                () -> assertEquals(Year.of(2020), cateringBillResponse.getYear()),
                () -> assertFalse(cateringBillResponse.isCorrection()),
                () -> assertEquals(new BigDecimal("28.00"), cateringBillResponse.getTotalDue()),
                () -> assertEquals(List.of(testOrder1, testOrder2), cateringBillResponse.getDailyCateringOrders())
        );

        verify(cateringBillRepository, times(1))
                .findByMonthAndYearAndChildIdWithAllDetails(Month.MARCH, Year.of(2020), 1L);
        verify(childService, times(1)).getFullNameOfChild(testChild);
        verifyNoMoreInteractions(cateringBillRepository, attendanceSheetRepository, childService, cateringOptionService, emailService);
    }

    @Test
    void shouldThrowException_WhenCateringBillNotFoundByChildIdAndMonth() {
        //given
        when(cateringBillRepository.findByMonthAndYearAndChildId(Month.MARCH, Year.of(2020), 1L)).thenReturn(Optional.empty());

        //expect
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class, () -> cateringBillService.getCateringBillByChildIdAndMonth(1L, Month.MARCH, Year.of(2020)));
        assertEquals("March 2020 catering bill has not been generated yet for child#1", ex.getMessage());

        verify(cateringBillRepository, times(1))
                .findByMonthAndYearAndChildIdWithAllDetails(Month.MARCH, Year.of(2020), 1L);
        verifyNoMoreInteractions(cateringBillRepository, attendanceSheetRepository, childService, cateringOptionService, emailService);
    }


    //getAllCateringBillsByDaycareGroupIdAndMonth

    @Test
    void shouldReturnSetOfCateringBillResponseObjects_WhenCateringBillsFoundByMonthAndDaycareGroupId() {
        //given
        Child testChild1 = new Child("Son", "Gohan", "songoku@fake.fake", false);
        ReflectionTestUtils.setField(testChild1, "id", 1L);
        Child testChild2 = new Child("Luke", "Skywalker", "darthvader@fake.fake", false);
        ReflectionTestUtils.setField(testChild2, "id", 2L);

        LocalDate testDate1 = LocalDate.of(2020, 3, 25);
        LocalDate testDate2 = LocalDate.of(2020, 3, 26);

        DailyCateringOrder testOrder1 = new DailyCateringOrder(testDate1, "Vegan", new BigDecimal("15.25"));
        DailyCateringOrder testOrder2 = new DailyCateringOrder(testDate2, "Milk-Free", new BigDecimal("12.75"));
        DailyCateringOrder testOrder3 = new DailyCateringOrder(testDate1, "Standard", new BigDecimal("12.00"));
        DailyCateringOrder testOrder4 = new DailyCateringOrder(testDate2, "Standard", new BigDecimal("12.00"));

        CateringBill testCateringBill1 = new CateringBill(Month.MARCH, Year.of(2020));
        testCateringBill1.setCorrection(false);
        testCateringBill1.setChild(testChild1);
        testCateringBill1.getDailyCateringOrders().addAll(Set.of(testOrder1, testOrder2));
        ReflectionTestUtils.setField(testCateringBill1, "id", 11L);

        CateringBill testCateringBill2 = new CateringBill(Month.MARCH, Year.of(2020));
        testCateringBill2.setCorrection(false);
        testCateringBill2.setChild(testChild2);
        testCateringBill2.getDailyCateringOrders().addAll(Set.of(testOrder3, testOrder4));
        ReflectionTestUtils.setField(testCateringBill2, "id", 12L);

        when(cateringBillRepository.findAllByMonthAndYearAndDaycareGroupId(Month.MARCH, Year.of(2020), 1L))
                .thenReturn(List.of(testCateringBill1, testCateringBill2));
        when(childService.getFullNameOfChild(testChild1)).thenReturn("Son Gohan");
        when(childService.getFullNameOfChild(testChild2)).thenReturn("Luke Skywalker");

        //when
        Set<CateringBillResponse> cateringBillResponses = cateringBillService
                .getAllCateringBillsByDaycareGroupIdAndMonth(1L, Month.MARCH, Year.of(2020));


        //then
        assertThat(cateringBillResponses, containsInAnyOrder(
                allOf(
                        hasProperty("billId", equalTo(11L)),
                        hasProperty("childId", equalTo(1L)),
                        hasProperty("childFullName", equalTo("Son Gohan")),
                        hasProperty("month", equalTo("March")),
                        hasProperty("year", equalTo(Year.of(2020))),
                        hasProperty("correction", equalTo(false)),
                        hasProperty("totalDue", equalTo(new BigDecimal("28.00"))),
                        hasProperty("dailyCateringOrders", equalTo(List.of(testOrder1, testOrder2)))
                ),
                allOf(
                        hasProperty("billId", equalTo(12L)),
                        hasProperty("childId", equalTo(2L)),
                        hasProperty("childFullName", equalTo("Luke Skywalker")),
                        hasProperty("month", equalTo("March")),
                        hasProperty("year", equalTo(Year.of(2020))),
                        hasProperty("correction", equalTo(false)),
                        hasProperty("totalDue", equalTo(new BigDecimal("24.00"))),
                        hasProperty("dailyCateringOrders", equalTo(List.of(testOrder3, testOrder4)))
                )
        ));

        verify(cateringBillRepository, times(1)).findAllByMonthAndYearAndDaycareGroupId(Month.MARCH, Year.of(2020), 1L);
        verify(childService, times(2)).getFullNameOfChild(ArgumentMatchers.any(Child.class));
        verifyNoMoreInteractions(cateringBillRepository, attendanceSheetRepository, childService, cateringOptionService, emailService);
    }

    @Test
    void shouldReturnEmptySet_WhenNoCateringBillsFoundByMonthAndDaycareGroupId() {

        //given
        when(cateringBillRepository.findAllByMonthAndYearAndDaycareGroupId(Month.MARCH, Year.of(2020), 1L))
                .thenReturn(Collections.emptyList());

        //when
        Set<CateringBillResponse> cateringBillResponses = cateringBillService
                .getAllCateringBillsByDaycareGroupIdAndMonth(1L, Month.MARCH, Year.of(2020));

        //then
        assertTrue(cateringBillResponses.isEmpty());

        verify(cateringBillRepository, times(1)).findAllByMonthAndYearAndDaycareGroupId(Month.MARCH, Year.of(2020), 1L);
        verifyNoMoreInteractions(cateringBillRepository, attendanceSheetRepository, childService, cateringOptionService, emailService);
    }

}