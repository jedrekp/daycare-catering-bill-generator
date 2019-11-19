package jedrekp.daycarecateringbillgenerator.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    @Qualifier("emailConfiguration")
    private Configuration emailConfig;

    public void sendEmail() throws MessagingException, IOException, TemplateException {

        Map model = new HashMap();
        model.put("name", "test");
        model.put("location", "test");
        model.put("signature", "test");
        model.put("content", "test");


        log.info("Sending Email to: " + "jedrzejpopkiewicz@gmail.com");


        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Template template = emailConfig.getTemplate("cateringBillEmail.ftl");
        String htmlEmailTemplate = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

        mimeMessageHelper.setTo("jedrzejpopkiewicz@gmail.com");
        mimeMessageHelper.setText(htmlEmailTemplate, true);
        mimeMessageHelper.setSubject("test");
        mimeMessageHelper.setFrom("daycare");


        emailSender.send(message);

    }
}
