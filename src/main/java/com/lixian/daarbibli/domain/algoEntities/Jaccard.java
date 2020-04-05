package com.lixian.daarbibli.domain.algoEntities;

import java.util.Map;

public class Jaccard {
    private Map<String,Integer> motOccurence;

    public Jaccard(Map<String,Integer> motOccurence) {
        this.motOccurence = motOccurence;
    }

    public String toString() {
        String result = "";
        for (Map.Entry<String,Integer> entry: motOccurence.entrySet()) {
            result += entry.getKey()+"-"+entry.getValue()+"\n";
        }
        return result;
    }

    public Integer nbWords() { return this.motOccurence.values().stream().reduce(0, Integer::sum); }

    public Map<String, Integer> getMotOccurence() {
        return motOccurence;
    }
}
