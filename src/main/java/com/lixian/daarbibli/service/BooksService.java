package com.lixian.daarbibli.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BooksService {


    String pathBooks = "src/main/resources/booksResources/gutenbergBooks";
    String pathIndex = "src/main/resources/booksResources/indexedFiles";
    String pathCloseness = "src/main/resources/booksResources/closeness.txt";
    String pathJaccard = "src/main/resources/booksResources/jaccardDistances.txt";
    List<File> files = Arrays.asList(Objects.requireNonNull(new File(pathIndex).listFiles()));

    public BooksService() {}

    public List<String> getAllFileNameContainingTheWord(String word) {
        return files.parallelStream()
            .filter(file -> {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String st;
                    while ((st = br.readLine()) != null) {
                        String current = st.split(" ")[0];
                        if(current.matches(word)){
                            br.close();
                            return true;
                        }
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            })
            .map(file -> file.getName().substring(9))
            .collect(Collectors.toList());
    }

    public List<String> getFilesSuggestion(String fileName, int nbSuggestion) {
        Map<String,Double> result = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pathJaccard)));
            String st;
            boolean flag = false;
            while ((st = br.readLine()) != null) {
                String current = st.split(",")[0].split("_")[1];
                if (flag && !current.equals(fileName)) break;
                if (!current.equals(fileName)) continue;
                if (!flag) flag = true;
                String second = st.split(",")[1].split("_")[1].split("=")[0];
                String dist = st.split("=")[1];
                result.put(second,Double.parseDouble(dist));
            }
            br.close();
            result = Utile.sortResultByValueDoubleReversed(result);
//            result = Utile.getTopResultSorted(result, nbSuggestion);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(result.keySet()).subList(0,nbSuggestion);
//        return new ArrayList<>(result.keySet());
    }

    public List<String> sortBookByClosness(List<String> filesName) {
        File closenessFile = new File(pathCloseness);
        List<String> result = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(closenessFile));
            String st;
            while ((st = br.readLine()) != null || result.size() < filesName.size()) {
                String name = st.split("=")[0];
                if (filesName.contains(name)) result.add(name);
            }
            br.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

}
