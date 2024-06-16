package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.Joueur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fr.umontpellier.iut.trainsJavaFX.GestionJeu.getJeu;

/**
 * Cette classe présente les éléments des joueurs autres que le joueur courant,
 * en cachant ceux que le joueur courant n'a pas à connaitre.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueAutresJoueurs extends VBox {

    @FXML
    private Label nbJetonGare;
    private List<? extends IJoueur> joueursSansCourant;

    public VueAutresJoueurs() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/autreJoueur.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setJoueursSansCourant(List<? extends IJoueur> joueursSansCourant) {
        this.joueursSansCourant = joueursSansCourant;
    }

    public void creerBindings(){
        //nbJetonGare.textProperty().bind(getNb);
        for (IJoueur joueur : joueursSansCourant) {
            VueJoueur vj = new VueJoueur(joueur);
            getChildren().add(vj);
            vj.creerBindings();
        }
    }
}
