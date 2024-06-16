package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Popup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Cette classe présente les éléments appartenant au joueur courant.
 * <p>
 * On y définit les bindings sur le joueur courant, ainsi que le listener à exécuter lorsque ce joueur change
 */
public class VueJoueurCourant extends VBox {

    private ObjectProperty<IJoueur> joueurCourantProperty;
    private HBox cartesEnMain;
    @FXML
    private Label labelCarteJouees;
    @FXML
    private StackPane cartesJouees;
    @FXML Label labelCartesRecues;
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
    private ListChangeListener<ICarte> changementPioche;
    private List<ICarte> pioche;
    @FXML
    private ImageView imageDeck;
    @FXML
    private Label labelDeck;
    private ListChangeListener<ICarte> changementDefausse;
    private List<ICarte> defausse;
    @FXML
    private ImageView imageDefausse;
    @FXML
    private Label labelDefausse;

    private Popup popupDeck ;
    private Popup popupDefausse ;
    private Popup popupJouees ;
    private Popup popupRecues ;

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
        changementJouees = change -> {
            while (change.next()){
                if (change.wasAdded()){
                    ICarte carte = change.getAddedSubList().get(0);
                    VueCarte vueCarte = new VueCarte(carte);
                    cartesJouees.getChildren().add(vueCarte);
                    vueCarte.creerBindingsCarteAvecTaille();
                }
                else if (change.wasRemoved()){
                    ICarte carte = change.getRemoved().get(0);
                    cartesJouees.getChildren().removeIf(vueCarte -> ((VueCarte) vueCarte).getCarte().equals(carte));
                }
            }
        };

        changementRecu = change -> {
            while (change.next()){
                if (change.wasAdded()){
                    ICarte carte = change.getAddedSubList().get(0);
                    VueCarte vueCarte = new VueCarte(carte);
                    cartesRecues.getChildren().add(vueCarte);
                    vueCarte.creerBindingsCarteAvecTaille();
                }
            }
        };

        pioche = new ArrayList<>();
        changementPioche = change ->{
            while (change.next()){
            if (change.wasAdded()){
                ICarte carte = change.getAddedSubList().get(0);
                pioche.add(carte);
            }
            else if (change.wasRemoved()){
                for (ICarte carteEnlevee : change.getRemoved()) {
                    enleverCarteDeListe(pioche,carteEnlevee);
                }
            }
        }};

        defausse = new ArrayList<>();
        changementDefausse = change ->{
            while (change.next()){
                if (change.wasAdded()){
                    defausse.addAll(change.getAddedSubList());
                }
                else if (change.wasRemoved()){
                     for (ICarte carteEnlevee : change.getRemoved()) {
                         enleverCarteDeListe(defausse,carteEnlevee);
                     }
                }
            }
        };

        popupDeck = new Popup();
        popupDefausse = new Popup();
        popupJouees = new Popup();
        popupRecues = new Popup();

    }



    public Property<IJoueur> joueurCourantProperty() {
        return joueurCourantProperty;
    }

    public void creerBindings() {
        cartesEnMain.minHeightProperty().bind(getScene().heightProperty().divide(4));
        cartesEnMain.maxHeightProperty().bind(getScene().heightProperty().divide(4));
        imageDeck.setOnMouseClicked(event -> actionCliquePioche());
        pioche.addAll(joueurCourantProperty.get().piocheProperty().get());
        imageDefausse.setOnMouseClicked(event -> actionCliqueDefausse());
        defausse.addAll(joueurCourantProperty.get().defausseProperty().get());
        labelCarteJouees.maxWidthProperty().bind(((HBox) labelCarteJouees.getParent()).widthProperty().divide(6));
        cartesJouees.setOnMouseClicked(event -> actionCliqueJouees());
        labelCartesRecues.maxWidthProperty().bind(((HBox) labelCartesRecues.getParent()).widthProperty().divide(5));
        cartesRecues.setOnMouseClicked(event -> actionCliqueRecues());
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {

            // Main du joueur
            cartesEnMain.getChildren().clear();
            for (ICarte carte : nouveauJoueur.mainProperty()) {
                VueCarte vueCarte = new VueCarte(carte);
                vueCarte.setActionCarteChoisie(mouseCliqued ->
                        nouveauJoueur.uneCarteDeLaMainAEteChoisie(((VueCarte) mouseCliqued.getSource()).getNomCarte()));
                cartesEnMain.getChildren().add(vueCarte);
                vueCarte.creerBindingsCarteAvecTaille();
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
                        if (nbCarte*(tailleCarte*2-1)<cartesEnMain.getWidth()*1.1) {
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
            popupDeck.getContent().clear();

            // défausse
            labelDefausse.textProperty().bind(nouveauJoueur.defausseProperty().sizeProperty().asString());
            popupDefausse.getContent().clear();

            // cartes jouées
            cartesJouees.getChildren().clear();
            popupJouees.getContent().clear();

            // cartes reçues
            cartesRecues.getChildren().clear();
            popupRecues.getContent().clear();

        });

    }

    public void actionCliquePopup(Popup popup, boolean estCarteJouee, List<ICarte> pile, Node parent){
        VueEnsembleCartes ensembleDeck = new VueEnsembleCartes(pile);
        if (estCarteJouee){
            ensembleDeck.setEstCarteJouees();
        }
        popup.getContent().add(ensembleDeck);
        ensembleDeck.creerBindings();
        double x = getScene().getWindow().getX() + getScene().getWidth() / 2;
        double y = getScene().getWindow().getY() + getScene().getHeight() / 2;
        popup.show(parent, x, y);
    }

    private void actionCliquePile(Popup popup, List<ICarte> pile, ImageView imagePile) {
        if (popup.isShowing()){
            popup.hide();
        }
        else {
            actionCliquePopup(popup,false, pile, imagePile);
        }
    }

    public void actionCliquePioche(){
        joueurCourantProperty.get().laPiocheAEteChoisie();
        actionCliquePile(popupDeck, pioche, imageDeck);
    }

    public void actionCliqueDefausse(){
        joueurCourantProperty.get().laDefausseAEteChoisie();
        actionCliquePile(popupDefausse, defausse, imageDefausse);
    }

    private void actionCliqueStack(Popup popup, boolean estCarteJouee, StackPane cartesStack) {
        if (popup.isShowing()){
            popup.hide();
        }
        else {
            List<ICarte> cartes = new ArrayList<>();
            for (Node vueCarteJouees : cartesStack.getChildren()){
                cartes.add(((VueCarte) vueCarteJouees).getCarte());
            }
            actionCliquePopup(popup, estCarteJouee, cartes, cartesStack);
        }
    }

    public void actionCliqueJouees(){
        actionCliqueStack(popupJouees,true, cartesJouees);
    }

    public void actionCliqueRecues(){
        actionCliqueStack(popupRecues,false, cartesRecues);
    }

    public HBox getCartesEnMain() {
        return cartesEnMain;
    }

    public ListChangeListener<ICarte> getChangementMain(IJoueur joueurCourant) {
        return changementMain = change -> {
            while (change.next()){
                if (change.wasRemoved()) {
                    ICarte carteEnlevee = change.getRemoved().get(0);
                    cartesEnMain.getChildren().remove(trouverVueCarteDansMain(carteEnlevee));
                }
                else if(change.wasAdded()){
                    for (ICarte carteAjoutee : change.getAddedSubList()) {
                        VueCarte vueCarte = new VueCarte(carteAjoutee);
                        vueCarte.setActionCarteChoisie(mouseCliqued ->
                                joueurCourant.uneCarteDeLaMainAEteChoisie(((VueCarte) mouseCliqued.getSource()).getNomCarte()));
                        cartesEnMain.getChildren().add(vueCarte);
                        vueCarte.creerBindingsCarteAvecTaille();
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

    public ListChangeListener<ICarte> getChangementPioche() {
        return changementPioche;
    }

    public ListChangeListener<ICarte> getChangementDefausse() {
        return changementDefausse;
    }

    private VueCarte trouverVueCarteDansMain(ICarte carteATrouver){
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

    private void enleverCarteDeListe(List<ICarte> listeCarte, ICarte carte){
        Iterator<ICarte> itListeCarte = listeCarte.iterator();
        boolean carteTrouvee = false;
        while (itListeCarte.hasNext() && !carteTrouvee){
            if (itListeCarte.next().getNom().equals(carte.getNom())){
                itListeCarte.remove();
                carteTrouvee = true;
            }
        }
    }
}
