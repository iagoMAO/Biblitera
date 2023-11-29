package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Reservation;
import com.mdolli.biblitera.models.User;
import javafx.fxml.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminPanelUserHistoryController implements Initializable {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane userReservations;

    @FXML
    private Label userName;

    @FXML
    private Label reservationsCount;

    private ArrayList<Reservation> reservations = new ArrayList<Reservation>();
    private AuthenticationController authController;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
        userReservations.getColumnConstraints().add(column);

        for(int i = 0; i < reservations.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("admin-userreservation.fxml"));
                AdminReservationController adminReservationController = new AdminReservationController();
                adminReservationController.setReservation(reservations.get(i));
                adminReservationController.setAuthController(authController);
                adminReservationController.setRootPane(rootPane);
                loader.setController(adminReservationController);
                StackPane item = loader.load();

                userReservations.add(item, 0, rows);

                rows += 1;

                userReservations.setVgap(10);
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

        String procedure = "{ CALL get_user_reservations(?) }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, user.getId());
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            while(result.next()) {
                System.out.println("Resultado");
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
        reservationsCount.setText(String.format((reservations.size() > 1 ? "%d reservas" : (reservations.size() <= 0) ? "Nenhuma reserva" : "%d reserva"), reservations.size()));
        userName.setText(user.getName());
    }
}
