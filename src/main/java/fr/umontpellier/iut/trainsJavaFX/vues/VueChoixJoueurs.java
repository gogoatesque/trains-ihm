package fr.umontpellier.iut.trainsJavaFX.vues;

import fr.umontpellier.iut.trainsJavaFX.mecanique.plateau.Plateau;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Cette classe correspond à une nouvelle fenêtre permettant de choisir le nombre et les noms des joueurs de la partie.
 * <p>
 * Sa présentation graphique peut automatiquement être actualisée chaque fois que le nombre de joueurs change.
 * Lorsque l'utilisateur a fini de saisir les noms de joueurs, il demandera à démarrer la partie.
 */
public class VueChoixJoueurs extends Stage {

    private final ObservableList<String> nomsJoueurs;
    private Plateau plateauChoisi;
    @FXML
    Spinner<Integer> spinner;
    @FXML
    private ComboBox<String> plateau;
    @FXML
    private Button jouer;
    @FXML
    private Button quitter;
    @FXML
    private VBox joueurVBox;
    private VBox root;
    private final ObservableList<TextField> joueurTextFields = FXCollections.observableArrayList();
    private StringProperty nomPlateau;

    public VueChoixJoueurs() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/choixJoueurs.fxml"));
            loader.setController(this);
            root = loader.load();
            setScene(new Scene(root));
            setTitle("Choix Joueurs");
        } catch (IOException e) {
            e.printStackTrace();
        }
        nomsJoueurs = FXCollections.observableArrayList();
        nomPlateau = new SimpleStringProperty();
        creerBindings();
    }

    @FXML
    public void initialize() {
        plateau.getItems().addAll("Tokyo", "Osaka");
        plateau.setValue("Osaka");
        updateJoueursTextFields(2);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            int nbJoueurs = newValue;
            updateJoueursTextFields(nbJoueurs);
        });
    }

    private void updateJoueursTextFields(int nbJoueurs) {
        joueurVBox.getChildren().clear();
        joueurTextFields.clear();

        for (int i = 1; i <= nbJoueurs; i++) {
            TextField textField = new TextField();
            textField.setPromptText("Joueur " + i);
            joueurVBox.getChildren().add(textField);
            joueurTextFields.add(textField);
        }
    }

    public void creerBindings(){
        quitter.setOnAction(actionEvent -> Platform.exit());
        jouer.setOnAction(actionEvent -> {
            for (TextField tf : joueurTextFields) {
                nomsJoueurs.add(tf.getText());
            }
        });
        nomPlateau.bind(plateau.valueProperty());
    }

    public List<String> getNomsJoueurs() {
        return nomsJoueurs;
    }

    /**
     * Définit l'action à exécuter lorsque la liste des participants est correctement initialisée
     */
    public void setNomsDesJoueursDefinisListener(ListChangeListener<String> quandLesNomsDesJoueursSontDefinis) {
        nomsJoueurs.addListener(quandLesNomsDesJoueursSontDefinis);
    }


    /**
     * Vérifie que tous les noms des participants sont renseignés
     * et affecte la liste définitive des participants
     */
    protected void setListeDesNomsDeJoueurs() {
        ArrayList<String> tempNamesList = new ArrayList<>();
        for (int i = 1; i <= getNombreDeJoueurs() ; i++) {
            String name = getJoueurParNumero(i);
            if (name == null || name.equals("")) {
                tempNamesList.clear();
                break;
            }
            else
                tempNamesList.add(name);
        }
        if (!tempNamesList.isEmpty()) {
            hide();
            nomsJoueurs.clear();
            nomsJoueurs.addAll(tempNamesList);
        }
    }

    /**
     * Retourne le nombre de participants à la partie que l'utilisateur a renseigné
     */
    protected int getNombreDeJoueurs() {
        return spinner.getValue();
    }

    /**
     * Retourne le nom que l'utilisateur a renseigné pour le ième participant à la partie
     * @param playerNumber : le numéro du participant
     */
    protected String getJoueurParNumero(int playerNumber) {
        return joueurTextFields.get(playerNumber - 1).getText();
    }

    public Plateau getPlateau() {
        return Plateau.valueOf(nomPlateau.get().toUpperCase());
    }
}
