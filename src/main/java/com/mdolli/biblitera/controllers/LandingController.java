package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.models.User;
import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.Utility;
import com.mdolli.biblitera.database.DBConnection;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

public class LandingController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox rememberMe;
    @FXML
    private Label registerButton;
    @FXML
    private Label forgotPassword;

    private Connection conn;

    public void registerButtonOnMouseClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("register-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        Stage stage = (Stage) registerButton.getScene().getWindow();
        Utility.fadeTransition(rootPane, true, (event -> stage.setScene(scene)));
    }

    public void loginButtonOnAction(ActionEvent e) throws SQLException, ClassNotFoundException {
        // Verificar condições para realizar um login
        if(emailField.getText().isEmpty()) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", "Por favor insira seu e-mail.");
            return;
        }

        if(passwordField.getText().isEmpty()) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", "Por favor insira sua senha.");
            return;
        }

        AuthenticationController authController = new AuthenticationController();
        User user = authController.Login(emailField.getText(), passwordField.getText());
        if(user != null) {
            FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("home-view.fxml"));
            HomeController homeController = new HomeController();
            homeController.setAuthenticationController(authController);
            homeController.setCurrentUser(user);
            pageLoader.setController(homeController);
            authController.loadAuthenticatedPage(this.rootPane, pageLoader);
        }
    }
}