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

/**
 * Controls the dealer-management window.
 *
 * This controller loads dealer records from DealerService
 * and displays them inside a JavaFX table.
 */
public class DealerController {

    // Main table used to display dealer records.
    @FXML
    private TableView<Dealer> dealerTable;

    // Dealer ID column.
    @FXML
    private TableColumn<Dealer, String> dealerIdColumn;

    // Dealer or company name column.
    @FXML
    private TableColumn<Dealer, String> dealerNameColumn;

    // Contact phone-number column.
    @FXML
    private TableColumn<Dealer, String> phoneNumberColumn;

    // Email-address column.
    @FXML
    private TableColumn<Dealer, String> emailColumn;

    // Dealer address or location column.
    @FXML
    private TableColumn<Dealer, String> addressColumn;

    // Shows the total number of loaded dealers.
    @FXML
    private Label totalDealersLabel;

    // Shows messages to the user.
    @FXML
    private Label dealerStatusLabel;

    // Service used for all dealer operations.
    private DealerService dealerService;

    /**
     * Runs automatically when the FXML file is loaded.
     */
    @FXML
    private void initialize() {

        configureTableColumns();

        totalDealersLabel.setText("0");
        dealerStatusLabel.setText("");
    }

    /**
     * Connects each JavaFX table column to the
     * matching getter method in the Dealer class.
     */
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
    /**
     * Supplies the dealer service used by this window.
     *
     * @param dealerService service containing dealer data
     */
    public void setDealerService(
            DealerService dealerService
    ) {
        this.dealerService = dealerService;

        refreshDealerTable();
    }

    /**
     * Loads all dealer records into the table.
     */
    private void refreshDealerTable() {

        // Stop if the service has not been supplied yet.
        if (dealerService == null) {
            return;
        }

        // Retrieve all dealer records from the service.
        List<Dealer> dealers =
                dealerService.getAllDealers();

        // Convert the normal list into a JavaFX ObservableList.
        ObservableList<Dealer> tableData =
                FXCollections.observableArrayList(dealers);

        // Display the records in the table.
        dealerTable.setItems(tableData);

        // Force JavaFX to redraw the table.
        dealerTable.refresh();

        // Update the total number of dealers.
        totalDealersLabel.setText(
                String.valueOf(dealers.size())
        );

        dealerStatusLabel.setText(
                "Dealer records loaded successfully."
        );
    }

    /**
     * Reloads all dealer records.
     */
    @FXML
    private void onRefreshButtonClick() {

        refreshDealerTable();

        dealerStatusLabel.setText(
                "Dealer table refreshed."
        );
    }
}
