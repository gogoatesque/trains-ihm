package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends HBox {

    ObjectProperty<IJoueur> joueurCourantProperty;
    private HBox cartesEnMain;

    private ListChangeListener<ICarte> changementMain;


    public VueJoueurCourant(){
        joueurCourantProperty = new SimpleObjectProperty<>();
        cartesEnMain = new HBox();
        cartesEnMain.setMaxSize(30,30);
    }



    public Property<IJoueur> joueurCourantProperty() {
        return joueurCourantProperty;
    }

    public void creerBindings() {
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {
            cartesEnMain.getChildren().clear();
            for (ICarte carte : nouveauJoueur.mainProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                vueCarte.setCarteChoisieListener((mouseEvent -> joueurCourantProperty.get().uneCarteDeLaMainAEteChoisie(((Button) mouseEvent.getSource()).getText())));
                cartesEnMain.getChildren().add(vueCarte);
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

    private Button trouverBoutonCarte(ICarte carteATrouver){
        int index = 0;
        boolean carteTrouvee = false;
        Button carteCherchee = null;
        while (!carteTrouvee && index < cartesEnMain.getChildren().size()){
            Button bouton = (Button) cartesEnMain.getChildren().get(index);
            if (bouton.getText().equals(carteATrouver.getNom())){
                carteTrouvee = true;
                carteCherchee = bouton;
            }
            index++;
        }
        return carteCherchee;
    }
}
