/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import model.bean.Mail;

/**
 *
 * @author TLDs
 */
public class EmailContact {

    Properties properties;
    static Session session;
    static Store store;
    String host = "imap.gmail.com";
    static String username, password;

    public EmailContact() {
        properties = new Properties();

        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.starttls.enable", "true");

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
            store.connect(host, user, password);

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
     * @return
     */
    public ArrayList<Mail> getMails(String user, String pass) {
        ArrayList<Mail> mails = new ArrayList<>();

        try {
            connectMail(user, pass);//TODO remove
        } catch (Exception e) {
            System.out.println(e);
        }

        try {

            Folder emailFolder = store.getFolder("inbox");
            emailFolder.open(Folder.READ_ONLY);
//            emailFolder.open(Folder.HOLDS_MESSAGES);

            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + getFrom(message));
                System.out.println("To: " + getTo(message));
                System.out.println("Text: " + getMailContent(message));

                mails.add(new Mail(message.getSubject(), getFrom(message), getMailContent(message)));
            }

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
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

//        if (part.isMimeType("multipart/*")) {
//            Multipart mp = (Multipart) part.getContent();
//            int count = mp.getCount();
//            for (int i = 0; i < count; i++) {
//                getMailContent(mp.getBodyPart(i));
//                if (mp.getBodyPart(i).isMimeType("text/plain")) {
//                    return (String) mp.getBodyPart(i).getContent();
//                }
//            }
//        }
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
}
