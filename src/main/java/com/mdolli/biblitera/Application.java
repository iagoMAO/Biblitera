package com.mdolli.biblitera;

import com.mdolli.biblitera.database.DBConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        String path = System.getProperty("user.home") + File.separator + "Biblitera" + File.separator + "assets" + File.separator + "books";
        File directory = new File(path);
        if(!directory.exists()) {
            directory.mkdirs();
        }
        System.setProperty("prism.lcdtext", "false");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setResizable(false);
        stage.setTitle("Biblitera");
        stage.getIcons().add(new Image(Application.class.getResourceAsStream("images/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}