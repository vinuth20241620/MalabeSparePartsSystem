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
import lk.vinuth.malabesparepartssystem.controller.PointOfSaleController;
import lk.vinuth.malabesparepartssystem.controller.DealerController;
import lk.vinuth.malabesparepartssystem.util.AuditLogger;

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

public class HelloController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<SparePart> inventoryTable;

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

    @FXML
    private Label totalPartsLabel;

    @FXML
    private Label totalValueLabel;

    @FXML
    private Label statusLabel;

    private ApplicationDataService applicationDataService;

    private InventoryService inventoryService;

    @FXML
    private void initialize() {

        applicationDataService = new ApplicationDataService();

        applicationDataService.loadLegacyData();

        inventoryService = applicationDataService.getInventoryService();

        configureTableColumns();

        refreshInventoryTable();

        statusLabel.setText("Legacy inventory loaded successfully.");
    }
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

    private void refreshInventoryTable() {

        List<SparePart> spareParts =
                inventoryService.getAllSpareParts();

        ObservableList<SparePart> tableData =
                FXCollections.observableArrayList(spareParts);

        inventoryTable.setItems(tableData);
        inventoryTable.refresh();

        totalPartsLabel.setText(
                String.valueOf(inventoryService.getTotalParts())
        );

        totalValueLabel.setText(
                String.format(
                        "Rs. %.2f",
                        inventoryService.calculateTotalInventoryValue()
                )
        );
    }

    @FXML
    private void onSearchButtonClick() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            refreshInventoryTable();
            statusLabel.setText(
                    "Enter a code, name, brand or category to search."
            );
            return;
        }

        String lowerKeyword = keyword.toLowerCase();

        ObservableList<SparePart> matchingParts =
                FXCollections.observableArrayList();

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

            if (codeMatches
                    || nameMatches
                    || brandMatches
                    || categoryMatches) {

                matchingParts.add(sparePart);
            }
        }

        inventoryTable.setItems(matchingParts);

        statusLabel.setText(
                matchingParts.size()
                        + " matching inventory record(s) found."
        );
    }

    private boolean containsIgnoreCase(
            String text,
            String lowerKeyword
    ) {

        if (text == null) {
            return false;
        }

        return text.toLowerCase().contains(lowerKeyword);
    }

    @FXML
    private void onClearSearchButtonClick() {

        searchField.clear();
        refreshInventoryTable();

        statusLabel.setText(
                "Search cleared. All inventory records are displayed."
        );
    }

    @FXML
    private void onAddPartButtonClick() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "part-form.fxml"
                    )
            );

            Parent formRoot = loader.load();

            PartFormController formController =
                    loader.getController();

            formController.setInventoryService(
                    inventoryService
            );

            formController.setOnPartSaved(() -> {

                refreshInventoryTable();

                statusLabel.setText(
                        "A new spare part was added successfully."
                );
            });

            Stage formStage = new Stage();

            formStage.setTitle("Add Spare Part");
            formStage.setScene(new Scene(formRoot));

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
    @FXML
    private void onDeletePartButtonClick() {

        SparePart selectedPart =
                inventoryTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            statusLabel.setText(
                    "Please select a spare part to delete."
            );
            return;
        }

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

        ButtonType selectedButton =
                confirmationAlert.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (selectedButton != ButtonType.OK) {
            statusLabel.setText(
                    "Delete operation cancelled."
            );
            return;
        }

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

        AuditLogger.logAction(
                "DELETE_PART",
                selectedPart.getPartCode(),
                selectedPart.getQuantity()
        );

        refreshInventoryTable();

        statusLabel.setText(
                "Spare part "
                        + selectedPart.getPartCode()
                        + " deleted successfully."
        );
    }

    @FXML
    private void onRefreshButtonClick() {

        searchField.clear();
        refreshInventoryTable();

        statusLabel.setText(
                "Inventory table refreshed."
        );
    }
    @FXML
    private void onUpdatePartButtonClick() {

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
    @FXML
    private void onLowStockButtonClick() {

        ObservableList<SparePart> lowStockParts =
                FXCollections.observableArrayList();

        for (SparePart part
                : inventoryService.getAllSpareParts()) {

            if (part.getQuantity() <= 5) {
                lowStockParts.add(part);
            }
        }

        inventoryTable.setItems(lowStockParts);

        inventoryTable.refresh();

        statusLabel.setText(
                lowStockParts.size()
                        + " low stock part(s) found."
        );
    }
    @FXML
    private void onDealersButtonClick() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "dealer-view.fxml"
                    )
            );

            Parent dealerRoot = loader.load();

            DealerController dealerController =
                    loader.getController();

            dealerController.setDealerService(
                    applicationDataService.getDealerService()
            );

            Stage dealerStage = new Stage();

            dealerStage.setTitle("Dealer Management");
            dealerStage.setScene(new Scene(dealerRoot));
            dealerStage.initModality(Modality.APPLICATION_MODAL);
            dealerStage.setResizable(false);
            dealerStage.showAndWait();

        } catch (IOException exception) {

            statusLabel.setText(
                    "Could not open the Dealer Management window."
            );

            exception.printStackTrace();
        }
    }
    /**
     * Opens the Point of Sale window.
     */
    @FXML
    private void onPointOfSaleButtonClick() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource(
                            "point-of-sale.fxml"
                    )
            );

            Parent posRoot = loader.load();

            PointOfSaleController posController =
                    loader.getController();

            posController.setInventoryService(
                    inventoryService
            );

            posController.setOnSaleCompleted(() -> {

                refreshInventoryTable();

                statusLabel.setText(
                        "Sale completed and inventory stock updated."
                );
            });

            Stage posStage = new Stage();

            posStage.setTitle("Point of Sale");
            posStage.setScene(new Scene(posRoot));
            posStage.initModality(Modality.APPLICATION_MODAL);
            posStage.setResizable(false);
            posStage.showAndWait();

        } catch (IOException exception) {

            statusLabel.setText(
                    "Could not open the Point of Sale window."
            );

            exception.printStackTrace();
        }
    }
    @FXML
    private void onReportsButtonClick() {

        List<SparePart> spareParts =
                inventoryService.getAllSpareParts();

        if (spareParts.isEmpty()) {

            Alert emptyAlert =
                    new Alert(Alert.AlertType.INFORMATION);

            emptyAlert.setTitle("Inventory Report");
            emptyAlert.setHeaderText("No inventory records");
            emptyAlert.setContentText(
                    "There are no spare parts available for reporting."
            );

            emptyAlert.showAndWait();
            return;
        }

        int lowStockCount = 0;
        int outOfStockCount = 0;

        double totalPrice = 0.0;

        SparePart mostExpensivePart = spareParts.get(0);

        for (SparePart sparePart : spareParts) {

            if (sparePart.getQuantity() <= 5) {
                lowStockCount++;
            }

            if (sparePart.getQuantity() == 0) {
                outOfStockCount++;
            }

            totalPrice += sparePart.getPrice();

            if (sparePart.getPrice()
                    > mostExpensivePart.getPrice()) {

                mostExpensivePart = sparePart;
            }
        }

        double averagePrice =
                totalPrice / spareParts.size();

        double inventoryValue =
                inventoryService.calculateTotalInventoryValue();

        String reportText =
                "Total Parts: "
                        + spareParts.size()
                        + "\n\n"
                        + "Total Inventory Value: Rs. "
                        + String.format("%.2f", inventoryValue)
                        + "\n\n"
                        + "Low Stock Items: "
                        + lowStockCount
                        + "\n\n"
                        + "Out of Stock Items: "
                        + outOfStockCount
                        + "\n\n"
                        + "Most Expensive Part: "
                        + mostExpensivePart.getPartName()
                        + " ("
                        + mostExpensivePart.getPartCode()
                        + ")"
                        + "\n"
                        + "Unit Price: Rs. "
                        + String.format(
                        "%.2f",
                        mostExpensivePart.getPrice()
                )
                        + "\n\n"
                        + "Average Unit Price: Rs. "
                        + String.format("%.2f", averagePrice);

        Alert reportAlert =
                new Alert(Alert.AlertType.INFORMATION);

        reportAlert.setTitle("Inventory Report");
        reportAlert.setHeaderText(
                "Malabe Spare Parts Inventory Summary"
        );

        reportAlert.setContentText(reportText);
        reportAlert.showAndWait();
    }

}
