package com.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.order.model.Ordine;
import com.order.service.OrderService;
import com.order.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/store/ordini")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping
	public ResponseEntity<List<Ordine>> getAllOrders(){
		List<Ordine> listOrders=orderService.retrieveAll();
		if(listOrders.isEmpty() || listOrders.size()==0)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(listOrders,HttpStatus.OK);
	}
	
	@GetMapping("/orderById")
	public ResponseEntity<Ordine> getOrderById(@RequestParam(required=true) Long idOrdine) {
		try {
			Ordine order=orderService.retrieveById(idOrdine);
			return new ResponseEntity<Ordine>(order,HttpStatus.OK);
		} catch(ResourceNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/orderByUuid")
	public ResponseEntity<Ordine> getOrderByUuid(@RequestParam(required=true) String uuid) {
		try {
			Ordine order=orderService.retrieveByUuid(uuid);
			return new ResponseEntity<Ordine>(order,HttpStatus.OK);
		} catch(ResourceNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/createOrder")
	public ResponseEntity<Ordine> createOrder(@RequestParam(required=true) String uuid,@RequestParam(required=true) String tipologia, @RequestParam(required=true) Long idProdotto, @RequestParam(required=true) int numeroPezzi, @RequestParam(required=true) String stato){
		try {
			Ordine order=orderService.createOrder(uuid,tipologia, idProdotto, numeroPezzi,stato);
			return new ResponseEntity<Ordine>(order,HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("/deleteOrder")
	public ResponseEntity<String> deleteOrder(@RequestParam(required=true) Long idOrdine) {
		try {
			orderService.deleteOrder(idOrdine);
			return new ResponseEntity<String>("Deleted.",HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Resource not found",HttpStatus.OK);
		}
		
	}
	
}
