package com.lixian.daarbibli.service.algosDocuments;

import com.lixian.daarbibli.domain.algoEntities.Ligne;
import com.lixian.daarbibli.domain.algoEntities.ListeMots;
import com.lixian.daarbibli.domain.algoEntities.Mot;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Indexing {
	private File file;
	private ArrayList<Ligne> lignes = new ArrayList<>();
	private ArrayList<Mot> mots = new ArrayList<>();
	private ArrayList<ListeMots> listeMots = new ArrayList<>();
	private Map<String, Integer> indexes = new HashMap<>();

	public Indexing(File file) {
		this.file = file;
		construireIndex();
	}

	public void construireIndex() {
		try {
			lignes.addAll(fileToList());
			for (Ligne l : lignes) mots.addAll(getMots(l));
			buildIndexFile();
            System.out.println(file.getName() + " : done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @return Une liste d'objet ligne contenant la ligne avec son numéro de ligne
	 * @throws IOException
	 */
	public ArrayList<Ligne> fileToList() throws IOException {
        ArrayList<Ligne> result = new ArrayList<Ligne>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        int i = 0;
        while ((st = br.readLine()) != null) {
            st = st.toLowerCase();
            i++;
            if(!st.trim().isEmpty()) {
                result.add(new Ligne(i, st));
            }
        }
        br.close();
        return result;
	}

	/**
	 *
	 * @param ligne La ligne a analyser
	 * @return Une liste de mot avec leur indice de ligne et de mot
	 */
	public ArrayList<Mot> getMots(Ligne ligne) {
		ArrayList<Mot> result = new ArrayList<>();

		String mot = "";
		int indice = 0;

		String lettres = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		for (int i = 0; i < ligne.getLigne().length(); i++) {
			if (lettres.indexOf(ligne.getLigne().charAt(i))!=-1) {
				if (mot.isEmpty()) {
					indice = i;
				}
				mot+=ligne.getLigne().charAt(i);
			} else {
				if (!mot.isEmpty()) {
					result.add(new Mot(ligne.getNumero_ligne(), indice, mot));
					mot = "";
					indice = 0;
				}
			}
		}

		if (!mot.isEmpty()) {
			result.add(new Mot(ligne.getNumero_ligne(), indice, mot));
			mot = "";
			indice = 0;
		}

		return result;
	}

	/**
	 * Construit l'index du fichier
	 * @throws IOException
	 */
	public void buildIndexFile() throws IOException {
		ArrayList<ListeMots> list = new ArrayList<ListeMots>();
		for(int i = 0; i < mots.size(); i++) {
			if(!indexes.containsKey(mots.get(i).getMot())) {
				list.add(new ListeMots(i));
				indexes.put(mots.get(i).getMot(), list.size()-1);
			} else {
				list.get(indexes.get(mots.get(i).getMot())).addId(i);
			}
		}
		listeMots = list;
		trier();
	}


	/**
	 * @throws IOException
	 *
	 */
	public void trier() throws IOException {

		int petit = 0;
		int grand = 0;

		ArrayList<Integer> listePetit = new ArrayList<Integer>();
		ArrayList<Integer> listeGrand = new ArrayList<Integer>();

		ArrayList<ListeMots> lm = new ArrayList<ListeMots>();

		ArrayList<ListeMots> lmGrand = new ArrayList<ListeMots>();

		while (!listeMots.isEmpty()) {

			for (int i = 0; i < listeMots.size(); i++) {

				if (petit == 0 || petit > listeMots.get(i).size()) {
					petit = listeMots.get(i).size();
					listePetit = new ArrayList<Integer>();
					listePetit.add(i);
				} else if (petit == listeMots.get(i).size()) {
					listePetit.add(i);
				}

				if (grand == 0 || grand < listeMots.get(i).size()) {
					grand = listeMots.get(i).size();
					listeGrand = new ArrayList<Integer>();
					listeGrand.add(i);
				} else if (grand == listeMots.get(i).size()) {
					listeGrand.add(i);
				}
			}

			for (int i = 0; i < listePetit.size(); i++) {
				lm.add(listeMots.get(listePetit.get(i)));
			}

			for (int i = 0; i < listeGrand.size(); i++) {
				lmGrand.add(listeMots.get(listeGrand.get(i)));
			}

			listeMots.removeAll(lm);
			listeMots.removeAll(lmGrand);

			petit = 0;
			grand = 0;

			listePetit = new ArrayList<Integer>();
			listeGrand = new ArrayList<Integer>();
		}

		listeMots = lm;
		for (int i = lmGrand.size()-1; i >= 0; i--) {
			if (!lm.contains(lmGrand.get(i))) {
				lm.add(lmGrand.get(i));
			}
		}
		WriteInFile();
	}

	/**
	 * Ecrit dans un fichier l'index
	 * @throws IOException
	 */
	public void WriteInFile() throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/booksResources/indexedFiles/indexing_"+file.getName()));
	    for (int i = 0; i < listeMots.size(); i++) {

	    	for (int j = 0; j < listeMots.get(i).size(); j++) {
	    		if (j==0) {
	    			writer.write(mots.get(listeMots.get(i).get(j)).getMot());
	    			writer.write(" = ");
	    		}

	            writer.write(mots.get(listeMots.get(i).get(j)).getIndice_ligne()+","+mots.get(listeMots.get(i).get(j)).getIndice_mot());
	            if (j != listeMots.get(i).size()-1) {
	            	writer.write("-");
	            }
	    	}

            writer.write('\n');
	    }
	    writer.close();
	}
}
