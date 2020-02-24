package net.bombdash.core.api.mail.senders.sendgrid;

import com.google.gson.Gson;
import net.bombdash.core.api.mail.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Service
public class SendGrid implements EmailSender {
    private RestTemplate template;

    @PostConstruct
    public void onInit() {
        template = new RestTemplate();
        template
                .getMessageConverters()
                .add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }

    @Autowired
    private Gson gson;

    @Async
    @Override
    public void sendEmail(String to, String subject, String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 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");
        headers.setContentType(MediaType.APPLICATION_JSON);
        SendGridRequest sendGridRequest = new SendGridRequest(to, subject, text);
        String json = gson.toJson(sendGridRequest);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        template.postForObject("https://api.sendgrid.com/v3/mail/send", request, SendGridResponse.class);
    }
}
