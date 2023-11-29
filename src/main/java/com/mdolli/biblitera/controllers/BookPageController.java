package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BookPageController implements Initializable {
    @FXML
    private Label bookName;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookAuthor;

    @FXML
    private Label bookDescription;

    @FXML
    private Label bookCopies;

    @FXML
    private Button borrowButton;

    private Node rootPane;
    private Book book;
    private AuthenticationController authController;

    public Node getRootPane() {
        return rootPane;
    }

    public void setRootPane(Node rootPane) {
        this.rootPane = rootPane;
    }

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

    public void borrowButtonOnAction(ActionEvent e) {
        if(book.getCopies() <= 0) {
            return;
        }

        FXMLLoader popup = new FXMLLoader(Application.class.getResource("reservation-popup.fxml"));
        ReservationPopupController popupController = new ReservationPopupController();
        popupController.setBook(book);
        popupController.setRootPane(this.rootPane);
        popupController.setAuthController(authController);
        popup.setController(popupController);
        try {
            Scene scene = new Scene(popup.load(), 600, 300);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UTILITY);
            stage.setOnHiding((event -> {
                refreshPage(event);
            }));
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void refreshPage(WindowEvent event) {
        System.out.println("FECHANDO");
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL get_book(?) }";
        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setInt(1, book.getId());
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            if(result.next()) {
                Book book = new Book(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), result.getString(5), result.getString(6));
                this.book = book;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("bookpage-view.fxml"));
        BookPageController bookViewController = new BookPageController();
        bookViewController.setBook(book);
        bookViewController.setRootPane(rootPane);
        bookViewController.setAuthController(authController);
        pageLoader.setController(bookViewController);
        authController.loadAuthenticatedPage(bookCopies, pageLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookName.setText(book.getName());
        bookAuthor.setText(book.getAuthor());
        bookDescription.setText(book.getDescription());
        bookCopies.setText(String.format((book.getCopies() > 1 ? "%d unidades disponíveis" : (book.getCopies() == 0 ? "Nenhuma unidade disponível": "%d unidade disponível")), book.getCopies()));

        if(book.getCopies() <= 0) {
            borrowButton.setDisable(true);
            borrowButton.setText("indisponível");
        }

        borrowButton.setOnAction(event -> {
            borrowButtonOnAction(event);
        });

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
