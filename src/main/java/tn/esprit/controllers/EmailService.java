package tn.esprit.controllers;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    public static void sendAccountCreationEmail(String to, String password, String role) {
        String subject = "Your Account Has Been Created";
        String body = "<p style='font-size:16px; font-family: Arial, sans-serif;'>"
                + "Dear User,<br><br>"
                + "Your account has been successfully created as a <strong>" + role + "</strong>.<br>"
                + "Here are your login credentials:<br><br>"
                + "<strong>Login (Email): </strong>" + to + "<br>"
                + "<strong>Password: </strong>" + password + "<br><br>"
                + "Please change your password after logging in.<br><br>"
                + "Best regards,<br>"
                + "Your Application Team"
                + "</p>";

        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create a Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("amorrions@gmail.com", "ragr xvjs cqkx anjk");
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("amorrions@gmail.com")); // Replace with your email
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to.trim()));
            message.setSubject(subject);
            message.setContent(body, "text/html"); // HTML format

            // Send the message
            Transport.send(message);

            System.out.println("Account creation email sent successfully!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
