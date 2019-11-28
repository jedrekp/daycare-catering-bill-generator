package jedrekp.daycarecateringbillgenerator.Repository;


import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CateringBillRepository extends JpaRepository<CateringBill, Long> {

    @Query("SELECT cb FROM CateringBill INNER JOIN FETCH cb.child INNER JOIN FETCH cb.dailyCateringOrders " +
            "WHERE cb.id = :cateringBillId")
    Optional<CateringBill> findByIdWithAllDetails(@Param("cateringBillId") Long cateringBillId);
}
