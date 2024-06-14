package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.ArrayList;

import static fr.umontpellier.iut.trainsJavaFX.GestionJeu.getJeu;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    ObjectProperty<IJoueur> joueurCourantProperty;
    private HBox cartesEnMain;
    @FXML
    private Button passer;
    private ListChangeListener<ICarte> changementMain;
    @FXML
    private Label pointRails;
    @FXML
    private Label argent;
    @FXML
    private Label jetonRails;
    @FXML
    private Label score;

    public VueJoueurCourant(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/joueurCourant.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        joueurCourantProperty = new SimpleObjectProperty<>();
        cartesEnMain = new HBox();
        cartesEnMain.setAlignment(Pos.CENTER);
    }



    public Property<IJoueur> joueurCourantProperty() {
        return joueurCourantProperty;
    }

    public void creerBindings() {
        // Bouton passer
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, actionPasserParDefaut);
        cartesEnMain.spacingProperty().bind(new DoubleBinding() {
            {
                this.bind(joueurCourantProperty);
                this.bind(joueurCourantProperty.get().mainProperty().sizeProperty()); // cette ligne de binding ne fonctionne pas malgré de nombreux essais avec différentes façons d'obtenir le nombre de cartes en main
                this.bind(getScene().widthProperty());
                this.bind(getScene().heightProperty());
            }
            @Override
            protected double computeValue() {
                System.out.println(joueurCourantProperty.get().mainProperty().sizeProperty().get());
                int nbCarte = cartesEnMain.getChildren().size();
                if (nbCarte <= 1) {
                    return 10;
                } else {
                    double tailleCarte = ((VueCarte) cartesEnMain.getChildren().get(0)).getWidthImage();
                    return ((getScene().getWidth() - nbCarte * tailleCarte) / (nbCarte-1)) - 2;
                }
            }
        });
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {
            cartesEnMain.getChildren().clear();
            for (ICarte carte : nouveauJoueur.mainProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                vueCarte.setCarteChoisieListener((mouseEvent -> joueurCourantProperty.get().uneCarteDeLaMainAEteChoisie(((VueCarte) mouseEvent.getSource()).getNomCarte())));
                cartesEnMain.getChildren().add(vueCarte);
                cartesEnMain.minHeightProperty().bind(getScene().heightProperty().divide(4));
                cartesEnMain.maxHeightProperty().bind(getScene().heightProperty().divide(4));
                vueCarte.creerBindings();
            }
        });

        changementMain = change -> {
            change.next();
            if (change.wasRemoved()){
                ICarte carteEnlevee = change.getRemoved().get(0);
                cartesEnMain.getChildren().remove(trouverBoutonCarte(carteEnlevee));
            }
        };
    }

    public HBox getCartesEnMain() {
        return cartesEnMain;
    }

    public ListChangeListener<ICarte> getChangementMain() {
        return changementMain;
    }

    private VueCarte trouverBoutonCarte(ICarte carteATrouver){
        int index = 0;
        boolean carteTrouvee = false;
        VueCarte carteCherchee = null;
        while (!carteTrouvee && index < cartesEnMain.getChildren().size()){
            VueCarte boutonCarte = (VueCarte) cartesEnMain.getChildren().get(index);
            if (boutonCarte.getNomCarte().equals(carteATrouver.getNom())){
                carteTrouvee = true;
                carteCherchee = boutonCarte;
            }
            index++;
        }
        return carteCherchee;
    }

    EventHandler<? super MouseEvent> actionPasserParDefaut = (mouseEvent -> getJeu().passerAEteChoisi());

}
