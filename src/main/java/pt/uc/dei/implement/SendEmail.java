package pt.uc.dei.implement;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.Serializable;

 
@Named
public class SendEmail implements Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(name = "java:jboss/mail/gmail")
    private Session session;

	@Asynchronous
    public void send(String addresses, String topic, String textMessage) {
 
        try {
 
            Message message = new MimeMessage(session);
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(addresses));
            message.setSubject(topic);
            message.setText(textMessage);
 
            Transport.send(message);
 
        } catch (MessagingException e) {
           System.out.println("SENDING EMAIL...");
            e.printStackTrace();
        }
    }
    


}