package com.lixian.daarbibli.domain.algoEntities;

public class Ligne {
	private int numero_ligne;
	private String ligne;
	public Ligne(int numero_ligne, String ligne) {
		this.numero_ligne = numero_ligne;
		this.ligne = ligne;
	}
	public int getNumero_ligne() {
		return numero_ligne;
	}
	public void setNumero_ligne(int numero_ligne) {
		this.numero_ligne = numero_ligne;
	}
	public String getLigne() {
		return ligne;
	}
	public void setLigne(String ligne) {
		this.ligne = ligne;
	}

}
