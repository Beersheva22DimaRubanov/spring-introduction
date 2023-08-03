package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
public class Message {
	@NotEmpty @Pattern(regexp = "[a-z]{3,5}", message = "type value mismatches pattern")
	String type;
	@NotEmpty
	String text;
	
}
