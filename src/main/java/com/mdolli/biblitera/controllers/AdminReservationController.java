package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Book;
import com.mdolli.biblitera.models.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.action.Action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class AdminReservationController implements Initializable {
    @FXML
    private Button acceptButton;

    @FXML
    private Label bookAuthor;

    @FXML
    private Label bookCopies;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookName;

    @FXML
    private Button denyButton;

    @FXML
    private Button historyButton;

    @FXML
    private Label reservationUser;

    @FXML
    private Button viewBookButton;

    @FXML
    private Label status;

    @FXML
    private Label dateBorrow;

    @FXML
    private Label dateEffectiveDev;

    @FXML
    private Label dateExpectedDev;

    private Reservation reservation;
    private AuthenticationController authController;
    private Node rootPane;

    public Node getRootPane() {
        return rootPane;
    }

    public void setRootPane(Node rootPane) {
        this.rootPane = rootPane;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    private void historyButtonOnAction(ActionEvent e) {
        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("admin-userhistory-view.fxml"));
        AdminPanelUserHistoryController adminPanelUserHistoryController = new AdminPanelUserHistoryController();
        adminPanelUserHistoryController.setUser(reservation.getUsuario());
        adminPanelUserHistoryController.setAuthController(authController);
        pageLoader.setController(adminPanelUserHistoryController);
        authController.loadAuthenticatedPage(rootPane, pageLoader);
    }

    private void viewBookButtonOnAction(ActionEvent e) {
        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("bookpage-view.fxml"));
        BookPageController bookViewController = new BookPageController();
        bookViewController.setBook(reservation.getLivro());
        bookViewController.setAuthController(authController);
        pageLoader.setController(bookViewController);
        authController.loadAuthenticatedPage(rootPane, pageLoader);
    }

    private void denyButtonOnAction(ActionEvent event) {
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL refuse_reservation(?) }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, reservation.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        refreshPage();
    }

    private void acceptButtonOnAction(ActionEvent event) {
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL accept_reservation(?) }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, reservation.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        refreshPage();
    }

    private void refreshPage() {
        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("admin-reservations-view.fxml"));
        AdminPanelReservationsController adminPanelReservationsController = new AdminPanelReservationsController();
        adminPanelReservationsController.setAuthController(authController);
        pageLoader.setController(adminPanelReservationsController);
        authController.loadAuthenticatedPage(this.rootPane, pageLoader);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(status != null) {
            switch(reservation.getStatus()) {
                case 0 :
                    status.setText("Pendente");
                    break;
                case 1:
                    status.setText("Aceito");
                    break;
            }
        }

        if(dateBorrow != null && dateEffectiveDev != null && dateExpectedDev != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            if(reservation.getDataEmprestimo() != null) {
                dateBorrow.setText(formatter.format(reservation.getDataEmprestimo()));
            }
            if(reservation.getDataDevEsperada() != null) {
                dateExpectedDev.setText(formatter.format(reservation.getDataDevEsperada()));
            }
            if(reservation.getDataDevEfetiva() != null) {
                dateEffectiveDev.setText(formatter.format(reservation.getDataDevEfetiva()));
            }
        }

        if(acceptButton != null && denyButton != null) {
            acceptButton.setOnAction((event -> {
                acceptButtonOnAction(event);
            }));
            denyButton.setOnAction((event -> {
                denyButtonOnAction(event);
            }));
        }

        if(reservation.getLivro() != null) {
            bookName.setText(reservation.getLivro().getName());
            bookAuthor.setText(reservation.getLivro().getAuthor());

            if(bookCopies != null)
                bookCopies.setText(String.format((reservation.getLivro().getCopies() > 1 ? "%d unidades restantes" : (reservation.getLivro().getCopies() <= 0 ? "Nenhuma unidade disponível" : "%d unidade restante")), reservation.getLivro().getCopies()));

            InputStream imgStream = null;
            try {
                imgStream = new FileInputStream(new File(String.format("%s/%s/%d.png", System.getProperty("user.home"), "/Biblitera/assets/books/", reservation.getLivro().getId())));
                bookCover.setImage(new Image(imgStream));
            } catch (FileNotFoundException e) {
                return;
            }

            Rectangle clip = new Rectangle(bookCover.getFitWidth(), bookCover.getFitHeight());
            clip.setArcHeight(20);
            clip.setArcWidth(20);

            bookCover.setClip(clip);
            if(viewBookButton != null && historyButton != null) {
                viewBookButton.setOnAction((event -> {
                    viewBookButtonOnAction(event);
                }));
                historyButton.setOnAction((event -> {
                    historyButtonOnAction(event);
                }));
            }
        }
        if(reservation.getUsuario() != null && reservationUser != null) {
            reservationUser.setText(String.format("por %s", reservation.getUsuario().getName()));
        }
    }
}
