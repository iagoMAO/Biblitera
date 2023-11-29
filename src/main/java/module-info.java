module com.example.javafxtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.mdolli.biblitera to javafx.fxml;
    exports com.mdolli.biblitera;
    exports com.mdolli.biblitera.controllers;
    opens com.mdolli.biblitera.controllers to javafx.fxml;
}