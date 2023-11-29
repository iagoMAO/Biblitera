package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Utility;
import com.mdolli.biblitera.database.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.ResourceBundle;

public class AdminPanelCreateController implements Initializable {
    private AuthenticationController authController;

    @FXML
    private Button uploadButton;

    @FXML
    private Button createButton;

    @FXML
    private ImageView bookCover;

    @FXML
    private TextField nameField;

    @FXML
    private TextField isbnField;

    @FXML
    private TextField authorField;

    @FXML
    private TextArea descField;

    @FXML
    private TextField copiesField;

    private File coverFile;

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    public void uploadButtonOnEvent(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        coverFile = fileChooser.showOpenDialog((Stage) createButton.getScene().getWindow());
        if(coverFile != null) {
            Image coverImage = new Image(coverFile.toURI().toString());
            bookCover.setImage(coverImage);
        }
    }

    public void createButtonOnEvent(ActionEvent e) {
        if(nameField.getText().isEmpty()) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", "Por favor insira o nome do livro.");
            return;
        }

        if(isbnField.getText().isEmpty()) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", "Por favor insira o ISBN do livro.");
            return;
        }

        if(authorField.getText().isEmpty()) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", "Por favor insira o autor do livro.");
            return;
        }

        try {
            String procedure = "{ CALL create_book(?, ?, ?, ?, ?, ?) }";
            Connection conn = DBConnection.estabilishConnection();

            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setString(1, nameField.getText());
            stmt.setString(2, isbnField.getText());
            stmt.setString(3, authorField.getText());
            stmt.setString(4, descField.getText());
            stmt.setInt(5, Integer.valueOf(copiesField.getText()));

            stmt.registerOutParameter(6, Types.INTEGER);

            stmt.execute();

            int bookId = stmt.getInt(6);
            Files.copy(coverFile.toPath(), Path.of(String.format("%s/%s/%d.png", System.getProperty("user.home"), "/Biblitera/assets/books/", bookId)));
            createButton.getScene().getWindow().hide();
        } catch(Exception ex) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", ex.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        uploadButton.setOnAction((event -> {
            uploadButtonOnEvent(event);
        }));

        createButton.setOnAction((event -> {
            createButtonOnEvent(event);
        }));

        Rectangle clip = new Rectangle(bookCover.getFitWidth(), bookCover.getFitHeight());
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        bookCover.setClip(clip);
    }
}
