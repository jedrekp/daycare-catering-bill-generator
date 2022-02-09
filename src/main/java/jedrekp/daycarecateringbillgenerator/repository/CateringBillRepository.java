package jedrekp.daycarecateringbillgenerator.repository;


import jedrekp.daycarecateringbillgenerator.entity.CateringBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface CateringBillRepository extends JpaRepository<CateringBill, Long> {


    boolean existsByMonthAndYearAndChildId(Month month, Year year, Long childId);

    Optional<CateringBill> findByMonthAndYearAndChildId(Month month, Year year, Long childId);

    @Query("SELECT cb FROM CateringBill cb INNER JOIN FETCH cb.child LEFT JOIN FETCH cb.dailyCateringOrders " +
            "WHERE cb.id = :cateringBillId")
    Optional<CateringBill> findByIdWithAllDetails(Long cateringBillId);

    @Query("SELECT cb FROM CateringBill cb INNER JOIN FETCH cb.child c LEFT JOIN FETCH cb.dailyCateringOrders "
            + "WHERE cb.month = :month AND cb.year = :year and c.id = :childId")
    Optional<CateringBill> findByMonthAndYearAndChildIdWithAllDetails(Month month, Year year, Long childId);

    @Query("SELECT cb FROM CateringBill cb LEFT JOIN FETCH cb.dailyCateringOrders " +
            "INNER JOIN FETCH cb.child c INNER JOIN c.daycareGroup dg " +
            "WHERE cb.month = :month AND cb.year = :year AND dg.id = :daycareGroupId")
    List<CateringBill> findAllByMonthAndYearAndDaycareGroupId(Month month, Year year, Long daycareGroupId);

}
