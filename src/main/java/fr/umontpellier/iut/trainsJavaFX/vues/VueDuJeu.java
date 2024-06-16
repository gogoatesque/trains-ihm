package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import fr.umontpellier.iut.trainsJavaFX.IJeu;
import fr.umontpellier.iut.trainsJavaFX.IJoueur;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private FlowPane boiteReserve;
    @FXML
    private ImageView passer;
    @FXML
    private VueAutresJoueurs vueAutresJoueurs;


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
        boiteReserve = new FlowPane();
        boiteReserve.setAlignment(Pos.CENTER);
    }

    public void creerBindings() {
        joueurCourantProperty.addListener((observableValue, ancienJoueur, nouveauJoueur) -> {
            nomJoueur.setText(nouveauJoueur.getNom());
            nomJoueur.setTextFill(Paint.valueOf(CouleursJoueurs.couleursBackgroundJoueur.get(nouveauJoueur.getCouleur())));
            List<? extends IJoueur> joueurs = new ArrayList<>(jeu.getJoueurs());
            joueurs.remove(nouveauJoueur);
            vueAutresJoueurs.setJoueursSansCourant(joueurs);
            vueAutresJoueurs.creerBindings();
        });

        // Vue Joueur Courant
        joueurCourantProperty.bind(jeu.joueurCourantProperty());
        vueJoueurCourant.joueurCourantProperty().bind(joueurCourantProperty);
        vueJoueurCourant.creerBindings();
        for (IJoueur joueur : jeu.getJoueurs()){
            joueur.mainProperty().addListener(vueJoueurCourant.getChangementMain(joueur));
            joueur.piocheProperty().addListener(vueJoueurCourant.getChangementPioche());
            joueur.defausseProperty().addListener(vueJoueurCourant.getChangementDefausse());
            joueur.cartesEnJeuProperty().addListener(vueJoueurCourant.getChangementJouees());
            joueur.cartesRecuesProperty().addListener(vueJoueurCourant.getChangementRecu());
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

       //Reserve Vbox
        setLeft(boiteReserve);
        boiteReserve.setOrientation(Orientation.VERTICAL);
        for (ICarte carte : jeu.getReserve()) {
            Map<String, IntegerProperty> reserveVal = jeu.getTaillesPilesReserveProperties();
            VueCarte vueCarte = new VueCarte(carte);
            Label nombreCarte = new Label();
            nombreCarte.setStyle("-fx-background-color: white");
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(vueCarte, nombreCarte);
            nombreCarte.setTranslateX(vueCarte.getHeight());
            nombreCarte.setTranslateY(vueCarte.getWidth());
            boiteReserve.getChildren().add(stackPane);
            boiteReserve.setHgap(25);
            boiteReserve.setVgap(2);
            stackPane.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                double rotatedWidth = newBounds.getWidth(); // Ancienne hauteur
                double rotatedHeight = newBounds.getHeight(); // Ancienne largeur

                // Positionner le label en bas à droite après rotation
                nombreCarte.setTranslateX(rotatedHeight / 2 - nombreCarte.getWidth() / 2);
                nombreCarte.setTranslateY(rotatedWidth / 2 - nombreCarte.getHeight() / 2);
            });
            stackPane.minHeightProperty().bind(boiteReserve.heightProperty().divide(7));
            stackPane.maxHeightProperty().bind(boiteReserve.heightProperty().divide(7));
            vueCarte.creerBindings();
            vueCarte.setRotate(-90);
            vueCarte.imageWidthPropety().bind(stackPane.heightProperty());
            vueCarte.setActionCarteChoisie(mouseEvent -> jeu.uneCarteDeLaReserveEstAchetee(((VueCarte) mouseEvent.getSource()).getNomCarte()));
            nombreCarte.textProperty().bind(reserveVal.get(carte.getNom()).asString());
        }

        // bouton passer
        passer.addEventHandler(MouseEvent.MOUSE_CLICKED, actionPasser);
    }

    public IJeu getJeu() {
        return jeu;
    }

    public HBox getMainJoueurCourant(){
        return vueJoueurCourant.getCartesEnMain();
    }
    EventHandler<? super MouseEvent> actionPasser = (mouseEvent -> getJeu().passerAEteChoisi());
}
