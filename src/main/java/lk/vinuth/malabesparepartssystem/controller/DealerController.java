package lk.vinuth.malabesparepartssystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import lk.vinuth.malabesparepartssystem.model.Dealer;
import lk.vinuth.malabesparepartssystem.service.DealerService;

import java.util.List;

public class DealerController {

    @FXML
    private TableView<Dealer> dealerTable;

    @FXML
    private TableColumn<Dealer, String> dealerIdColumn;

    @FXML
    private TableColumn<Dealer, String> dealerNameColumn;

    @FXML
    private TableColumn<Dealer, String> phoneNumberColumn;

    @FXML
    private TableColumn<Dealer, String> emailColumn;

    @FXML
    private TableColumn<Dealer, String> addressColumn;

    @FXML
    private Label totalDealersLabel;

    @FXML
    private Label dealerStatusLabel;

    private DealerService dealerService;

    @FXML
    private void initialize() {

        configureTableColumns();

        totalDealersLabel.setText("0");
        dealerStatusLabel.setText("");
    }

    private void configureTableColumns() {

        dealerIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("dealerId")
        );

        dealerNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("dealerName")
        );

        phoneNumberColumn.setCellValueFactory(
                new PropertyValueFactory<>("phoneNumber")
        );

        emailColumn.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );

        addressColumn.setCellValueFactory(
                new PropertyValueFactory<>("address")
        );
    }
    public void setDealerService(
            DealerService dealerService
    ) {
        this.dealerService = dealerService;

        refreshDealerTable();
    }

    private void refreshDealerTable() {

        if (dealerService == null) {
            return;
        }

        List<Dealer> dealers =
                dealerService.getAllDealers();

        ObservableList<Dealer> tableData =
                FXCollections.observableArrayList(dealers);

        dealerTable.setItems(tableData);

        dealerTable.refresh();

        totalDealersLabel.setText(
                String.valueOf(dealers.size())
        );

        dealerStatusLabel.setText(
                "Dealer records loaded successfully."
        );
    }

    @FXML
    private void onRefreshButtonClick() {

        refreshDealerTable();

        dealerStatusLabel.setText(
                "Dealer table refreshed."
        );
    }
}
