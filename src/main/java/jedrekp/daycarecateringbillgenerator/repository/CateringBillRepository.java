package jedrekp.daycarecateringbillgenerator.repository;


import jedrekp.daycarecateringbillgenerator.entity.CateringBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Month;
import java.time.Year;
import java.util.Optional;

@Repository
public interface CateringBillRepository extends JpaRepository<CateringBill, Long> {


    boolean existsByMonthAndYearAndChild_Id(Month month, Year year, Long childId);

    @Query("SELECT cb FROM CateringBill cb INNER JOIN FETCH cb.child INNER JOIN FETCH cb.dailyCateringOrders " +
            "WHERE cb.id = :cateringBillId")
    Optional<CateringBill> findByIdWithAllDetails(@Param("cateringBillId") Long cateringBillId);
}
