package jedrekp.daycarecateringbillgenerator.Controller;

import jedrekp.daycarecateringbillgenerator.DTO.MonthlyCateringBillDTO;
import jedrekp.daycarecateringbillgenerator.Service.CateringBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;

@RestController
@CrossOrigin
public class CateringBillController {

    @Autowired
    CateringBillService cateringBillService;

    @PostMapping(value = "/cateringBill/child/{childId}", params = {"month", "year"})
    public ResponseEntity<MonthlyCateringBillDTO> generateMonthlyCateringBillForChild(
            @PathVariable Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBill(childId, month, year), HttpStatus.OK);
    }

}
