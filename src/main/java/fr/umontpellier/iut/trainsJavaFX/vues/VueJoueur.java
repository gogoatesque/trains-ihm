package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;

/*
* Vue pour un joueur qui n'est pas le joueur courant*/
public class VueJoueur extends HBox {
    @FXML
    private Label nomJoueur;
    @FXML
    private Label labelJetonRails;
    @FXML
    private Label labelScore;
    @FXML
    private Label labelDeck;
    @FXML
    private Label labelDefausse;
    private IJoueur joueur;

    public VueJoueur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/joueur.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VueJoueur(IJoueur joueur) {
        this();
        this.joueur = joueur;
    }

    public void creerBindings(){
        nomJoueur.setText(joueur.getNom());
        labelJetonRails.setText(String.valueOf(joueur.nbJetonsRailsProperty().get()));
        labelScore.textProperty().bind(joueur.scoreProperty().asString());
        labelDeck.setText(String.valueOf(joueur.piocheProperty().get().size()));
        labelDefausse.setText(String.valueOf(joueur.defausseProperty().get().size()));

    }
}
