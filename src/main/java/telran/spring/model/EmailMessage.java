package telran.spring.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class EmailMessage extends Message {
	
	@Email
	String emailAddress;

		
}
