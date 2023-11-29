package com.mdolli.biblitera.controllers;

import com.mdolli.biblitera.Application;
import com.mdolli.biblitera.database.DBConnection;
import com.mdolli.biblitera.models.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminPanelBooksController implements Initializable {
    @FXML
    private GridPane adminBooks;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button createButton;

    private ArrayList<Book> books = new ArrayList<Book>();
    private AuthenticationController authController;

    public AuthenticationController getAuthController() {
        return authController;
    }

    public void setAuthController(AuthenticationController authController) {
        this.authController = authController;
    }

    public void createButtonOnAction(ActionEvent e) {
        FXMLLoader popup = new FXMLLoader(Application.class.getResource("admin-create-popup.fxml"));
        AdminPanelCreateController popupController = new AdminPanelCreateController();
        popupController.setAuthController(authController);
        popup.setController(popupController);
        try {
            Scene scene = new Scene(popup.load(), 800, 650);
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetchBooks();
        populateBooks();
        createButton.setOnAction((event -> {
            createButtonOnAction(event);
        }));
    }

    public void populateBooks() {
        int rows = 0;

        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        adminBooks.getColumnConstraints().add(column);

        for(int i = 0; i < books.size(); i++) {
            try {
                FXMLLoader loader = new FXMLLoader(Application.class.getResource("admin-book.fxml"));
                AdminBookController bookController = new AdminBookController();
                bookController.setBook(books.get(i));
                bookController.setAuthController(authController);
                loader.setController(bookController);
                StackPane item = loader.load();

                adminBooks.add(item, 0, rows);

                rows += 1;

                adminBooks.setVgap(20);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void fetchBooks() {
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
            stmt.setString(1, "");
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
}
