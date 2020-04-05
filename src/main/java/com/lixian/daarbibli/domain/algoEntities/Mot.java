package com.lixian.daarbibli.domain.algoEntities;

public class Mot {
	private int indice_ligne;
	private int indice_mot;
	private String mot;

	public Mot(int indice_ligne, int indice_mot, String mot) {
		this.indice_ligne = indice_ligne;
		this.indice_mot = indice_mot;
		this.mot = mot;
	}
	public int getIndice_ligne() {
		return indice_ligne;
	}
	public void setIndice_ligne(int indice_ligne) {
		this.indice_ligne = indice_ligne;
	}
	public int getIndice_mot() {
		return indice_mot;
	}
	public void setIndice_mot(int indice_mot) {
		this.indice_mot = indice_mot;
	}
	public String getMot() {
		return mot;
	}
	public void setMot(String mot) {
		this.mot = mot;
	}
}
