package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Book;
import com.mdolli.biblitera.models.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminPanelReservationsController implements Initializable {
    @FXML
    private GridPane adminReservations;

    @FXML
    private AnchorPane rootPane;

    private AuthenticationController authController;
    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    private void populateReservations() {
        int rows = 0;

        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        adminReservations.getColumnConstraints().add(column);

        for(int i = 0; i < reservations.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("admin-reservation.fxml"));
                AdminReservationController adminReservationController = new AdminReservationController();
                adminReservationController.setReservation(reservations.get(i));
                adminReservationController.setAuthController(authController);
                adminReservationController.setRootPane(rootPane);
                loader.setController(adminReservationController);
                StackPane item = loader.load();

                adminReservations.add(item, 0, rows);

                rows += 1;

                adminReservations.setVgap(20);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void fetchReservations() {
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL get_pending_reservations() }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            while(result.next()) {
                Reservation reservation = new Reservation(result.getInt(1), result.getInt(2), result.getInt(3), result.getDate(4), result.getDate(5), result.getDate(6), result.getInt(7));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchReservations();
        populateReservations();
    }
}
