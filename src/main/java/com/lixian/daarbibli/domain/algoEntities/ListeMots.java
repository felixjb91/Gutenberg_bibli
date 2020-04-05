package com.lixian.daarbibli.domain.algoEntities;
import java.util.ArrayList;

public class ListeMots {
	private ArrayList<Integer> listeId;

	public ListeMots(int id) {
		listeId = new ArrayList<Integer>();
		listeId.add(id);
	}
	public ArrayList<Integer> listeId() {
		return listeId;
	}
	public void addId(int id) {
		listeId.add(id);
	}
	public int size() {
		return listeId.size();
	}
	public int get(int id) {
		return listeId.get(id);
	}
}
