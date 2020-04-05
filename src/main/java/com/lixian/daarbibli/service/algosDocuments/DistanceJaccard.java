package com.lixian.daarbibli.service.algosDocuments;

import com.lixian.daarbibli.domain.algoEntities.Jaccard;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DistanceJaccard {
    private String booksResources = "src/main/resources/booksResources/";
    private List<File> fichiers;
    private List<String> result;

    public Map<String, Jaccard> books = new HashMap<>();
    public Map<String, Float> distance = new HashMap<>();


    public DistanceJaccard() {
        this.fichiers =  Arrays.asList(Objects.requireNonNull(new File(booksResources+"indexedFiles").listFiles()));
        this.result = new ArrayList<>();
    }

    public void calculer() throws IOException {

        for (int i = 0; i < fichiers.size(); i++) {
            File file1 = fichiers.get(i);
            Jaccard jaccard1;
            Set<String> mots1;
            if (books.containsKey(file1.getName())) {
                jaccard1 = books.get(file1.getName());
                mots1 = jaccard1.getMotOccurence().keySet();
            }else {
                jaccard1 = this.readIndexWithOccurence(file1);
                mots1 = jaccard1.getMotOccurence().keySet();
                books.put(file1.getName(), jaccard1);
            }

            long cpt = System.currentTimeMillis();

            for (int j = i+1; j < fichiers.size(); j++) {

                File file2 = fichiers.get(j);

//                System.out.println(file1.getName()+ " ; "+file2.getName());
                Set<String> mots2;
                Jaccard jaccard2;
                if (books.containsKey(file2.getName())) {
                    jaccard2 = books.get(file2.getName());
                    mots2 = jaccard2.getMotOccurence().keySet();
                }else {
                    jaccard2 = this.readIndexWithOccurence(file2);
                    mots2 = jaccard2.getMotOccurence().keySet();
                    books.put(file2.getName(), jaccard2);
                }

                float sommeSoustraction = 0;
                float sommeMax = 0;

                Set<String> intersection = mots1.parallelStream().filter(mots2::contains).collect(Collectors.toSet());
                Set<String> difference = mots1.parallelStream().filter(mot -> !intersection.contains(mot)).collect(Collectors.toSet());
                difference.addAll(mots2.parallelStream().filter(mot -> !intersection.contains(mot)).collect(Collectors.toList()));
                float distanceJaccard;
                if(distance.containsKey(file1.getName()+file2.getName())){
                    distanceJaccard = distance.get(file1.getName()+file2.getName());
                }else if (distance.containsKey(file2.getName()+file1.getName())) {
                    distanceJaccard = distance.get(file2.getName()+file1.getName());
                }else {
                    for(String mot: intersection) {
                        int nbW1 = jaccard1.getMotOccurence().get(mot);
                        int nbW2 = jaccard2.getMotOccurence().get(mot);
                        sommeSoustraction += (Integer.max(nbW1, nbW2) - Integer.min(nbW1, nbW2));
                        sommeMax += Integer.max(nbW1, nbW2);
                    }
                    for(String mot: difference) {
                        int nbW = jaccard1.getMotOccurence().containsKey(mot) ?
                            jaccard1.getMotOccurence().get(mot) :
                            jaccard2.getMotOccurence().get(mot);
                        sommeSoustraction += nbW;
                        sommeMax += nbW;
                    }
                    distanceJaccard = sommeSoustraction / sommeMax;
                    distance.put(file1.getName()+file2.getName(), distanceJaccard);
                }
                this.result.add(file1.getName()+","+file2.getName()+"="+distanceJaccard);
            }
            System.out.println(System.currentTimeMillis()-cpt);
        }
        publierValeur();
    }

    public void publierValeur() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(booksResources+"jaccardDistances.txt"));
        for (String r : result) {
            writer.write(r);
            writer.write('\n');
        }
        writer.close();
    }


    /**
     *
     * @return
     * @throws IOException
     */
    public ArrayList<String> readIndex(File file) throws IOException {
        ArrayList<String> result = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(booksResources + "indexedFiles/indexing_"+file.getName()));
        String st;
        while ((st = br.readLine()) != null) {
            //Index index = new Index();
            String[] ligne = st.split("=");
            //index.setMot(ligne[0].trim());
            //String[] liste_mots = ligne[1].split("-");
            //index.setNb_apparition(liste_mots.length);
            result.add(ligne[0].trim());
        }
        return result;
    }

    public Jaccard readIndexWithOccurence(File file) throws IOException {

        ArrayList<String> mot = new ArrayList<String>();
        ArrayList<Integer> occurence = new ArrayList<Integer>();

        Map<String,Integer> motOccurence = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(booksResources + "indexedFiles/"+file.getName()));
        String st;
        while ((st = br.readLine()) != null) {
            String[] ligne = st.split("=");
            mot.add(ligne[0].trim());
            String[] nb_occurence = ligne[1].trim().split("-");
            occurence.add(nb_occurence.length);
            motOccurence.put(ligne[0].trim(), nb_occurence.length);
        }

//        Jaccard result = new Jaccard(mot, occurence);
        Jaccard result = new Jaccard(motOccurence);

        return result;
    }


}
