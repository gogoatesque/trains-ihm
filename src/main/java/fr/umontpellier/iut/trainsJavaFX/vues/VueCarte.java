package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;

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
        Image image = new Image("images/cartes/"+carte.getNom().replace(" ","_").toLowerCase().replace("é","e").replace("è","e").replace("ô","o")+".jpg");
        ratio = image.getWidth() / image.getHeight();
        imageCarte.setImage(image);

        ImageView visualisationCarte = new ImageView(image);
        visualisationCarte.setPreserveRatio(true);
        visualisationCarte.fitHeightProperty().bind(getScene().heightProperty().divide(2.5));
        Popup popupCarte = new Popup();
        popupCarte.getContent().add(visualisationCarte);
        imageCarte.hoverProperty().addListener((observableValue, ancien, nouveau) -> {
            if (nouveau){

                double x = getScene().getWindow().getX() + getScene().getWidth()/2;
                double y = getScene().getWindow().getY() + getScene().getHeight()/3;
                popupCarte.show(imageCarte,x,y);
            }
            else {
                popupCarte.hide();
            }
        });
    }

    public void creerBindingsCarteAvecTaille(){
        creerBindings();
        imageCarte.fitHeightProperty().bind(((Pane)this.getParent()).heightProperty());
    }


    public void setActionCarteChoisie(EventHandler<MouseEvent> quandCarteEstChoisie) {
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

    public DoubleProperty imageWidthPropety(){
        return imageCarte.fitWidthProperty();
    }
}
