package com.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.order.model.Ordine;
import com.order.repository.OrderRepository;
import com.order.exception.ResourceNotFoundException;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public List<Ordine> retrieveAll(){
		return orderRepository.findAll();
	}
	
	public Ordine retrieveById(Long idOrdine) throws ResourceNotFoundException {
		return orderRepository.findById(idOrdine).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
	}
	
	public Ordine retrieveByUuid(String uuid) throws ResourceNotFoundException {
		return orderRepository.findByUuidStr(uuid).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
	}
	
	@Transactional
	public Ordine createOrder(String uuid, String tipologia, Long idProdotto, int numeroPezzi, String stato) {
		Ordine order= new Ordine(uuid, tipologia, idProdotto, numeroPezzi, stato);
		return orderRepository.save(order);
	}
	
	public void deleteOrder(Long idOrdine) throws ResourceNotFoundException {
		Ordine order= orderRepository.findById(idOrdine).orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
		orderRepository.delete(order);
	}
	
}
