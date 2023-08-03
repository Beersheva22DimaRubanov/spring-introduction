package telran.spring;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.model.EmailMessage;
import telran.spring.model.TcpMessage;
import telran.spring.service.EmailSender;

@SpringBootTest

class EmailSenderTest {
	@Autowired
	EmailSender sender;

	@Test
	void emailSenderRightFlow() {
		EmailMessage message = new EmailMessage();
		message.setType("email");
		message.setText("text");
		message.setEmailAddress("test@gmail.com");
		String expected = String.format("email sender - text: %s has been sent to %s", message.getText(),
					message.getEmailAddress());
		assertEquals(expected, sender.send(message));
	}
	
	@Test
	void emailSenderWrongType() {
		TcpMessage message = new TcpMessage();
		message.setType("email1");
		message.setText("text");
		message.setHostName("test@gmail.com");
//		String expected = String.format("email sender - text: $s has been sent to mail %s", message.getText(),
//				message.getEmailAddress());
		assertThrowsExactly(IllegalArgumentException.class, ()->sender.send(message));
	}

}
