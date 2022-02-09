package jedrekp.daycarecateringbillgenerator.controller;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.DTO.response.CateringBillResponse;
import jedrekp.daycarecateringbillgenerator.service.CateringBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
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
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity<CateringBillResponse> getCateringBillPreview(
            @RequestParam long childId, @RequestParam Month month, @RequestParam Year year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBillPreview(childId, month, year), HttpStatus.OK);
    }

    @GetMapping(value = "/send-to-parent", params = {"cateringBillId"})
    @PreAuthorize("hasRole('HEADMASTER')")
    public ResponseEntity sendBillTOParentViaEmail(@RequestParam long cateringBillId) throws TemplateException, IOException, MessagingException {
        cateringBillService.sendBillToParentViaEmail(cateringBillId);
        return ResponseEntity.noContent().build();
    }
}
