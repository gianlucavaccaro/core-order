package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.model.Ordine;

public interface OrderRepository extends JpaRepository<Ordine, Long>{

}
