package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.Utility;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.mdolli.biblitera.models.User;

import java.io.IOException;
import java.sql.*;

public class AuthenticationController {
    private User currentUser;

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User Register(String email, String password, String name, String cpf) {
        User user = null;
        try {
            String procedure = "{ CALL create_user(?, ?, ?, ?) }";
            Connection conn = DBConnection.estabilishConnection();

            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, cpf
                    .replace(".", "")
                    .replace("-", "")
            );

            stmt.executeQuery();

            user = Login(email, password);
        } catch(Exception ex) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", ex.getMessage());
        }
        this.currentUser = user;
        return user;
    }

    public User Login(String email, String password) throws SQLException, ClassNotFoundException {
        Connection conn = DBConnection.estabilishConnection();
        User user = null;

        try {
            String procedure = "{ CALL login_user(?, ?, ?, ?, ?, ?, ?) }";

            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setString(1, email);
            stmt.setString(2, password);

            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.registerOutParameter(5, Types.VARCHAR);
            stmt.registerOutParameter(6, Types.VARCHAR);
            stmt.registerOutParameter(7, Types.TINYINT);

            stmt.execute();

            int userId = stmt.getInt(3);
            String userEmail = stmt.getString(4);
            String userName = stmt.getString(5);
            String userCpf = stmt.getString(6);
            int userCargo = stmt.getInt(7);

            user = (userId != 0 ? new User(userId, userName, userEmail, userCpf, userCargo) : null);
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        this.currentUser = user;
        return user;
    }

    public void loadAuthenticatedPage(Node rootPane, FXMLLoader pageLoader) {
        try {
            FXMLLoader navLoader = new FXMLLoader(Application.class.getResource("navbar.fxml"));

            NavigationController navigationController = new NavigationController();
            navigationController.setAuthenticationController(this);

            Parent mainRoot = pageLoader.load();
            navLoader.setController(navigationController);
            Parent navRoot = navLoader.load();

            AnchorPane pageRootPane = (AnchorPane) mainRoot.lookup("#rootPane");
            pageRootPane.getChildren().add(navRoot);

            Scene scene = new Scene(mainRoot, 1280, 720);
            Stage stage = (Stage) rootPane.getScene().getWindow();

            Utility.fadeTransition(rootPane, true, (event -> stage.setScene(scene)));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
