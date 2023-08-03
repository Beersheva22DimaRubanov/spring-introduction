package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.EmailMessage;
import telran.spring.model.Message;
import telran.spring.model.TcpMessage;

@Service
@Slf4j
public class TcpSender implements Sender {
	@Override
	public String send(Message message) {
		log.debug("TCP service received message{}", message);
		String res = errorMessage;
		if(message instanceof TcpMessage) {
			TcpMessage tcpMessage =  (TcpMessage) message;
			res = String.format("TCP sender - text: %s has been sent to %s", tcpMessage.getText(), 
					tcpMessage.getHostName(), tcpMessage.getPort());
		} else {
			throw new IllegalArgumentException(res);
		}
		return res;
	}
	
	@Override
	public String getMessageTypeString() {
		// TODO Auto-generated method stub
		return "tcp";
	}

	@Override
	public Class<? extends Message> getMessageTypeObject() {
		// TODO Auto-generated method stub
		return TcpMessage.class;
	}

}
