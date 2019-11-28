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
@RequestMapping("/cateringBills")
public class CateringBillOperationsController {

    @Autowired
    CateringBillService cateringBillService;

    @GetMapping(value = "/show-preview", params = {"childId", "month", "year"})
    public ResponseEntity<CateringBill> showCateringBillPreview(
            @RequestParam Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBill(childId, month, year), HttpStatus.OK);
    }

    @GetMapping(value = "/send-to-parent", params = "cateringBillId")
    public ResponseEntity sendBillTOParentViaEmail(@RequestParam Long cateringBillId) {
        cateringBillService.sendBillToParentViaEmail(cateringBillId);
        return ResponseEntity.noContent().build();
    }
}
