package jedrekp.preschoolcateringbillcalculator.Controller;

import jedrekp.preschoolcateringbillcalculator.DTO.MonthInput;
import jedrekp.preschoolcateringbillcalculator.DTO.MonthlyCateringBillDTO;
import jedrekp.preschoolcateringbillcalculator.Service.CateringBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CateringBillController {

    @Autowired
    CateringBillService cateringBillService;

    @PostMapping("/cateringBill/child/{childId}")
    public ResponseEntity<MonthlyCateringBillDTO> generateMonthlyCateringBillForChild(
            @PathVariable Long childId, @RequestBody MonthInput monthInput) {
        return new ResponseEntity<>(cateringBillService.generateCateringBill(childId, monthInput.getMonth(), monthInput.getYear()), HttpStatus.OK);

    }

}
