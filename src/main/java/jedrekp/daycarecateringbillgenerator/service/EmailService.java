package jedrekp.daycarecateringbillgenerator.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jedrekp.daycarecateringbillgenerator.entity.CateringBill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    @Qualifier("emailConfiguration")
    private final Configuration emailConfiguration;

    @Value("${spring.mail.username}")
    private String mailSenderAddress;

    public void sendEmailWithCateringBill(CateringBill cateringBill, BigDecimal totalDue)
            throws MessagingException, IOException, TemplateException {

        log.info("Sending Email to: {}", cateringBill.getChild().getParentEmail());

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        String htmlEmailTemplate = prepareHtmlTemplate(cateringBill, totalDue);
        String subject = createEmailSubject(cateringBill);

        mimeMessageHelper.setTo(cateringBill.getChild().getParentEmail());
        mimeMessageHelper.setText(htmlEmailTemplate, true);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setFrom(mailSenderAddress);

        emailSender.send(message);

    }

    private String prepareHtmlTemplate(CateringBill cateringBill, BigDecimal totalDue) throws IOException, TemplateException {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("month", MessageFormat.format("{0} {1}",
                cateringBill.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), String.valueOf(cateringBill.getYear())));
        templateModel.put("childName", MessageFormat.format("{0} {1}",
                cateringBill.getChild().getFirstName(), cateringBill.getChild().getLastName()));
        templateModel.put("correction", cateringBill.isCorrection());
        templateModel.put("dailyOrders", cateringBill.getDailyCateringOrders());
        templateModel.put("totalDue", totalDue);
        Template template = emailConfiguration.getTemplate("catering-bill-email.ftl");
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, templateModel);
    }

    private String createEmailSubject(CateringBill cateringBill) {
        String subject = MessageFormat.format("{0} {1} daycare catering bill for {2} {3}",
                cateringBill.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), String.valueOf(cateringBill.getYear()),
                cateringBill.getChild().getFirstName(), cateringBill.getChild().getLastName());
        if (cateringBill.isCorrection()) {
            subject = subject + " (correction)";
        }
        return subject;
    }
}
