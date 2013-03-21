package model;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 *
 * @author balantis7
 */
public class Mailer {

    private static final String SMTP_HOST_NAME = "smtp.gmail.com";
    private static final int SMTP_HOST_PORT = 465;
    private static final String SMTP_AUTH_USER = "restaurant.page@gmail.com";
    private static final String SMTP_AUTH_PWD  = "apachetomcat";



    public void sendMail(String address, String username,String pass) throws Exception{
        Properties props = new Properties();

        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", SMTP_HOST_NAME);
        props.put("mail.smtps.auth", "true");
        // props.put("mail.smtps.quitwait", "false");

        Session mailSession = Session.getDefaultInstance(props);
        mailSession.setDebug(true);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("Odigos Estiatorion Elladas: Egine epanafora tou kodikou sas");
        message.setContent("Egine epanafora tou kodikou sas. O neos sas kodikos einai "+pass+". Parakalo allakste ton kodiko sintoma.", "text/plain");

        message.addRecipient(Message.RecipientType.TO,
             new InternetAddress(address));

        transport.connect
          (SMTP_HOST_NAME, SMTP_HOST_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);

        transport.sendMessage(message,
            message.getRecipients(Message.RecipientType.TO));
        transport.close();
    }
}