package telran.spring.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.model.Message;

public interface Sender {
	String errorMessage = "Message has a wrong type";
String send(Message message);
String getMessageTypeString();
Class<? extends Message> getMessageTypeObject();
}
