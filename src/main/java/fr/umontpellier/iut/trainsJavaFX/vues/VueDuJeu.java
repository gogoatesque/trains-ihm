package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;


/**
 * Cette classe correspond à la fenêtre principale de l'application.
 * <p>
 * Elle est initialisée avec une référence sur la partie en cours (Jeu).
 * <p>
 * On y définit les bindings sur les éléments internes qui peuvent changer
 * (le joueur courant, ses cartes en main, son score, ...)
 * ainsi que les listeners à exécuter lorsque ces éléments changent
 */
public class VueDuJeu extends BorderPane {

    private final IJeu jeu;
    @FXML
    private VuePlateau plateau;
    @FXML
    private Label instruction;
    @FXML
    private Label nomJoueur;

    private ObjectProperty<IJoueur> joueurCourantProperty;
    @FXML
    private VueJoueurCourant vueJoueurCourant;
    @FXML
    private HBox boiteCarteEnMain;
    @FXML
    private VBox boiteReserve;

    public VueDuJeu(IJeu jeu) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/jeu.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.jeu = jeu;
        joueurCourantProperty = new SimpleObjectProperty<>();
    }

    public void creerBindings() {
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {
            nomJoueur.setText(nouveauJoueur.getNom());
        });

        // Vue Joueur Courant
        joueurCourantProperty.bind(jeu.joueurCourantProperty());
        vueJoueurCourant.joueurCourantProperty().bind(joueurCourantProperty);
        vueJoueurCourant.creerBindings();
        for (IJoueur joueur: jeu.getJoueurs()){
            joueur.mainProperty().addListener(vueJoueurCourant.getChangementMain());
        }

        // Instructions
        instruction.textProperty().bind(jeu.instructionProperty());

        // Plateau
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();

        // Main
        boiteCarteEnMain = getMainJoueurCourant();
        setBottom(boiteCarteEnMain);
        boiteCarteEnMain.minWidthProperty().bind(getScene().widthProperty());
    }

    public IJeu getJeu() {
        return jeu;
    }

    public HBox getMainJoueurCourant(){
        return vueJoueurCourant.getCartesEnMain();
    }
}
