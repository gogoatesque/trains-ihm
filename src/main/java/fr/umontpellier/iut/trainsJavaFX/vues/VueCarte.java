package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Cette classe représente la vue d'une carte.
 * <p>
 * On y définit le listener à exécuter lorsque cette carte a été choisie par l'utilisateur
 */
public class VueCarte extends Button {

    private final ICarte carte;

    public VueCarte(ICarte carte) {
        super();
        String url = "images/cartes/"+carte.getNom().replace(" ","_").toLowerCase().replace("é","e").replace("è","e")+".jpg";
        this.carte = carte;
        ImageView image = new ImageView(new Image(url));
        image.setPreserveRatio (true);
        setGraphic(image);
    }

    public void creerBindings(){
        ((ImageView) this.getGraphic()).fitHeightProperty().bind(((HBox)this.getParent()).prefHeightProperty());
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    public String getNomCarte(){
        return carte.getNom();
    }

}
