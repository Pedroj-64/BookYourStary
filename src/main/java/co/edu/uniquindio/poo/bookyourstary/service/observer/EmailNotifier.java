package co.edu.uniquindio.poo.bookyourstary.service.observer;

import co.edu.uniquindio.poo.bookyourstary.config.EmailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailNotifier implements Observer {

    private final EmailConfig emailConfig;

    public EmailNotifier(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Override
    public void update(String message, String toEmail) {
        try {
            // Envío por defecto (puedes dejarlo como texto plano o HTML simple)
            sendHtmlEmail(toEmail, "Notificación BookYourStary", message);
        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    public void sendPlainTextEmail(String to, String subject, String textBody) throws MessagingException {
        sendEmail(to, subject, textBody, "text/plain; charset=utf-8");
    }

    public void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        sendEmail(to, subject, htmlBody, "text/html; charset=utf-8");
    }

    private void sendEmail(String to, String subject, String content, String mimeType) throws MessagingException {
        Session session = Session.getInstance(emailConfig.getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailConfig.getFromEmail(), emailConfig.getPassword());
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailConfig.getFromEmail()));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setContent(content, mimeType);

        Transport.send(message);
    }
}
