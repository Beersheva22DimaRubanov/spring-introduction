package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controler.SenderController;
import telran.spring.model.Message;
import telran.spring.service.Sender;

@Service
class MockSender implements Sender {

	@Override
	public String send(Message message) {
		return "test";
	}

	@Override
	public String getMessageTypeString() {
		return "test";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {
		return Message.class;
	}

}

@WebMvcTest({ SenderController.class, MockSender.class })

class SendersControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	Message message;
	String sendUrl = "http://localhost:8080/sender";
	String getTypesUrl = sendUrl;
	String isTypePathUrl = String.format("%s/type", sendUrl);

	@BeforeEach
	void setUp() {
		message = new Message();
		message.setText("test");
		message.setType("test");
	}

	@Test
	void sendRightFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBas(messageJson).andExpect(status().isOk()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("test", response);
	}

	private ResultActions getRequestBas(String messageJson) throws Exception {
		return mockMvc.perform(post(sendUrl).contentType(MediaType.APPLICATION_JSON).content(messageJson))
				.andDo(print());
	}

	@Test
	void sendNotFoundFlow() throws Exception {
		message.setType("abc");
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBas(messageJson).andExpect(status().isNotFound()).andReturn().getResponse()
				.getContentAsString();
		assertEquals("Wrong message type abc", response);
	}

	@Test
	void sendValidationViolationFlow() throws Exception {
		message.setType("123");
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBas(messageJson).andExpect(status().isBadRequest()).andReturn().getResponse()
				.getContentAsString();
		assertTrue(response.contains("mismatches"));
	}

	@Test
	void getTypesTest() throws Exception {
		String response = mockMvc.perform(get(getTypesUrl)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		String[] typesResponse = mapper.readValue(response, String[].class);
		assertArrayEquals(new String[] {"test"}, typesResponse);
	}
	
	@Test
	void isTypePathExists() throws Exception{
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(booleanResponse);
	}
	
	@Test
	void isTypePathNotxists() throws Exception{
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test1")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(booleanResponse);
	}
	
	@Test
	void isTypePathParamExists() throws Exception{
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(booleanResponse);
	}

	@Test
	void isTypePathParamNotxists() throws Exception{
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test1")).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean booleanResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(booleanResponse);
	}
	
	@Test
	void isTypePathParamMissing() throws Exception{
		String responseJson = mockMvc.perform(get(isTypePathUrl)).andDo(print())
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertTrue(responseJson.contains("must"));
	}
	
	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc);
	}

}
