package com.lixian.daarbibli.domain.algoEntities;

import java.util.ArrayList;
import java.util.List;

public class Parcours {
    private String noeud;
    private List<String> parcouru;
    private Double cout;
    public Parcours (String noeud, List<String> parcouru, Double cout) {
        this.parcouru = new ArrayList<String>();
        for (String s : parcouru) {
            this.parcouru.add(s);
        }
        this.parcouru.add(noeud);
        this.noeud = noeud;
        this.cout = cout.doubleValue();
    }
    public Parcours (String noeud, Double cout) {
        this.parcouru = new ArrayList<String>();
        this.parcouru.add(noeud);
        this.noeud = noeud;
        this.cout = cout;
    }
    public boolean hasParcouru(String n) {
        if (parcouru.contains(n)) {
            return true;
        }
        return false;
    }
    public boolean DepasseCout(Double result) {
        if (result > cout) {
            return true;
        }
        return false;
    }
    public String getNoeud() {
        return noeud;
    }
    public List<String> getParcouru() {
        return parcouru;
    }
    public Double getCout() {
        return cout;
    }
    public void setCout(Double cout) {
        this.cout = cout;
    }
    public void setParcouru(List<String> parcouru) {
        this.parcouru = parcouru;
    }
    public void setNoeud(String noeud) {
        this.noeud = noeud;
    }
    public boolean containsV(String v) {
        if (parcouru.contains(v)) {
            return true;
        }
        return false;
    }
    public String toString() {
        String result = "";
        for (int i = 0; i < parcouru.size(); i++) {
            if (i==0) {
                result=parcouru.get(i);
            } else {
                result+="-"+parcouru.get(i);
            }
        }
        result+="="+cout;
        return result;
    }
}
