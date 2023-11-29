package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.mdolli.biblitera.models.User;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NavigationController implements Initializable {
    private Node pageRoot;

    @FXML
    private Label navUserName;

    @FXML
    private Label booksLink;

    @FXML
    private Label homeLink;

    @FXML
    private Label acervoLink;

    private AuthenticationController authenticationController;

    public Node getPageRoot() {
        return pageRoot;
    }

    public void setPageRoot(Node pageRoot) {
        this.pageRoot = pageRoot;
    }

    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User user = authenticationController.getCurrentUser();
        navUserName.setText(String.format("olá, %s", user.getName().split(" ")[0]));
        homeLink.setOnMouseClicked(mouseEvent -> {
            homeLinkOnMouseClicked(mouseEvent);
        });
        if(user.getCargo() >= 1) {
            acervoLink.setVisible(true);
            acervoLink.setOnMouseClicked(mouseEvent -> {
                acervoLinkOnMouseClicked(mouseEvent);
            });
        } else {
            acervoLink.setVisible(false);
        }
    }

    public void homeLinkOnMouseClicked(Event e) {
        try {
            FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("home-view.fxml"));
            FXMLLoader navLoader = new FXMLLoader(Application.class.getResource("navbar.fxml"));

            NavigationController navigationController = new NavigationController();
            navigationController.setAuthenticationController(authenticationController);

            HomeController homeController = new HomeController();
            homeController.setAuthenticationController(authenticationController);
            homeController.setCurrentUser(authenticationController.getCurrentUser());

            pageLoader.setController(homeController);
            navLoader.setController(navigationController);

            Parent mainRoot = pageLoader.load();
            Parent navRoot = navLoader.load();

            AnchorPane rootPane = (AnchorPane) mainRoot.lookup("#rootPane");
            rootPane.getChildren().add(navRoot);

            Scene scene = new Scene(mainRoot, 1280, 720);
            Stage stage = (Stage) homeLink.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            throw new RuntimeException  (ex);
        }
    }

    public void acervoLinkOnMouseClicked(Event e) {
        if(authenticationController.getCurrentUser().getCargo() < 1) {
            return;
        }
        try {
            FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("admin-view.fxml"));
            FXMLLoader navLoader = new FXMLLoader(Application.class.getResource("navbar.fxml"));

            NavigationController navigationController = new NavigationController();
            navigationController.setAuthenticationController(authenticationController);

            AdminPanelController adminPanelController = new AdminPanelController();
            adminPanelController.setAuthController(authenticationController);

            pageLoader.setController(adminPanelController);
            navLoader.setController(navigationController);

            Parent mainRoot = pageLoader.load();
            Parent navRoot = navLoader.load();

            AnchorPane rootPane = (AnchorPane) mainRoot.lookup("#rootPane");
            rootPane.getChildren().add(navRoot);

            Scene scene = new Scene(mainRoot, 1280, 720);
            Stage stage = (Stage) homeLink.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
