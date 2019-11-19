package jedrekp.daycarecateringbillgenerator.Controller;

import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
import jedrekp.daycarecateringbillgenerator.Service.CateringBillService;
import jedrekp.daycarecateringbillgenerator.Service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.Month;

@RestController
@CrossOrigin
public class CateringBillController {

    @Autowired
    CateringBillService cateringBillService;

    @Autowired
    EmailSenderService emailSenderService;

    @GetMapping(value = "/cateringBills/children/{childId}", params = {"month", "year"})
    public ResponseEntity<CateringBill> generateMonthlyCateringBillForChild(
            @PathVariable Long childId, @RequestParam Month month, @RequestParam Integer year) {
        return new ResponseEntity<>(
                cateringBillService.generateCateringBill(childId, month, year), HttpStatus.OK);
    }

    @GetMapping(value = "/email")
    public ResponseEntity sendEmail() {
        try {
            this.emailSenderService.sendEmail();
        } catch (IOException | TemplateException | MessagingException ex) { ex.printStackTrace();}
        return ResponseEntity.noContent().build();
    }
}
