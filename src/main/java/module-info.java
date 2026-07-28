module lk.vinuth.malabesparepartssystem {

    requires javafx.controls;
    requires javafx.fxml;

    opens lk.vinuth.malabesparepartssystem to javafx.fxml;

    opens lk.vinuth.malabesparepartssystem.controller to javafx.fxml;

    opens lk.vinuth.malabesparepartssystem.model to javafx.base;

    exports lk.vinuth.malabesparepartssystem;

    exports lk.vinuth.malabesparepartssystem.controller;

    exports lk.vinuth.malabesparepartssystem.model;

    exports lk.vinuth.malabesparepartssystem.service;

    exports lk.vinuth.malabesparepartssystem.repository;

    exports lk.vinuth.malabesparepartssystem.util;
}