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
public class VueCarte extends ImageView {

    private final ICarte carte;
    private final double ratio;

    public VueCarte(ICarte carte) {
        super();
        Image image = new Image("images/cartes/"+carte.getNom().replace(" ","_").toLowerCase().replace("é","e").replace("è","e")+".jpg");
        ratio = image.getWidth() / image.getHeight();
        this.carte = carte;
        this.setImage(image);
        setPreserveRatio(true);
    }

    public void creerBindings(){
        this.fitHeightProperty().bind(((HBox)this.getParent()).heightProperty());
    }

    public void setCarteChoisieListener(EventHandler<MouseEvent> quandCarteEstChoisie) {
        setOnMouseClicked(quandCarteEstChoisie);
    }

    public String getNomCarte(){
        return carte.getNom();
    }

    public double getWidthImage(){
        return getFitHeight()*ratio;
    }

}
