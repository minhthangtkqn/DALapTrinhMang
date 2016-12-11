/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

/**
 *
 * @author TLDs
 */
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    public String send(ArrayList<String> to, String from, String password, String messageContent, String messageSubject) {
        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Get the default Session object.
        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            for (int i = 0; i < to.size(); i++) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.get(i)));
            }

            // Set Subject: header field
            message.setSubject(messageSubject);

            // Now set the actual message
            message.setText(messageContent);

            // Send message
            Transport.send(message);
            return "Sent message successfully....";
        } catch (MessagingException mex) {
            System.out.println(mex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "Gui mail that bai!";
    }

    public static void main(String[] args) {
//        ArrayList<String> arrayReceiver = new ArrayList<>();
//        arrayReceiver.add("hoangminhthang21051995@gmail.com");
//        arrayReceiver.add("thanghoangbks2014@gmail.com");
//
//        SendEmail sendEmail = new SendEmail();
//        sendEmail.send(arrayReceiver, "hoangminhthang21051995@gmail.com", "toilathang", "Noi dung Email", "Tieu de");

        String abc = "abhs bhsadb adb jhsadb j   asbdj";
        System.out.println(abc.replaceAll("\\s+", ""));
        System.out.println("s");
    }
}
