package jedrekp.daycarecateringbillgenerator.Controller;

import jedrekp.daycarecateringbillgenerator.DTO.CateringBillDTO;
import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.Service.CateringBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Month;
import java.time.Year;

@RestController
@RequestMapping("/cateringBills")
@CrossOrigin
public class CateringBillOperationsController {

    @Autowired
    CateringBillService cateringBillService;

    @GetMapping(value = "/display-preview", params = {"childId", "month", "year"})
    public ResponseEntity<CateringBillDTO> getCateringBillPreview(
            @RequestParam Long childId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBillPreview(childId, month, year), HttpStatus.OK);
    }

    @GetMapping(value = "/send-to-parent", params = "cateringBillId")
    public ResponseEntity sendBillTOParentViaEmail(@RequestParam Long cateringBillId) {
        cateringBillService.sendBillToParentViaEmail(cateringBillId);
        return ResponseEntity.noContent().build();
    }
}
