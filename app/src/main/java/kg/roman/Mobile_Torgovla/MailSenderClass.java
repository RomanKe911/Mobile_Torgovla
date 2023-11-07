package kg.roman.Mobile_Torgovla;

import android.util.Log;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailSenderClass extends javax.mail.Authenticator {
    private String user;
    private String password;
    private String port;
    private String mailhost_new;
    private Session session;
    private String mailhost = "smtp.gmail.com";
    private String mailhost2 = "smtp.mail.ru";
    private String mailhost3 = "mail.sunbell-kg.webhost.kg";

    private Multipart _multipart;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public MailSenderClass(String user, String password, String port, String mailhost_new) {
        this.user = user;
        this.password = password;
        this.port = port;
        this.mailhost_new = mailhost_new;

        _multipart = new MimeMultipart();
        Log.e("MAAIL_Per ", user + " ,/ " + password + " ,/ " + port + " ,/ " + mailhost_new);


        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

       props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        props.put("mail.smtp.host", mailhost_new);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.user", user);
        props.put("mail.smtp.pwd", password);



        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");  //***
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);


       /* props.setProperty("mail.smtp.sender", user);
        props.setProperty("mail.smtp.password", password);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.setProperty("mail.host", "mail.sunbell-kg.webhost.kg");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.socketFactory.port", "587");
        props.setProperty("mail.smtp.ssl.trust", "mail.sunbell-kg.webhost.kg");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");*/


        /*
        //1) get the session object
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        // You have missed this line.
        properties.put("mail.smtp.starttls.enable", "true");
        // This SMTP server works with me for all Microsoft email providers, like: -
        // Outlook, Hotmail, Live, MSN, Office 365 and Exchange.
        properties.put("mail.smtp.host", mailhost_new);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.user", user);
        properties.put("mail.smtp.pwd", password);
*/




















		/*
	  Properties localProperties = new Properties();
        localProperties.setProperty("mail.transport.protocol", "smtp");
        localProperties.setProperty("mail.host", this.mailhost2);
        localProperties.put("mail.smtp.auth", "true");
        localProperties.put("mail.smtp.port", "465");
        localProperties.put("mail.smtp.socketFactory.port", "465");
        localProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        localProperties.put("mail.smtp.socketFactory.fallback", "false");
        localProperties.setProperty("mail.smtp.quitwait", "false");*/
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipients, String filename) throws Exception {

        try {
            MimeMessage message = new MimeMessage(session);

            // ���
            message.setSender(new InternetAddress(sender));
            // � ���
            message.setSubject(subject);
            // ����
            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(recipients));

            // ����� �������
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            _multipart.addBodyPart(messageBodyPart);

            // � ��� ��������
            if (!filename.equalsIgnoreCase("")) {
                BodyPart attachBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filename);
                attachBodyPart.setDataHandler(new DataHandler(source));
                attachBodyPart.setFileName(filename);

                _multipart.addBodyPart(attachBodyPart);
            }

            message.setContent(_multipart);

            Transport.send(message);
       // } catch (MessagingException e) {
        } catch (Exception e) {
            Log.e("sendMail", "Ошибка отправки функцией sendMail! ");
        }
    }
}
