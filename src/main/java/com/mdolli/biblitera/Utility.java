package com.mdolli.biblitera;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

public class Utility {
    public static void createAlertBox(Alert.AlertType type, String title, String content)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                alert.close();
            }
        });
    }

    public static void fadeTransition(Node node, boolean fadeOut, EventHandler<ActionEvent> callback) {
        FadeTransition transition = new FadeTransition();
        transition.setDuration(Duration.millis(100));
        transition.setNode(node);
        transition.setFromValue(fadeOut ? 1 : 0);
        transition.setToValue(fadeOut ? 0 : 1);
        transition.setOnFinished(callback);
        transition.play();
    }
}
