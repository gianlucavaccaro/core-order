package com.order.kafka;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderEvent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String UUID_str;
	private Long idProdotto;
	private Long idMagazzino;
	private int numeroPezzi;
	List<TrackingEvent> tracking;

	public List<TrackingEvent> getTracking() {
		if(tracking == null) {
			tracking = new ArrayList<>();
		}
		return tracking;
	}

	public void setTracking(List<TrackingEvent> tracking) {
		this.tracking = tracking;
	}
	
	public TrackingEvent getLastTracking() {
		if(tracking == null) {
			tracking = new ArrayList<>();
		}
		if(tracking.isEmpty()) {
			return new TrackingEvent();
		}
		return tracking.get(tracking.size()-1);
	}

	public String getUUID_str() {
		return UUID_str;
	}

	public void setUUID_str(String uUID_str) {
		UUID_str = uUID_str;
	}

	public Long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Long idProdotto) {
		this.idProdotto = idProdotto;
	}

	public Long getIdMagazzino() {
		return idMagazzino;
	}

	public void setIdMagazzino(Long idMagazzino) {
		this.idMagazzino = idMagazzino;
	}

	public int getNumeroPezzi() {
		return numeroPezzi;
	}

	public void setNumeroPezzi(int numeroPezzi) {
		this.numeroPezzi = numeroPezzi;
	}
	
	
}
