module lk.vinuth.malabesparepartssystem {

    // JavaFX modules used by the application.
    requires javafx.controls;
    requires javafx.fxml;

    /*
     * Opens the main package to JavaFX.
     * This allows FXMLLoader to create and connect
     * the controller declared inside the FXML file.
     */
    opens lk.vinuth.malabesparepartssystem to javafx.fxml;

    /*
     * Opens the model package to javafx.base.
     *
     * TableView uses reflection through PropertyValueFactory
     * to call getter methods such as getPartCode().
     */
    opens lk.vinuth.malabesparepartssystem.model to javafx.base;

    // Makes the main package accessible to other modules.
    exports lk.vinuth.malabesparepartssystem;

    // Exports the model package for normal application access.
    exports lk.vinuth.malabesparepartssystem.model;

    // Exports the service package for controller access.
    exports lk.vinuth.malabesparepartssystem.service;

    // Exports the repository package for application access.
    exports lk.vinuth.malabesparepartssystem.repository;

    // Exports the utility package for application access.
    exports lk.vinuth.malabesparepartssystem.util;
}