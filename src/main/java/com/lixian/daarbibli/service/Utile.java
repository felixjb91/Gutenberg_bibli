package com.lixian.daarbibli.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Utile {

    public static Map<String,Double> sortResultByValueDouble(Map<String,Double> result) {
        return result.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new)
            );
    }

    public static Map<String,Integer> sortResultByValueInteger(Map<String,Integer> result) {
        return result.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new)
            );
    }

    public static Map<String,Double> sortResultByValueDoubleReversed(Map<String,Double> result) {
        return result.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new)
            );
    }

//    public static Map<String,Double> getTopResultSorted(Map<String,Double> input, int nbSuggestion) {
//        Map<String,Double> result = new HashMap<>();
//        List<String> resultKey = new ArrayList<>();
//        List<Double> resultValue = new ArrayList<>();
//        int indexOfMax=0;
//        for(Map.Entry<String,Double> entry : input.entrySet()) {
//            if (resultKey.size() < nbSuggestion) {
//                resultKey.add(entry.getKey());
//                resultValue.add(entry.getValue());
//            }
//            else {
//                indexOfMax = resultValue.indexOf(Collections.max(resultValue));
//                if (entry.getValue() < Collections.min(resultValue)) {
//                    resultKey.set(indexOfMax, entry.getKey());
//                    resultValue.set(indexOfMax, entry.getValue());
//                }
//            }
//        }
//        for(int i=0; i<nbSuggestion; i++)  result.put(resultKey.get(i), resultValue.get(i));
//        return sortResultByValueDoubleReversed(result);
//    }

    public static List<Map<String,String>> getTitleAndAuthorFromWeb(List<String> filesname) {
        Map<String, String> authorMap = new HashMap<>();
        Map<String, String> titleMap = new HashMap<>();
        filesname.forEach(file -> {
            Document doc = null;
            try {
                doc = Jsoup.connect("http://www.gutenberg.org/ebooks/"+file.substring(0,file.length()-4)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert doc != null;
            Elements table = doc.select("#bibrec > div:nth-child(1) > table > tbody");
            Elements author = table.select("tr:nth-child(1) > td > a");
            Elements title = table.select("tr:nth-child(2) > td");
            titleMap.put(file, title.get(0).text());
            authorMap.put(file, author.get(0).text());
        });
        return new ArrayList<>(Arrays.asList(titleMap,authorMap));
    }

    public static List<Map<String,String>> getTitleAndAuthorFromTxt(List<String> files) {
        Map<String, String> authorMap = new HashMap<>();
        Map<String, String> titleMap = new HashMap<>();
        List<String> listFileToGetOnWeb = new ArrayList<>();
        files.forEach(file -> {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(new File("src/main/resources/booksResources/indexedFiles/indexing_" + file)));
                String ligne;
                int cpt = 0;
                String numTitle = "";
                String numAuthor = "";
                while ((ligne = br.readLine()) != null) {
                    if (cpt > 2) break;
                    cpt ++;
                    String[] subList = ligne.split(" ");
                    if(subList[0].equals("title")) numTitle = subList[2];
                    if(subList[0].equals("author")) numAuthor = subList[2];
                }
                br.close();

                if(!numTitle.isEmpty() && !numAuthor.isEmpty()) {

                    br = new BufferedReader(new FileReader(new File("src/main/resources/booksResources/gutenbergBooks/" + file)));
                    int i=0;
                    int numT = Integer.parseInt(numTitle.split(",")[0])-1;
                    int numA = Integer.parseInt(numAuthor.split(",")[0])-1;
                    while ((ligne = br.readLine()) != null || i <= Integer.max(numA,numT)) {
                        if (i == numA) authorMap.put(file, ligne.split(":")[1].substring(1));
                        if (i == numT) titleMap.put(file, ligne.split(":")[1].substring(1));
                        i++;
                    }
                }
                else listFileToGetOnWeb.add(file);
//                else listFileToGetOnWeb.add(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        files.removeAll(listFileToGetOnWeb);
        return new ArrayList<>(Arrays.asList(titleMap,authorMap));
    }



}
