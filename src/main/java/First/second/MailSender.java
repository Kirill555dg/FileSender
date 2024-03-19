package First.second;

import com.sun.mail.imap.protocol.FLAGS;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

import static First.second.Main.properties;
import static First.second.SearchClass.getCurrentMonthName;
import static First.second.SearchClass.getCurrentYear;

public class MailSender {

    public static String SendMail(String to, String pathName) throws IOException {
        String checkFall = null;
        String from = "xxx@yandex.ru"; // ОТ КОГО
        String currentMonth = getCurrentMonthName();
        String currentYear = getCurrentYear();

        final String host = properties.getProperty("mail.imap.host");
        final String user = properties.getProperty("mail.smtp.user");
        final String pwd = properties.getProperty("mail.smtp.password");
        final PasswordAuthentication logpass = new PasswordAuthentication(user, pwd);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return logpass;
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject("Title"); // ТЕКСТ ЗАГОЛОВКА ПИСЬМА

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentPart = new MimeBodyPart();

            MimeBodyPart textPart = new MimeBodyPart();

            try {

                File f = new File(pathName); // ПУТЬ К ФАЙЛУ

                attachmentPart.attachFile(f);
                textPart.setText("Text"); // ТЕКСТ ВНУТРИ ПИСЬМА
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {

                e.printStackTrace();

            }

            message.setContent(multipart);

            Transport.send(message);

            Store store = session.getStore("imap");
            store.connect(host, user, pwd);

            Folder folder = store.getFolder("Счета");
            if (!folder.exists()) {
                folder.create(Folder.HOLDS_MESSAGES);
            }
            folder.open(Folder.READ_WRITE);
            // folder.appendMessages(new Message[]{message});
            try {
                message.setFrom(to);
                folder.appendMessages(new Message[]{message});
                // Message[] msgs = folder.getMessages();
                message.setFlag(FLAGS.Flag.RECENT, true);

            } catch (Exception ignore) {
                System.out.println("error processing message " + ignore.getMessage());
            } finally {
                store.close();
                // folder.close(false);
            }

        } catch (MessagingException mex) {
            mex.printStackTrace();
            return "BADNEWS";
        }
        return "OK";
    }
}
