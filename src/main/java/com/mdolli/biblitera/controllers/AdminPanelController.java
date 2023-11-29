package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelController implements Initializable {
    @FXML
    private StackPane booksButton;

    @FXML
    private StackPane reservationsButton;

    @FXML
    private AnchorPane rootPane;

    private AuthenticationController authController;

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    public void booksButtonOnAction(MouseEvent e) {
        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("admin-books-view.fxml"));
        AdminPanelBooksController adminPanelBooksController = new AdminPanelBooksController();
        adminPanelBooksController.setAuthController(authController);
        pageLoader.setController(adminPanelBooksController);
        authController.loadAuthenticatedPage(this.rootPane, pageLoader);
    }

    public void reservationsButtonOnAction(MouseEvent e) {
        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("admin-reservations-view.fxml"));
        AdminPanelReservationsController adminPanelReservationsController = new AdminPanelReservationsController();
        adminPanelReservationsController.setAuthController(authController);
        pageLoader.setController(adminPanelReservationsController);
        authController.loadAuthenticatedPage(this.rootPane, pageLoader);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        booksButton.setOnMouseClicked((event -> {
            booksButtonOnAction(event);
        }));
        reservationsButton.setOnMouseClicked((event -> {
            reservationsButtonOnAction(event);
        }));
    }
}
