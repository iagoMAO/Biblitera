package com.mdolli.biblitera.controllers;
import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.models.User;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Book;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.spreadsheet.Grid;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    private AuthenticationController authenticationController;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane displayBooks;

    @FXML
    private Label navUserName;

    @FXML
    private TextField searchField;

    private ArrayList<Book> books = new ArrayList<Book>();

    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public void setAuthenticationController(AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
    }

    public void setCurrentUser(User user) {
        authenticationController.setCurrentUser(user);
    }

    private User getCurrentUser() {
        return authenticationController.getCurrentUser();
    }

    private void fetchBooks(String search) {
        books.clear();
        Connection conn = null;
        try {
            conn = DBConnection.estabilishConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String procedure = "{ CALL get_books(?) }";

        try {
            CallableStatement stmt = conn.prepareCall(procedure);
            stmt.setString(1, search);
            stmt.execute();

            ResultSet result = stmt.getResultSet();
            while(result.next()) {
                Book book = new Book(result.getInt(1), result.getString(2), result.getString(3), result.getInt(4), result.getString(5), result.getString(6));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ChangeListener<String> searchFieldChangeListener() {
        return new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldString, String newString) {
                fetchBooks(newString);
                populateBooks();
            }
        };
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchField.textProperty().addListener(searchFieldChangeListener());
        fetchBooks(searchField.getText());
        populateBooks();
    }

    public void populateBooks() {
        displayBooks.getChildren().clear();
        int columns = 0;
        int rows = 0;

        for(int i = 0; i < books.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("book.fxml"));
                BookController bookController = new BookController();
                bookController.setRootPane(rootPane);
                bookController.setBook(books.get(i));
                bookController.setAuthController(authenticationController);
                loader.setController(bookController);
                AnchorPane item = loader.load();

                if(columns >= 7) {
                    rows += 1;
                    columns = 0;
                }
                displayBooks.add(item, columns++, rows);
                displayBooks.setVgap(20);
                displayBooks.setHgap(20);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
