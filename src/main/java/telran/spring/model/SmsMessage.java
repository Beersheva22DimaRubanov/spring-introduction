package telran.spring.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SmsMessage extends Message {
	@Pattern(regexp = "\\d{5,10}")
	public String phoneNumber;

}
