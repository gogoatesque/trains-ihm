package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.ArrayList;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    ObjectProperty<IJoueur> joueurCourantProperty;
    private HBox cartesEnMain;

    private ListChangeListener<ICarte> changementMain;


    public VueJoueurCourant(){
        joueurCourantProperty = new SimpleObjectProperty<>();
        cartesEnMain = new HBox();
        cartesEnMain.setAlignment(Pos.CENTER);
    }



    public Property<IJoueur> joueurCourantProperty() {
        return joueurCourantProperty;
    }

    public void creerBindings() {
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {
            cartesEnMain.getChildren().clear();
            for (ICarte carte : nouveauJoueur.mainProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                vueCarte.setCarteChoisieListener((mouseEvent -> joueurCourantProperty.get().uneCarteDeLaMainAEteChoisie(((VueCarte) mouseEvent.getSource()).getNomCarte())));
                cartesEnMain.getChildren().add(vueCarte);
                cartesEnMain.prefHeightProperty().bind(getScene().heightProperty().divide(4));
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
}
