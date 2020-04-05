package com.lixian.daarbibli.domain.algoEntities;

import java.util.ArrayList;
import java.util.List;

public class Voisins {
    public String point;
    public List<String> voisins;
    public List<Double> valeurs;

    public Voisins(String point) {
        this.point = point;
        this.voisins = new ArrayList<String>();
        this.valeurs = new ArrayList<Double>();
    }

    public boolean hasVoisins(String voisin) {
        if (this.voisins.contains(voisin)) {
            return true;
        }
        return false;
    }
    public void add(String voisin, Double valeur, Double treShold) {
        if (valeur >= treShold) {
            this.voisins.add(voisin);
            this.valeurs.add(valeur);
        }
    }
    public String getPoint() {
        return point;
    }
    public void setPoint(String point) {
        this.point = point;
    }
    public List<String> getVoisins() {
        return voisins;
    }
    public void setVoisins(List<String> voisins) {
        this.voisins = voisins;
    }
    public List<Double> getValeurs() {
        return valeurs;
    }
    public void setValeurs(List<Double> valeurs) {
        this.valeurs = valeurs;
    }
    public String toString() {
        String result = point;
        for (String s : voisins) {
            result += ","+s;
        }
        return result;
    }
}
