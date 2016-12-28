/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import model.bean.Mail;

/**
 *
 * @author TLDs
 */
public class EmailContact {

    private Properties properties;
    private static Session session;
    private static Store store;
    private String hostGetMail = "imap.gmail.com";
    private String hostSendMail = "smtp.gmail.com";

    public EmailContact() {
        properties = new Properties();

        //set up imap
        properties.put("mail.imap.host", hostGetMail);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.starttls.enable", "true");

        //set up smtp
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", hostSendMail);
        properties.put("mail.smtp.port", "587");

    }

    public boolean connectMail(String user, String password) {
        try {
            session = Session.getInstance(properties,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            });

            store = session.getStore("imaps");
            store.connect(hostGetMail, user, password);

        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
        return true;
    }

    /**
     *
     * @param user
     * @param pass
     * @param folderName
     * @return ArrayList<Mail>
     */
    public ArrayList<Mail> getMails(String user, String pass, String folderName) {
        ArrayList<Mail> mails = new ArrayList<>();

        try {
            connectMail(user, pass);
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            Folder emailFolder = store.getFolder(folderName);
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);
            if (messages.length == 0) {
                mails = null;
            } else {
                for (int i = 0, n = messages.length; i < n; i++) {
                    Message message = messages[i];
                    System.out.println("---------------------------------");
                    System.out.println("Email Number " + (i + 1));
                    System.out.println("Subject: " + message.getSubject());
                    System.out.println("Date: " + message.getReceivedDate().toString());
                    System.out.println("From: " + getFrom(message));

                    mails.add(new Mail(message.getSubject(), message.getReceivedDate().toString(), getFrom(message), getTo(message), getMailContent(message)));
                }
            }
            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (Exception ex) {
            System.out.println("Co loi xay ra: " + ex);
        }
        return mails;
    }

    public static String getFrom(Message m) throws Exception {
        Address[] a;
        String from = "";
        // FROM
        if ((a = m.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                from += a[j].toString();
            }
        }
        return from;
    }

    public static String getTo(Message m) throws Exception {
        Address[] a;
        String to = "";

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                to += a[j].toString();
            }
        }
        return to;
    }

    private String getMailContent(Message message) throws Exception {
        String content = "";

        if (message.isMimeType("text/plain")) {
            content = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            content = getTextFromMimeMultipart(mimeMultipart);
        }
        return content;
    }

    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                System.out.println("Xu li html");
                result = result + bodyPart.getContent().toString().replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public boolean send(ArrayList<String> to, String from, String password,
            String messageSubject, String messageContent, ArrayList<File> attachFiles) {

        System.out.println("Setup done!");

        // Get the default Session object.
        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: địa chỉ mail người gửi
            message.setFrom(new InternetAddress(from));

            // Set To: Danh sách mail những người nhận
            for (int i = 0; i < to.size(); i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.get(i)));
            }

            // Set Subject: tiêu đề Email
            message.setSubject(messageSubject);

            // Create the message part 
            BodyPart messageBodyPart = new MimeBodyPart();

            // Nội dung text của Email
            messageBodyPart.setText(messageContent);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Thêm các file đính kèm
            if (!attachFiles.isEmpty()) {
                for (File file : attachFiles) {
                    messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(file.getAbsolutePath());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(getFileName(file.getAbsolutePath()));
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            // Send the complete message parts
            message.setContent(multipart);

            System.out.println("content done");

            // Send message
            Transport.send(message, from, password);
            System.out.println("send done");
            return true;
        } catch (MessagingException mex) {
            System.out.println(mex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return false;
    }

    private static String getFileName(String urlFile) {
        String fileName;
        int indexFileName = urlFile.lastIndexOf('\\') + 1;
        fileName = urlFile.substring(indexFileName);
        return fileName;
    }
}
