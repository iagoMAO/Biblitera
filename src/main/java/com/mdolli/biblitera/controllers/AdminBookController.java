package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.models.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminBookController implements Initializable {
    @FXML
    private ImageView bookCover;

    @FXML
    private Label bookName;

    @FXML
    private Label bookAuthor;

    private Book book;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookName.setText(book.getName());
        bookAuthor.setText(book.getAuthor());

        InputStream imgStream = null;
        try {
            imgStream = new FileInputStream(new File(String.format("%s/%s/%d.png", System.getProperty("user.home"), "/Biblitera/assets/books/", book.getId())));
            bookCover.setImage(new Image(imgStream));
        } catch (FileNotFoundException e) {
            return;
        }

        bookCover.setFitWidth(80);
        bookCover.setFitHeight(120);

        Rectangle clip = new Rectangle(bookCover.getFitWidth(), bookCover.getFitHeight());
        clip.setArcHeight(20);
        clip.setArcWidth(20);

        bookCover.setClip(clip);
    }
}
