package com.ss.utopia.email;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ss.utopia.entity.RegistrationConfirmation;
import com.ss.utopia.entity.ResetPasswordConfirmation;
import com.ss.utopia.entity.User;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SesException;

@Service
public class EmailSender {
	
	@Value("${aws.emailSender}")
    private String sender;

	private Region region = Region.US_EAST_2;
	
	public void sendForgetPasswordEmail(User user, ResetPasswordConfirmation confirmation) {
		String subject = "Utopia Airlines Forgotten Account Password";
		String recipient = user.getEmail();
		String name = user.getGivenName();
		
		String url = "http://127.0.0.1:3000/resetpassword/" + confirmation.getToken();
		
		String bodyHtml = "<html>" + "<h1>Hello " + name + ", </h1>"
				+ "<p>You will now be redirected to our website.</p> <a href='"
				+ url + "'>Click here to change your password.</a>"
				+ "</html>";
		
		String bodyText = "Hello " + name + ", "
				+ "Please use an email client that can display HTML.";
		
		SesClient client = SesClient.builder().region(region).build();
		
		try {
			send(client, sender, recipient, subject, bodyHtml, bodyText);
			client.close();
			System.out.println("closed client........");
		}
		catch (MessagingException e){
			e.getStackTrace();
		}
	}
	
	public void sendConfirmationEmail(User user, RegistrationConfirmation confirmation) {
		String subject = "Utopia Airlines Account Verification";
		String recipient = user.getEmail();
		String name = user.getGivenName();
		String url = "http://localhost:8081/users/registration/" + confirmation.getToken();
		
		String bodyHtml = "<html>" + "<h1>Hello " + name + ", </h1>"
				+ "<p>You're almost done! <a href='" + url + "'>Click here to verify your account.</a>"
				+ "</html>";
		String bodyText = "Hello " + name + ", "
				+ "Please use an email client that can display HTML.";
		
		SesClient client = SesClient.builder().region(region).build();
		
		try {
			send(client, sender, recipient, subject, bodyHtml, bodyText);
			client.close();
			System.out.println("closed client........");
		}
		catch (MessagingException e){
			e.getStackTrace();
		}
	}
	
	private void send(SesClient client, String sender, String recipient,
			String subject, String bodyHtml, String bodyText) throws MessagingException {
		
		Destination destination = Destination.builder().toAddresses(recipient).build();
		
		Content content = Content.builder().data(bodyHtml).build();
		
		Content sub = Content.builder().data(subject).build();
		
		Body body = Body.builder().html(content).build();
		
		Message message = Message.builder().subject(sub).body(body).build();
		
		SendEmailRequest request = SendEmailRequest.builder()
				.destination(destination)
				.message(message)
				.source(sender)
				.build();
		
		try {
			System.out.println("Attempting to send email through SES");
			client.sendEmail(request);
		} catch (SesException e) {
			System.err.println(e.awsErrorDetails().errorMessage());
		}
	}
}
