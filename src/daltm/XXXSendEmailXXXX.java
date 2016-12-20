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
import java.io.File;
import java.util.*;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class XXXSendEmailXXXX {

    public boolean send(ArrayList<String> to, String from, String password,
            String messageSubject, String messageContent, ArrayList<File> attachFiles) {
        // Assuming you are sending email from localhost
        String host = "smtp.gmail.com";

        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        System.out.println("Setup done!");

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

//    public static void main(String[] args) {
////        ArrayList<String> arrayReceiver = new ArrayList<>();
////        arrayReceiver.add("hoangminhthang21051995@gmail.com");
////        arrayReceiver.add("thanghoangbks2014@gmail.com");
////
////        XXXSendEmailXXXX sendEmail = new XXXSendEmailXXXX();
////        sendEmail.send(arrayReceiver, "hoangminhthang21051995@gmail.com", "toilathang", "Noi dung Email", "Tieu de");
//
//        String abc = "abhs bhsadb adb jhsadb j   asbdj";
//        System.out.println(abc.replaceAll("\\s+", ""));
//        System.out.println("s");
//    }
}
