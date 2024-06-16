package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.GestionJeu;
import fr.umontpellier.iut.trainsJavaFX.ICarte;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VueEnsembleCartes extends FlowPane {

    private List<ICarte> ensembleCarte;
    private boolean estCarteJouees;

    public VueEnsembleCartes() {
        super();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ensembleCartes.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public VueEnsembleCartes(List<ICarte> listeCarte){
        this();
        ensembleCarte = new ArrayList<>(listeCarte);
    }

    public void creerBindings(){
        for (ICarte carte : ensembleCarte){
            VueCarte vueCarte = new VueCarte(carte);
            if (estCarteJouees) {
                vueCarte.setActionCarteChoisie(event -> GestionJeu.getJeu().joueurCourantProperty().get().uneCarteEnJeuAEteChoisie(carte.getNom()));
            }
            getChildren().add(vueCarte);
            vueCarte.creerBindings();
            vueCarte.imageWidthPropety().bind(widthProperty().divide(4));
            vueCarte.setPadding(new Insets(10,10,10,10));
        }
    }

    public void setEstCarteJouees() {
        estCarteJouees = true;
    }
}
