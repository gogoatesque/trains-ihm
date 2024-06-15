package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends Pane {

    private ICarte carte;
    private double ratio;
    @FXML
    private ImageView imageCarte;

    private EventHandler<MouseEvent> cliqueHandler;

    public VueCarte() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/carte.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VueCarte(ICarte carte){
        this();
        this.carte = carte;
    }

    public void creerBindings(){
        Image image = new Image("images/cartes/"+carte.getNom().replace(" ","_").toLowerCase().replace("é","e").replace("è","e")+".jpg");
        ratio = image.getWidth() / image.getHeight();
        imageCarte.setImage(image);
        imageCarte.fitHeightProperty().bind(((Pane)this.getParent()).heightProperty());
        imageCarte.hoverProperty().addListener((observableValue, ancien, nouveau) -> {
            // code pour rendre la carte plus grande
        });
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    public String getNomCarte(){
        return carte.getNom();
    }

    public ICarte getCarte() {
        return carte;
    }

    public double getWidthImage(){
        return imageCarte.getFitHeight()*ratio;
    }

}
