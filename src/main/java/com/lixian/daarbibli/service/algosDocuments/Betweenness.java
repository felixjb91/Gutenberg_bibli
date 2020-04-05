package com.lixian.daarbibli.service.algosDocuments;

import com.lixian.daarbibli.domain.algoEntities.Parcours;
import com.lixian.daarbibli.domain.algoEntities.Voisins;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Betweenness {

    public List<Voisins> points;
    public List<Parcours> parcours;
    public List<Parcours> parcours_changement;

    public List<Parcours> ppc;
    public List<Integer> nb_ppc;

    public List<Parcours> ppc_passant_par_v;
    public List<Integer> nb_ppc_passant_par_v;

    public List<Double> betweenness;
    public List<Double> betweenness_inverse;

    public Double treShold;

    public Betweenness(Double treShold) {
        this.points = new ArrayList<Voisins>();
        this.parcours = new ArrayList<Parcours>();
        this.parcours_changement = new ArrayList<Parcours>();
        this.ppc = new ArrayList<Parcours>();
        this.nb_ppc = new ArrayList<Integer>();
        this.betweenness = new ArrayList<Double>();
        this.betweenness_inverse = new ArrayList<Double>();

        this.ppc_passant_par_v = new ArrayList<Parcours>();
        this.nb_ppc_passant_par_v = new ArrayList<Integer>();

        this.treShold = treShold;
    }

    public void plusCourtChemin() throws IOException {
        recupererDonnees();

        //i : v
        //j : s
        //k : t
        for (int i = 0; i < points.size(); i++) {

            this.ppc = new ArrayList<Parcours>();
            this.nb_ppc = new ArrayList<Integer>();

            this.ppc_passant_par_v = new ArrayList<Parcours>();
            this.nb_ppc_passant_par_v = new ArrayList<Integer>();

            Double bet = 0.0;

            for (int j = 0; j < points.size(); j++) {
                for (int k = j+1; k < points.size(); k++) {

                    Parcours result_ppc = new Parcours("", 0.0);
                    int result_nb_ppc = 0;

                    Parcours result_ppc_passant_par_v = new Parcours("", 0.0);
                    int result_nb_ppc_passant_par_v = 0;

                    parcours = new ArrayList<Parcours>();
                    String result = points.get(k).getPoint();

                    if (!points.get(i).getPoint().equals(points.get(j).getPoint())
                        && !points.get(i).getPoint().equals(points.get(k).getPoint())
                        && !points.get(j).getPoint().equals(points.get(k).getPoint())) {
                        parcours.add(new Parcours(points.get(j).getPoint(), 0.0));
                    }

                    //Tant qu'il reste des parcours
                    while (parcours.size()>0) {
                        //On boucle sur les parcours
                        for (int b = 0; b < parcours.size(); b++) {
                            //On parcours la liste des relation de voisins
                            for (Voisins v : points) {
                                //Si la relation de voisin est le point du parcours
                                if (v.getPoint().equals(parcours.get(b).getNoeud())) {
                                    //On boucle sur les voisins du points actuel
                                    for (int m = 0; m < v.getVoisins().size(); m++) {
                                        //Si le point n'a pas encore été parcouru par ce parcours
                                        if (!parcours.get(b).hasParcouru(v.getVoisins().get(m))) {
                                            //Si le voisin qu'on test est la destination
                                            if (v.getVoisins().get(m).equals(result)) {

                                                //On s'occupe d'ajouter le PPC passant par V
                                                if (parcours.get(b).containsV(points.get(i).getPoint())) {
                                                    Parcours new_result_v = new Parcours(
                                                            v.getVoisins().get(m),
                                                            parcours.get(b).getParcouru(),
                                                            (parcours.get(b).getCout()+v.getValeurs().get(m))
                                                    );

                                                    //Si le plus court chemin n'a pas été initialisé
                                                    if (result_ppc_passant_par_v.getCout().equals(0.0)) {
                                                        result_ppc_passant_par_v.setCout(new_result_v.getCout());
                                                        result_ppc_passant_par_v.setParcouru(new_result_v.getParcouru());
                                                        result_nb_ppc_passant_par_v = 1;
                                                    } else if (new_result_v.DepasseCout(result_ppc.getCout())) {
                                                        result_ppc_passant_par_v.setCout(new_result_v.getCout());
                                                        result_ppc_passant_par_v.setParcouru(new_result_v.getParcouru());
                                                        result_nb_ppc_passant_par_v = 1;
                                                    }
                                                    //Sinon, si les coûts sont égaux, on incrémente
                                                    else if (result_ppc_passant_par_v.getCout().equals(new_result_v.getCout())) {
                                                        result_nb_ppc_passant_par_v++;
                                                    }
                                                }

                                                //On s'occupe du plus court chemin
                                                Parcours new_result = new Parcours(
                                                        v.getVoisins().get(m),
                                                        parcours.get(b).getParcouru(),
                                                        (parcours.get(b).getCout()+v.getValeurs().get(m))
                                                );

                                                //Si le plus court chemin n'a pas été initialisé
                                                if (result_ppc.getCout().equals(0.0)) {
                                                    result_ppc.setCout(new_result.getCout());
                                                    result_ppc.setParcouru(new_result.getParcouru());
                                                    result_nb_ppc = 1;
                                                } else if (new_result.DepasseCout(result_ppc.getCout())) {
                                                    result_ppc.setCout(new_result.getCout());
                                                    result_ppc.setParcouru(new_result.getParcouru());
                                                    result_nb_ppc = 1;
                                                }
                                                //Sinon, si les coûts sont égaux, on incrémente
                                                else if (result_ppc.getCout().equals(new_result.getCout())) {
                                                    result_nb_ppc++;
                                                }
                                            //Si le voisin qu'on test n'est pas le résultat
                                            } else {
                                                //Si un ppc n'a pas encore été trouvé, ce chemin est encore possible
                                                if (result_ppc.getCout().equals(0.0)) {
                                                    parcours_changement.add(
                                                            new Parcours(
                                                                    v.getVoisins().get(m),
                                                                    parcours.get(b).getParcouru(),
                                                                    (parcours.get(b).getCout()+v.getValeurs().get(m))
                                                            )
                                                    );
                                                }
                                                //Si un ppc a été trouvé, ce chemin est possible que si son coût additionné au prochain point est inférieur au coût du ppc actuel
                                                else if (result_ppc.getCout() > (parcours.get(b).getCout()+v.getValeurs().get(m))){
                                                    parcours_changement.add(
                                                            new Parcours(
                                                                    v.getVoisins().get(m),
                                                                    parcours.get(b).getParcouru(),
                                                                    (parcours.get(b).getCout()+v.getValeurs().get(m))
                                                            )
                                                    );
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            parcours.remove(parcours.get(b));
                            parcours.addAll(parcours_changement);
                            parcours_changement = new ArrayList<Parcours>();
                        }
                    }

                    if (result_ppc.getCout()!=0.0) {
                        ppc.add(result_ppc);
                        nb_ppc.add(result_nb_ppc);

                        int num = 0;
                        int deno = 0;

                        if (result_ppc.containsV(points.get(i).getPoint())) {
                            num = result_nb_ppc_passant_par_v;
                        }
                        deno = result_nb_ppc;

                        if (num != 0 || deno != 0) {
                            bet += (num/deno);
                        }
                    }

                    if (result_ppc_passant_par_v.getCout()!=0.0) {
                        ppc_passant_par_v.add(result_ppc_passant_par_v);
                        nb_ppc_passant_par_v.add(result_nb_ppc_passant_par_v);
                    }
                }
            }

            betweenness.add(bet);
        }
        betweenness_inverse();
    }

    public void betweenness_inverse() throws IOException {

        Double min = -1.0;
        Double max = -1.0;

        for (Double b : betweenness) {
            if (min.equals(-1.0) || min > b) {
                min = b;
            }

            if (max.equals(-1.0) || max < b) {
                max = b;
            }
        }

        for (Double b : betweenness) {
            Double num = b - min;
            Double deno = max - min;

            this.betweenness_inverse.add(num/deno);
        }

        publierValeur();
    }

    public void recupererDonnees() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("jaccardDistances.txt"));
        ArrayList<String> recuperer = new ArrayList<String>();
        String st;
        int i = 0;
        while ((st = br.readLine()) != null) {
            i++;
            String[] decoupe = st.split("=");
            String[] fichiers = decoupe[0].split(",");

            String gauche = fichiers[0].trim();
            String droit = fichiers[1].trim();
            Double valeur = Double.parseDouble(decoupe[1].trim());

            if (recuperer.contains(gauche)) {
                for (Voisins v : points) {
                    if (v.getPoint().equals(gauche) && !v.getVoisins().contains(droit)) {
                        v.add(droit, valeur, treShold);
                    }
                }
            } else {
                Voisins v = new Voisins(gauche);
                v.add(droit, valeur, treShold);
                this.points.add(v);
                recuperer.add(gauche);
            }

            if (recuperer.contains(droit)) {
                for (Voisins v : points) {
                    if (v.getPoint().equals(droit) && !v.getVoisins().contains(gauche)) {
                        v.add(gauche, valeur, treShold);
                    }
                }
            } else {
                Voisins v = new Voisins(droit);
                v.add(gauche, valeur, treShold);
                this.points.add(v);
                recuperer.add(droit);
            }
        }
    }

    public void publierValeur() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("betweenness.txt"));
        for (int i = 0; i < betweenness.size(); i++) {
            writer.write(points.get(i).getPoint()+"="+betweenness_inverse.get(i));
            writer.write('\n');
        }
        writer.close();
    }
}
