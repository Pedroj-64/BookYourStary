package co.edu.uniquindio.poo.bookyourstary.config;

import java.util.Properties;

public class EmailConfig {

    private final String fromEmail;
    private final String password;

    public EmailConfig(String fromEmail, String password) {
        this.fromEmail = fromEmail;
        this.password = password;
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getPassword() {
        return password;
    }
}
