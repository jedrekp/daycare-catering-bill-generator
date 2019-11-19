package jedrekp.daycarecateringbillgenerator.Controller;

import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
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

    @GetMapping(value = "/cateringBills/children/{childId}", params = {"month", "year"})
    public ResponseEntity<CateringBill> generateMonthlyCateringBillForChild(
            @PathVariable Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBill(childId, month, year), HttpStatus.OK);
    }

    @PostMapping(value = "/cateringBills/children/{childId}", params = {"month", "year"})
    public ResponseEntity sendEmail(@PathVariable Long childId, @RequestParam Month month, @RequestParam Integer year) {
        cateringBillService.sendCateringBillViaEmailAndSaveIt(childId, month, year);
        return ResponseEntity.noContent().build();
    }
}
