package com.order.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.model.Ordine;
import com.order.service.OrderService;

import jakarta.annotation.PostConstruct;

@Component
public class KafkaOrderConsumer {
	
	@Autowired
	private OrderService service;
	@Autowired
	private KafkaOrderProducer producer;
	
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void init() {
		objectMapper=new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	

	@KafkaListener(topics="TOPIC_ORDER_IN",groupId="CORE-PRODUCT_KAFKA_TOPIC_ORDER_IN")
	public void consume(String message) throws JsonProcessingException {
		//prendo i dati e memorizzo in db - check su fine transazione
		OrderEvent event=objectMapper.readValue(message, OrderEvent.class);
		TrackingEvent orderRecord=new TrackingEvent();
		System.out.println("Messaggio ricevuto in order:" + event.getLastTracking().toString());
		
		try {
			orderRecord=event.getLastTracking();
			Ordine order= new Ordine();
			order.setTipologiaOrdine("VENDITA");
			if(orderRecord.getServiceName().equals("core-storage") && orderRecord.getStatus().equals("OK"))
				orderRecord.setStatus("CONFIRMED");
			else if (orderRecord.getServiceName().equals("core-storage") && (orderRecord.getStatus().equals("ROLLBACK") || orderRecord.getStatus().equals("KO")))
				orderRecord.setStatus("DELETED");
			else if (orderRecord.getServiceName().equals("core-product") && orderRecord.getStatus().equals("KO"))
				orderRecord.setStatus("DELETED");
			service.createOrder(order.getTipologiaOrdine(), event.getIdProdotto(), event.getNumeroPezzi(), orderRecord.getStatus());
			event.getTracking().add(orderRecord);
			orderRecord.setServiceName("core-order");
			producer.sendAckOrder(event);
		} catch (Exception e) {
			e.printStackTrace();
			orderRecord.setStatus("KO");
			orderRecord.setServiceName("core-order");
			event.getTracking().add(orderRecord);
			producer.sendAckOrder(event);
		}
	}
}
