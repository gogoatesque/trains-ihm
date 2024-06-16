package fr.umontpellier.iut.trainsJavaFX.vues;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class VueEnsembleCartes {
    public VueEnsembleCartes() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ensembleCartes.fxml"));
            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
