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
import javax.activation.*;

public class XXXSendFileEmailXXXX {

//    public static void main(String[] args) {
//        // Recipient's email ID needs to be mentioned.
//        String to = "thanghoangbks2014@gmail.com";
//
//        // Sender's email ID needs to be mentioned
//        String from = "thanghoangbks2014@gmail.com";
//
//        //Password of sender's email
//        String password = "01696578341";
//
//        // Assuming you are sending email from localhost
//        String host = "smtp.gmail.com";
//
//        // Get system properties
//        Properties properties = System.getProperties();
//
//        // Setup mail server
//        properties.setProperty("mail.smtp.host", host);
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.port", "587");
//
//        // Get the default Session object.
//        Session session = Session.getInstance(properties,
//                new javax.mail.Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(from, password);
//            }
//        });
//
//        try {
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("Test Gui File");
//
//            // Create the message part 
//            BodyPart messageBodyPart = new MimeBodyPart();
//
//            // Fill the message
//            messageBodyPart.setText("Test gui file Text.txt thoi nhe!");
//
//            // Create a multipar message
//            Multipart multipart = new MimeMultipart();
//
//            // Set text message part
//            multipart.addBodyPart(messageBodyPart);
//
//            // Part two is attachment
//            messageBodyPart = new MimeBodyPart();
//            String urlFile = "F:\\Projects\\Java\\DALTM\\src\\daltm\\text.txt";
//            DataSource source = new FileDataSource(urlFile);
//            messageBodyPart.setDataHandler(new DataHandler(source));
//            messageBodyPart.setFileName(getFileName(urlFile));
//            multipart.addBodyPart(messageBodyPart);
//
//            // Send the complete message parts
//            message.setContent(multipart);
//
//            // Send message
//            Transport.send(message, from, password);
//            System.out.println("Sent message & attachment successfully....");
//        } catch (Exception mex) {
//            System.out.println(mex);
//            System.out.println("Gui mail khong thanh cong!");
//        }
//    }
//
//    private static String getFileName(String urlFile) {
//        String fileName = "";
//        int indexFileName = urlFile.lastIndexOf('\\') + 1;
//        fileName = urlFile.substring(indexFileName);
//        return fileName;
//    }
}
