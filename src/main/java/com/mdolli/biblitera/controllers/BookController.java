package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.models.Book;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class BookController implements Initializable {
    private Node rootPane;

    @FXML
    private AnchorPane bookPane;

    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookName;

    @FXML
    private Label bookAuthor;

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private Book book;
    private AuthenticationController authController;

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    public void setRootPane(Node rootPane) {
        this.rootPane = rootPane;
    }

    public void bookOnMouseClicked(Event event) {
        FXMLLoader pageLoader = new FXMLLoader(Application.class.getResource("bookpage-view.fxml"));
        BookPageController bookViewController = new BookPageController();
        bookViewController.setBook(book);
        bookViewController.setRootPane(rootPane);
        bookViewController.setAuthController(authController);
        pageLoader.setController(bookViewController);
        authController.loadAuthenticatedPage(this.rootPane, pageLoader);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookName.setText(book.getName());
        bookAuthor.setText(book.getAuthor());

        InputStream imgStream = null;
        try {
            imgStream = new FileInputStream(new File(String.format("%s/%s/%d.png", System.getProperty("user.home"), "/Biblitera/assets/books/", book.getId())));
            bookCover.setImage(new Image(imgStream));
        } catch (FileNotFoundException e) {
            return;
        }

        bookCover.setFitWidth(155);
        bookCover.setFitHeight(250);

        Rectangle clip = new Rectangle(bookCover.getFitWidth(), bookCover.getFitHeight());
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        bookCover.setClip(clip);

        bookPane.setOnMouseClicked(mouseEvent -> {
            bookOnMouseClicked(mouseEvent);
        });
    }
}
