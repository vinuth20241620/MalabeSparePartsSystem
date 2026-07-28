package lk.vinuth.malabesparepartssystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.service.ApplicationDataService;
import lk.vinuth.malabesparepartssystem.service.InventoryService;
import lk.vinuth.malabesparepartssystem.controller.PartFormController;

import java.time.LocalDate;
import java.util.List;
import java.io.IOException;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Controls the main inventory JavaFX screen.
 *
 * This class connects the controls in hello-view.fxml
 * with the inventory data and application services.
 */
public class HelloController {

    // Search text entered by the user.
    @FXML
    private TextField searchField;

    // Table used to display every spare part.
    @FXML
    private TableView<SparePart> inventoryTable;

    // Individual columns inside the inventory table.
    @FXML
    private TableColumn<SparePart, String> partCodeColumn;

    @FXML
    private TableColumn<SparePart, String> partNameColumn;

    @FXML
    private TableColumn<SparePart, String> brandColumn;

    @FXML
    private TableColumn<SparePart, Double> priceColumn;

    @FXML
    private TableColumn<SparePart, Integer> quantityColumn;

    @FXML
    private TableColumn<SparePart, String> categoryColumn;

    @FXML
    private TableColumn<SparePart, LocalDate> dateAddedColumn;

    // Labels displaying inventory totals and user feedback.
    @FXML
    private Label totalPartsLabel;

    @FXML
    private Label totalValueLabel;

    @FXML
    private Label statusLabel;

    // Central service used to load the legacy files.
    private ApplicationDataService applicationDataService;

    // Service used to access inventory operations.
    private InventoryService inventoryService;

    /**
     * Runs automatically after the FXML file has loaded.
     */
    @FXML
    private void initialize() {

        // Create the central application data service.
        applicationDataService = new ApplicationDataService();

        // Load inventory and dealer records from the text files.
        applicationDataService.loadLegacyData();

        // Get the inventory service containing the loaded parts.
        inventoryService = applicationDataService.getInventoryService();

        // Connect each table column to a SparePart property.
        configureTableColumns();

        // Display the loaded inventory records.
        refreshInventoryTable();

        // Show confirmation to the user.
        statusLabel.setText("Legacy inventory loaded successfully.");
    }
    /**
     * Connects each table column to the matching
     * getter method inside the SparePart class.
     */
    private void configureTableColumns() {

        partCodeColumn.setCellValueFactory(
                new PropertyValueFactory<>("partCode")
        );

        partNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("partName")
        );

        brandColumn.setCellValueFactory(
                new PropertyValueFactory<>("brand")
        );

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category")
        );

        dateAddedColumn.setCellValueFactory(
                new PropertyValueFactory<>("dateAdded")
        );
    }

    /**
     * Reloads all inventory records into the table
     * and refreshes the summary labels.
     */
    private void refreshInventoryTable() {

        // Retrieve all spare parts from the service.
        List<SparePart> spareParts =
                inventoryService.getAllSpareParts();

        // Convert the ordinary List into a JavaFX ObservableList.
        ObservableList<SparePart> tableData =
                FXCollections.observableArrayList(spareParts);

        // Display the records inside the table.
        inventoryTable.setItems(tableData);
        // Force JavaFX to redraw cells after an existing object is updated.
        inventoryTable.refresh();

        // Update the total number of part records.
        totalPartsLabel.setText(
                String.valueOf(inventoryService.getTotalParts())
        );

        // Update the complete inventory monetary value.
        totalValueLabel.setText(
                String.format(
                        "Rs. %.2f",
                        inventoryService.calculateTotalInventoryValue()
                )
        );
    }

    /**
     * Searches the inventory using the text entered
     * in the search field.
     *
     * The search manually checks the part code,
     * part name, brand and category.
     */
    @FXML
    private void onSearchButtonClick() {

        // Read and clean the user's search text.
        String keyword = searchField.getText().trim();

        // Display all records when no keyword was entered.
        if (keyword.isEmpty()) {
            refreshInventoryTable();
            statusLabel.setText(
                    "Enter a code, name, brand or category to search."
            );
            return;
        }

        // Convert the keyword to lowercase for case-insensitive searching.
        String lowerKeyword = keyword.toLowerCase();

        // Create an empty list for matching results.
        ObservableList<SparePart> matchingParts =
                FXCollections.observableArrayList();

        // Check every spare part manually.
        for (SparePart sparePart
                : inventoryService.getAllSpareParts()) {

            boolean codeMatches =
                    containsIgnoreCase(
                            sparePart.getPartCode(),
                            lowerKeyword
                    );

            boolean nameMatches =
                    containsIgnoreCase(
                            sparePart.getPartName(),
                            lowerKeyword
                    );

            boolean brandMatches =
                    containsIgnoreCase(
                            sparePart.getBrand(),
                            lowerKeyword
                    );

            boolean categoryMatches =
                    containsIgnoreCase(
                            sparePart.getCategory(),
                            lowerKeyword
                    );

            // Add the part when at least one field matches.
            if (codeMatches
                    || nameMatches
                    || brandMatches
                    || categoryMatches) {

                matchingParts.add(sparePart);
            }
        }

        // Display only the matching records.
        inventoryTable.setItems(matchingParts);

        // Provide clear feedback to the user.
        statusLabel.setText(
                matchingParts.size()
                        + " matching inventory record(s) found."
        );
    }

    /**
     * Safely checks whether text contains a keyword,
     * without considering uppercase and lowercase letters.
     *
     * @param text original field value
     * @param lowerKeyword lowercase search keyword
     * @return true if the field contains the keyword
     */
    private boolean containsIgnoreCase(
            String text,
            String lowerKeyword
    ) {

        // A missing optional field cannot match.
        if (text == null) {
            return false;
        }

        return text.toLowerCase().contains(lowerKeyword);
    }

    /**
     * Clears the search box and displays all records again.
     */
    @FXML
    private void onClearSearchButtonClick() {

        searchField.clear();
        refreshInventoryTable();

        statusLabel.setText(
                "Search cleared. All inventory records are displayed."
        );
    }

    /**
     * Opens the Add Part form in a separate window.
     */
    @FXML
    private void onAddPartButtonClick() {

        try {
            /*
             * Load the Add Part form from the FXML file.
             */
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "part-form.fxml"
                    )
            );

            Parent formRoot = loader.load();

            /*
             * Get the controller created for part-form.fxml.
             */
            PartFormController formController =
                    loader.getController();

            /*
             * Give the form access to the same inventory service
             * used by the main screen.
             */
            formController.setInventoryService(
                    inventoryService
            );

            /*
             * Refresh the main table after a part is added.
             */
            formController.setOnPartSaved(() -> {

                refreshInventoryTable();

                statusLabel.setText(
                        "A new spare part was added successfully."
                );
            });

            /*
             * Create a new window for the form.
             */
            Stage formStage = new Stage();

            formStage.setTitle("Add Spare Part");
            formStage.setScene(new Scene(formRoot));

            /*
             * Prevent the user from using the main window
             * until this form is closed.
             */
            formStage.initModality(Modality.APPLICATION_MODAL);

            formStage.setResizable(false);
            formStage.showAndWait();

        } catch (IOException exception) {

            statusLabel.setText(
                    "Could not open the Add Part form."
            );

            exception.printStackTrace();
        }
    }
    /**
     * Deletes the selected spare part after asking
     * the user to confirm the action.
     */
    @FXML
    private void onDeletePartButtonClick() {

        // Get the row currently selected in the table.
        SparePart selectedPart =
                inventoryTable.getSelectionModel().getSelectedItem();

        // Stop if the user did not select a row.
        if (selectedPart == null) {
            statusLabel.setText(
                    "Please select a spare part to delete."
            );
            return;
        }

        /*
         * Create a confirmation dialog so a record
         * cannot be deleted accidentally.
         */
        Alert confirmationAlert =
                new Alert(Alert.AlertType.CONFIRMATION);

        confirmationAlert.setTitle("Confirm Delete");
        confirmationAlert.setHeaderText(
                "Delete spare part "
                        + selectedPart.getPartCode()
                        + "?"
        );

        confirmationAlert.setContentText(
                "This action cannot be undone."
        );

        /*
         * Display the dialog and wait for the user
         * to choose OK or Cancel.
         */
        ButtonType selectedButton =
                confirmationAlert.showAndWait()
                        .orElse(ButtonType.CANCEL);

        // Stop when the user presses Cancel.
        if (selectedButton != ButtonType.OK) {
            statusLabel.setText(
                    "Delete operation cancelled."
            );
            return;
        }

        /*
         * Ask InventoryService to remove the selected part.
         */
        boolean deleted =
                inventoryService.deleteSparePart(
                        selectedPart.getPartCode()
                );

        if (!deleted) {
            statusLabel.setText(
                    "The spare part could not be deleted."
            );
            return;
        }

        // Reload the table and summary values.
        refreshInventoryTable();

        statusLabel.setText(
                "Spare part "
                        + selectedPart.getPartCode()
                        + " deleted successfully."
        );
    }

    /**
     * Reloads the complete inventory table.
     */
    @FXML
    private void onRefreshButtonClick() {

        searchField.clear();
        refreshInventoryTable();

        statusLabel.setText(
                "Inventory table refreshed."
        );
    }
    /**
     * Opens the selected spare part in the edit form.
     */
    @FXML
    private void onUpdatePartButtonClick() {

        // Get the selected row.
        SparePart selectedPart = inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            statusLabel.setText("Please select a spare part to update.");
            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("part-form.fxml"));

            Parent root = loader.load();

            PartFormController controller = loader.getController();

            controller.setInventoryService(inventoryService);

            controller.setOnPartSaved(this::refreshInventoryTable);

            controller.setPartToEdit(selectedPart);

            Stage stage = new Stage();
            stage.setTitle("Update Spare Part");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshInventoryTable();

        } catch (IOException exception) {

            exception.printStackTrace();

            statusLabel.setText("Could not open the Update Part form.");
        }
    }

}
