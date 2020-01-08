package jedrekp.daycarecateringbillgenerator.controller;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.DTO.response.CateringBillResponse;
import jedrekp.daycarecateringbillgenerator.service.CateringBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.Month;
import java.time.Year;

@RestController
@RequestMapping("/cateringBills")
@CrossOrigin
@RequiredArgsConstructor
public class CateringBillOperationsController {

    private final CateringBillService cateringBillService;

    @GetMapping(value = "/generate-preview", params = {"childId", "month", "year"})
    public ResponseEntity<CateringBillResponse> getCateringBillPreview(
            @RequestParam long childId, @RequestParam @Valid Month month, @RequestParam Year year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBillPreview(childId, month, year), HttpStatus.OK);
    }

    @GetMapping(value = "/send-to-parent", params = {"cateringBillId"})
    public ResponseEntity sendBillTOParentViaEmail(@RequestParam long cateringBillId) throws IOException, TemplateException, MessagingException {
        cateringBillService.sendBillToParentViaEmail(cateringBillId);
        return ResponseEntity.noContent().build();
    }
}
