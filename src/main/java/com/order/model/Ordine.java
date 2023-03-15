package com.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="ordine")
public class Ordine {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long idOrdine;
	private String uuidStr;
	private String stato;
	private String tipologiaOrdine;
	private Long idProdotto;
	private int numeroPezzi;
	
	public Ordine(Long idOrdine, String tipologia, Long idProdotto, int numeroPezzi) {
		this.idOrdine = idOrdine;
		this.tipologiaOrdine = tipologia;
		this.idProdotto = idProdotto;
		this.numeroPezzi = numeroPezzi;
	}

	public Ordine(String uuid, String tipologia, Long idProdotto, int numeroPezzi, String stato) {
		this.uuidStr=uuid;
		this.tipologiaOrdine = tipologia;
		this.idProdotto = idProdotto;
		this.numeroPezzi = numeroPezzi;
		this.stato=stato;
	}

	public Ordine() {
	}

	public String getUUID_str() {
		return uuidStr;
	}

	public void setUUID_str(String uUID_str) {
		uuidStr = uUID_str;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}

	public Long getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(Long idOrdine) {
		this.idOrdine = idOrdine;
	}

	public String getTipologiaOrdine() {
		return tipologiaOrdine;
	}

	public void setTipologiaOrdine(String tipologia) {
		this.tipologiaOrdine = tipologia;
	}

	public Long getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(Long idProdotto) {
		this.idProdotto = idProdotto;
	}

	public int getNumeroPezzi() {
		return numeroPezzi;
	}

	public void setNumeroPezzi(int numeroPezzi) {
		this.numeroPezzi = numeroPezzi;
	}

	@Override
	public String toString() {
		return "Ordine [idOrdine=" + idOrdine + ", tipologia=" + tipologiaOrdine + ", idProdotto=" + idProdotto
				+ ", numeroPezzi=" + numeroPezzi + "]";
	}
	
	
}
