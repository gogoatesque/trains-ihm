package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import fr.umontpellier.iut.trainsJavaFX.mecanique.cartes.Carte;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    private ObjectProperty<IJoueur> joueurCourantProperty;
    private HBox cartesEnMain;
    @FXML
    private StackPane cartesJouees;
    @FXML
    private StackPane cartesRecues;
    private ListChangeListener<ICarte> changementMain;
    private ListChangeListener<ICarte> changementJouees;
    private ListChangeListener<ICarte> changementRecu;
    @FXML
    private Label labelArgent;
    @FXML
    private ImageView imagePointRails;
    @FXML
    private Label labelPointRails;
    @FXML
    private Label labelJetonRails;
    @FXML
    private Label labelScore;
    @FXML
    private Label labelDeck;
    @FXML
    private Label labelDefausse;

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
        cartesJouees = new StackPane();
        changementJouees = change -> {
            while (change.next()){
                if (change.wasAdded()){
                    ICarte carte = change.getAddedSubList().get(0);
                    VueCarte vueCarte = new VueCarte(carte);
                    vueCarte.creerBindings();
                    cartesJouees.getChildren().add(vueCarte);
                }
                else if (change.wasRemoved()){
                    ICarte carte = change.getRemoved().get(0);
                    cartesJouees.getChildren().removeIf(vueCarte -> ((VueCarte) vueCarte).getCarte().equals(carte));
                }
            }
        };

        cartesRecues = new StackPane();
        changementRecu = change -> {
            while (change.next()){
                if (change.wasAdded()){
                    ICarte carte = change.getAddedSubList().get(0);
                    VueCarte vueCarte = new VueCarte(carte);
                    vueCarte.creerBindings();
                    cartesRecues.getChildren().add(vueCarte);
                }
            }
        };
    }



    public Property<IJoueur> joueurCourantProperty() {
        return joueurCourantProperty;
    }

    public void creerBindings() {
        cartesEnMain.minHeightProperty().bind(getScene().heightProperty().divide(4));
        cartesEnMain.maxHeightProperty().bind(getScene().heightProperty().divide(4));
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {

            // Main du joueur
            cartesEnMain.getChildren().clear();
            for (ICarte carte : nouveauJoueur.mainProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                vueCarte.setCarteChoisieListener(mouseCliqued ->
                        nouveauJoueur.uneCarteDeLaMainAEteChoisie(((VueCarte) mouseCliqued.getSource()).getNomCarte()));
                cartesEnMain.getChildren().add(vueCarte);
                vueCarte.creerBindingsCarteEnMain();
            }
            cartesEnMain.spacingProperty().bind(new DoubleBinding() {
                {
                    this.bind(joueurCourantProperty);
                    this.bind(nouveauJoueur.mainProperty().sizeProperty());
                    this.bind(getScene().widthProperty());
                    this.bind(getScene().heightProperty());
                }
                @Override
                protected double computeValue() {
                    int nbCarte = cartesEnMain.getChildren().size();
                    if (nbCarte == 0){
                        return 0;
                    }
                    else {
                        double tailleCarte = ((VueCarte) cartesEnMain.getChildren().get(0)).getWidthImage();
                        if (nbCarte <= 5) {
                            return tailleCarte;
                        } else {
                            return ((getScene().getWidth() - nbCarte * tailleCarte) / (nbCarte - 1)) - 1;
                        }
                    }
                }
            });


            // argent
            labelArgent.textProperty().bind(nouveauJoueur.argentProperty().asString());

            // points rails
            labelPointRails.textProperty().bind(nouveauJoueur.pointsRailsProperty().asString());
            labelPointRails.textProperty().addListener((source, ancien, nouveau) -> {
                if (nouveau.equals("0")){
                    imagePointRails.setImage(new Image("images/boutons/rail.png"));
                }
                else{
                    imagePointRails.setImage(new Image("images/boutons/rails.png"));
                }
            });

            // Jetons rails
            labelJetonRails.textProperty().bind(nouveauJoueur.nbJetonsRailsProperty().asString());

            // points de victoire alias score
            labelScore.textProperty().bind(nouveauJoueur.scoreProperty().asString());

            // deck
            labelDeck.textProperty().bind(nouveauJoueur.piocheProperty().sizeProperty().asString());

            // défausse
            labelDefausse.textProperty().bind(nouveauJoueur.defausseProperty().sizeProperty().asString());

            // cartes jouées
            cartesJouees.getChildren().clear();

            // cartes reçues
            cartesRecues.getChildren().clear();

        });
    }

    public HBox getCartesEnMain() {
        return cartesEnMain;
    }

    public StackPane getcartesJouees() {
        return cartesJouees;
    }

    public StackPane getcartesRecues() {
        return cartesRecues;
    }

    public ListChangeListener<ICarte> getChangementMain(IJoueur joueurCourant) {
        return changementMain = change -> {
            while (change.next()){
                if (change.wasRemoved()) {
                    ICarte carteEnlevee = change.getRemoved().get(0);
                    cartesEnMain.getChildren().remove(trouverBoutonCarte(carteEnlevee));
                }
                else if(change.wasAdded()){
                    for (ICarte carteAjoutee : change.getAddedSubList()) {
                        VueCarte vueCarte = new VueCarte(carteAjoutee);
                        vueCarte.setCarteChoisieListener(mouseCliqued ->
                                joueurCourant.uneCarteDeLaMainAEteChoisie(((VueCarte) mouseCliqued.getSource()).getNomCarte()));
                        cartesEnMain.getChildren().add(vueCarte);
                        vueCarte.creerBindingsCarteEnMain();
                    }
                }
            }
        };
    }

    public ListChangeListener<ICarte> getChangementJouees() {
        return changementJouees;
    }

    public ListChangeListener<ICarte> getChangementRecu() {
        return changementRecu;
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
