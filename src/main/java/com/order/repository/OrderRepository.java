package com.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.model.Ordine;

public interface OrderRepository extends JpaRepository<Ordine, Long>{

	public Optional<Ordine> findByUuidStr(String uuid);
}
