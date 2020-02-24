package net.bombdash.core.api.mail.senders.sendgrid;

import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;

public class SendGridRequest {
    public SendGridRequest(String to, String subject, String html) {
        personalizations = Collections.singletonList(new Personalizations(to));
        from = new From("info@bombdash.net");
        this.subject = subject;
        this.content = Collections.singletonList(new Content(html));
    }

    private List<Personalizations> personalizations;
    private From from;
    private String subject;
    private List<Content> content;

    private class Personalizations {
        public Personalizations(String to) {
            this.to = Collections.singletonList(new Email(to));
        }

        private class Email {
            public Email(String email) {
                this.email = email;
            }

            String email;
        }

        private List<Email> to;
    }

    private class From {
        public From(String email) {
            this.email = email;
        }

        String email;
    }

    private class Content {
        private Content(String html) {
            this.value = html;

        }

        String type = MediaType.TEXT_HTML_VALUE;
        String value;
    }
}
