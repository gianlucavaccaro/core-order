package com.order.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger logger = LogManager.getLogger(KafkaOrderConsumer.class);
	
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
		logger.info("Received message from "+event.getLastTracking().getServiceName()+" with status "+event.getLastTracking().getStatus()+".");
		
		try {
			orderRecord=event.getLastTracking();
			Ordine order= new Ordine();
			order.setTipologiaOrdine("VENDITA");
			if(orderRecord.getServiceName().equals("core-storage") && orderRecord.getStatus().equals("OK")) {
				orderRecord.setStatus("CONFIRMED");
				service.createOrder(event.getUUID_str(),order.getTipologiaOrdine(), event.getIdProdotto(), event.getNumeroPezzi(), orderRecord.getStatus());
				logger.info("Order successfully confirmed.");
			}
			else {
				orderRecord.setStatus("DELETED");
				try {
					if(orderRecord.getServiceName().equals("core-product") && orderRecord.getFailureReason().equals("ID_NOT_PRESENT"))
						event.setIdProdotto(null);
					service.createOrder(event.getUUID_str(),order.getTipologiaOrdine(), event.getIdProdotto(), event.getNumeroPezzi(), orderRecord.getStatus());
					logger.info("Order created with status "+orderRecord.getStatus()+".");
				} catch (Exception e) {
					e.printStackTrace();
					logger.info("Error creating Order.");
				}
			}
			
			/*else if (orderRecord.getServiceName().equals("core-storage") && (orderRecord.getStatus().equals("ROLLBACK") || orderRecord.getStatus().equals("KO")))
				orderRecord.setStatus("DELETED");
			else if (orderRecord.getServiceName().equals("core-product") && orderRecord.getStatus().equals("KO"))
				orderRecord.setStatus("DELETED");*/
			
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
