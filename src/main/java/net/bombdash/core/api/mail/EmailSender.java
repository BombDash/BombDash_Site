package net.bombdash.core.api.mail;

public interface EmailSender {
    void sendEmail(String to, String subject,String text);
}
