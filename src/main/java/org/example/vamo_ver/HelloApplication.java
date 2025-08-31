package org.example.vamo_ver;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        final double SCENE_WIDTH = 601;
        final double SCENE_HEIGHT = 601;

        // Tela Inicial
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("tela.fxml"));
        Parent menuRoot = fxmlLoader.load();
        Scene menuScene = new Scene(menuRoot, SCENE_WIDTH, SCENE_HEIGHT);

        // Configuração da Janela
        // Configura o tamanho fixo da janela
        stage.setMinWidth(SCENE_WIDTH);
        stage.setMaxWidth(SCENE_WIDTH);
        stage.setMinHeight(SCENE_HEIGHT);
        stage.setMaxHeight(SCENE_HEIGHT);

        stage.setTitle("XXXadrez!");
        stage.setScene(menuScene); 
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}