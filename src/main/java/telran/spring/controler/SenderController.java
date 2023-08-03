package telran.spring.controler;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.spring.exceptions.NotFoundException;
import telran.spring.model.Message;
import telran.spring.service.Sender;

@RestController
@RequestMapping("sender")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SenderController {
	Map<String, Sender> senderMap;
	final List<Sender> sendersList;
	final ObjectMapper mapper;

	@PostMapping
	String send(@RequestBody @Valid Message message) {
		log.debug("Controller received message {}", message);
		Sender sender = senderMap.get(message.getType());
		String resWrong = "Wrong message type " + message.getType();
		String res = null;
		if (sender != null) {
			res = sender.send(message);
		} else {
			throw new NotFoundException(resWrong);
		}
		return res;
	}
	
	@GetMapping
	Set<String> getTypes(){
		return senderMap.keySet();
	}
	
	@GetMapping("type/{typeName}")
	boolean isTypeExistsPath(@PathVariable(name = "typeName") String type) {
		log.debug("Type inside a path {}", type);
		return senderMap.containsKey(type);
	}
	
	@GetMapping("type")
	boolean isTypeExistsParam(@RequestParam(name = "type", defaultValue = "") @NotEmpty String type) {
		log.debug("Type inside a parametr {}", type);
		return senderMap.containsKey(type);
	}

	@PostConstruct
	void init() {
		senderMap = sendersList.stream().collect(Collectors.toMap(Sender::getMessageTypeString, s -> s));
		sendersList.forEach(sender -> mapper.registerSubtypes(sender.getMessageTypeObject()));
		log.info("registered senders: {}", senderMap.keySet());
	}

	@PreDestroy
	void shutDown() {
		log.info("Context closed");
	}
}
