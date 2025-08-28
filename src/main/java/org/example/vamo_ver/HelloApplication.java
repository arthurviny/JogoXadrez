package org.example.vamo_ver;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        final double SCENE_WIDTH = 601;
        final double SCENE_HEIGHT = 601;

        // ---------- TELA INICIAL ----------
        VBox menuLayout = new VBox(20);
        menuLayout.setStyle("-fx-alignment: center; -fx-background-color: linear-gradient(to bottom, #1e1e1e, #3a3a3a);");
        
        Button playButton = new Button("▶ JOGAR");
        playButton.setStyle("-fx-font-size: 20px; -fx-padding: 10px 20px; -fx-background-color: #769656; -fx-text-fill: white; -fx-background-radius: 10;");

        menuLayout.getChildren().add(playButton);

        Scene menuScene = new Scene(menuLayout, SCENE_WIDTH, SCENE_HEIGHT);

        // ---------- TELA DO JOGO (FXML) ----------
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene jogoScene = new Scene(fxmlLoader.load(), SCENE_WIDTH, SCENE_HEIGHT);

        // ação do botão Play -> abre o tabuleiro
        playButton.setOnAction(e -> stage.setScene(jogoScene));

        // ---------- CONFIG DA JANELA ----------
        stage.setMinWidth(SCENE_WIDTH);
        stage.setMaxWidth(SCENE_WIDTH);
        stage.setMinHeight(SCENE_HEIGHT);
        stage.setMaxHeight(SCENE_HEIGHT);

        stage.setTitle("XXXadrez!");
        stage.setScene(menuScene); // começa no menu
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
