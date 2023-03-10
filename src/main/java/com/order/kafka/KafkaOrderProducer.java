package com.order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Component
public class KafkaOrderProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;
	private final String TOPIC_SALE_OUT="TOPIC_ORCHESTRATOR_IN";
	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	private void send(String topic, String message) {
		kafkaTemplate.send(topic, message);
	}
	
	//pubblico sul topic orchestrator in l'order event aggiornato
	public void sendAckOrder(OrderEvent event) throws JsonProcessingException {
		//assumo che event è stato già aggiornato
		String json=objectMapper.writeValueAsString(event);
		send(TOPIC_SALE_OUT,json);
	}
}
