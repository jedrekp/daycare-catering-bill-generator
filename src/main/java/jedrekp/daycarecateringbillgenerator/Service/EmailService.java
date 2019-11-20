package jedrekp.daycarecateringbillgenerator.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.Entity.CateringBill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    @Qualifier("emailConfiguration")
    private Configuration emailConfig;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    public void sendEmailWithCateringBill(CateringBill cateringBill) throws MessagingException, IOException, TemplateException {

        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("month", MessageFormat.format("{0} {1}",
                cateringBill.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                String.valueOf(cateringBill.getYear())));
        templateModel.put("childName", MessageFormat.format("{0} {1}",
                cateringBill.getChild().getFirstName(), cateringBill.getChild().getLastName()));
        templateModel.put("dailyOrders", cateringBill.getDailyCateringOrders());
        templateModel.put("totalDue", cateringBill.getTotalDue());


        log.info("Sending Email to: {}", cateringBill.getChild().getParentEmail());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Template template = emailConfig.getTemplate("catering-bill-email.ftl");
        String htmlEmailTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);

        String subject = MessageFormat.format("{0} {1} daycare catering bill for {2} {3}",
                cateringBill.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), String.valueOf(cateringBill.getYear()),
                cateringBill.getChild().getFirstName(), cateringBill.getChild().getLastName());

        mimeMessageHelper.setTo(cateringBill.getChild().getParentEmail());
        mimeMessageHelper.setText(htmlEmailTemplate, true);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(mailSenderAddress);

        emailSender.send(message);

    }
}
