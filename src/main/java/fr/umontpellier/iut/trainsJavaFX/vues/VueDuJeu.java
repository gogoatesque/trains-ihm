package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.ListeDeCartes;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private VuePlateau plateau;
    private Label instruction;
    private Label nomJoueur;
    private Button passer;
    private ObjectProperty<IJoueur> joueurCourantProperty;
    private VueJoueurCourant joueurCourant;

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
        plateau = new VuePlateau();
        instruction = new Label();
        nomJoueur = new Label();
        passer = new Button("passer");
        joueurCourantProperty = new SimpleObjectProperty<>();
        joueurCourant = new VueJoueurCourant();

        setTop(instruction);
        setCenter(plateau);
        setRight(joueurCourant);
        setLeft(passer);
        setBottom(joueurCourant.getCartesEnMain());
    }
    public void creerBindings() {
        joueurCourantProperty.bind(jeu.joueurCourantProperty());
        joueurCourant.joueurCourantProperty().bind(joueurCourantProperty);
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, actionPasserParDefaut);

        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {
            nomJoueur.setText(nouveauJoueur.getNom());
        });

        joueurCourant.creerBindings();
        for (IJoueur joueur: jeu.getJoueurs()){
            joueur.mainProperty().addListener(joueurCourant.getChangementMain());
        }
        instruction.textProperty().bind(jeu.instructionProperty());
        plateau.prefWidthProperty().bind(getScene().widthProperty());
        plateau.prefHeightProperty().bind(getScene().heightProperty());
        plateau.creerBindings();
    }

    public IJeu getJeu() {
        return jeu;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());




}
