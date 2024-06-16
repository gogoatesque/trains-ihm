package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.TrainsIHM;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class VueResultats extends VBox {

    private TrainsIHM ihm;

    @FXML
    VBox boiteClassement;

    public VueResultats(TrainsIHM ihm) {
        this.ihm = ihm;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/resultats.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (IJoueur joueur : getClassement()){
            HBox boiteJoueur = new HBox();
            Label labelJoueur = new Label(joueur.getNom());
            labelJoueur.setTextFill(Paint.valueOf(CouleursJoueurs.couleursBackgroundJoueur.get(joueur.getCouleur())));
            Label labelScore = new Label(" : " + joueur.getScoreTotal());
            boiteJoueur.getChildren().addAll(labelJoueur,labelScore);
            boiteJoueur.setAlignment(Pos.CENTER);
            boiteClassement.getChildren().add(boiteJoueur);
        }
    }

    public List<IJoueur> getClassement() {
        List<IJoueur> classement = new ArrayList<>(ihm.getJeu().getJoueurs());
        classement.sort((j1, j2) -> j2.getScoreTotal()-j1.getScoreTotal());
        return classement;
    }



}
