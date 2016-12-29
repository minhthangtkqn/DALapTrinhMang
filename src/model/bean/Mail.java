/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.bean;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author TLDs
 */
public class Mail {

    private String subject, time, from, to, content;
    private Message message;

    public Mail(String subject, String from, Message message) {
        this.subject = subject;
        this.from = from;
        this.message = message;
    }

    public Mail(String subject, String time, String from, String to, String content) {

        if (from.contains("UTF-8")) {
            this.from = from.substring(from.indexOf("<") + 1, from.indexOf(">"));
        } else {
            this.from = from;
        }

        this.subject = subject;
        this.time = time;
        this.to = to;
        this.content = content;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

//    public String getTime() {
//        String tmp = "";
//        try {
//            tmp = message.getReceivedDate().toString();
//        } catch (MessagingException ex) {
//            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return tmp;
//    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

//    public String getContent() {
//        String tmp = "";
//        System.out.println("chuan bi lay noi dung");
//        try {
//            tmp = getMailContent(message);
//        } catch (Exception ex) {
//            Logger.getLogger(Mail.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.out.println("lay noi dung xong");
//        return tmp;
    public String getContent() {
        return content;
    }

//    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    private String getMailContent(Message message) throws Exception {
        String result = "";

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
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
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
