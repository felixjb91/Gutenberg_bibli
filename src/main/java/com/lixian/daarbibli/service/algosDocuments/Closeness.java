package com.lixian.daarbibli.service.algosDocuments;

import com.lixian.daarbibli.service.Utile;

import java.io.*;
import java.util.*;

public class Closeness {

    private String booksResources = "src/main/resources/booksResources/";
    private List<File> fichiers;
    private Map<String,Double> result;

    public Closeness() throws IOException {
        this.result = new HashMap<>();
        this.fichiers =  Arrays.asList(Objects.requireNonNull(new File(this.booksResources+"indexedFiles").listFiles()));
    }

    public void calculer() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(booksResources+"jaccardDistances.txt"));
        ArrayList<ArrayList<String>> jaccards = new ArrayList<ArrayList<String>>();
        String st;
        while ((st = br.readLine()) != null) {
            String[] decoupe = st.split("=");
            String[] fichiers = decoupe[0].split(",");

            ArrayList<String> jacc = new ArrayList<String>();

            jacc.add(fichiers[0].trim());
            jacc.add(fichiers[1].trim());
            jacc.add(decoupe[1].trim());

            jaccards.add(jacc);
        }

        for (File f : fichiers) {
            Double result = 0.0;
            for (ArrayList<String> ja : jaccards) {
                if (ja.get(0).equals(f.getName()) || ja.get(1).equals(f.getName())) {
                    result += Double.parseDouble(ja.get(2).trim());
                }
            }
            if (result != 0.0) {
                result = (fichiers.size()- 1) / result;
            }
            this.result.put(f.getName().split("_")[1], result);
        }
        result = Utile.sortResultByValueDouble(result);
        publierValeur();
    }

    public void publierValeur() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(booksResources+"closeness.txt"));
        for (Map.Entry<String,Double> entry: result.entrySet()) {
            writer.write(entry.getKey()+"="+ entry.getValue());
            writer.write('\n');
        }
        writer.close();
    }

}
