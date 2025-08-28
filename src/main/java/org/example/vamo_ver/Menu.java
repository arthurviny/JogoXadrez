package org.example.vamo_ver;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.net.URL;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    @FXML
    private ImageView logoImage;

    @FXML
    private Button playButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Carrega a logo
        Image logo = new Image(getClass().getResourceAsStream("/images/branco/Rainha.png"));
        logoImage.setImage(logo);

        // Ação do botão Play
        playButton.setOnAction(event -> abrirTabuleiro());
    }

    private void abrirTabuleiro() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vamo_ver/hello-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Xadrez Fantástico");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

