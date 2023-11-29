package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.Utility;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ResourceBundle;

public class ReservationPopupController implements Initializable {
    private Book book;
    private Node rootPane;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookName;

    @FXML
    private Label bookAuthor;

    @FXML
    private Button confirmButton;

    @FXML
    private Button dismissButton;

    private AuthenticationController authController;

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Node getRootPane() {
        return rootPane;
    }

    public void setRootPane(Node rootPane) {
        this.rootPane = rootPane;
    }

    public void confirmReservation(ActionEvent e) {
        int userId = authController.getCurrentUser().getId();
        int bookId = book.getId();

        try {
            String procedure = "{ CALL create_reservation(?, ?) }";
            Connection conn = DBConnection.estabilishConnection();

            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);

            stmt.executeQuery();
            bookCover.getScene().getWindow().hide();
        } catch(Exception ex) {
            Utility.createAlertBox(Alert.AlertType.ERROR, "Erro!", ex.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookAuthor.setText(book.getAuthor());
        bookName.setText(book.getName());

        confirmButton.setOnAction((event -> {
            confirmReservation(event);
        }));
        dismissButton.setOnAction((event -> bookCover.getScene().getWindow().hide()));

        InputStream imgStream = null;
        try {
            imgStream = new FileInputStream(new File(String.format("%s/%s/%d.png", System.getProperty("user.home"), "/Biblitera/assets/books/", book.getId())));
            bookCover.setImage(new Image(imgStream));
        } catch (FileNotFoundException e) {
            return;
        }

        Rectangle clip = new Rectangle(bookCover.getFitWidth(), bookCover.getFitHeight());
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        bookCover.setClip(clip);
    }
}