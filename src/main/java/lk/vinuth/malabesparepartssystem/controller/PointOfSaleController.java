package lk.vinuth.malabesparepartssystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.service.InventoryService;
import lk.vinuth.malabesparepartssystem.util.AuditLogger;

import java.util.List;

public class PointOfSaleController {

    @FXML
    private ComboBox<SparePart> partComboBox;

    @FXML
    private Label unitPriceLabel;

    @FXML
    private Label availableStockLabel;

    @FXML
    private TextField saleQuantityField;

    @FXML
    private Label saleTotalLabel;

    @FXML
    private Label posStatusLabel;

    private InventoryService inventoryService;

    private Runnable onSaleCompleted;

    @FXML
    private void initialize() {

        unitPriceLabel.setText("Rs. 0.00");
        availableStockLabel.setText("0");
        saleTotalLabel.setText("Rs. 0.00");
        posStatusLabel.setText("");
    }

    public void setInventoryService(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;

        loadSpareParts();
    }

    public void setOnSaleCompleted(
            Runnable onSaleCompleted
    ) {
        this.onSaleCompleted = onSaleCompleted;
    }

    private void loadSpareParts() {

        if (inventoryService == null) {
            return;
        }

        List<SparePart> spareParts =
                inventoryService.getAllSpareParts();

        partComboBox.setItems(
                FXCollections.observableArrayList(spareParts)
        );
    }
    @FXML
    private void onPartSelected() {

        SparePart selectedPart =
                partComboBox.getValue();

        if (selectedPart == null) {
            unitPriceLabel.setText("Rs. 0.00");
            availableStockLabel.setText("0");
            saleTotalLabel.setText("Rs. 0.00");
            return;
        }

        unitPriceLabel.setText(
                String.format(
                        "Rs. %.2f",
                        selectedPart.getPrice()
                )
        );

        availableStockLabel.setText(
                String.valueOf(selectedPart.getQuantity())
        );

        saleTotalLabel.setText("Rs. 0.00");
        saleQuantityField.clear();
        posStatusLabel.setText("");
    }

    @FXML
    private void onCalculateTotalButtonClick() {

        SparePart selectedPart =
                partComboBox.getValue();

        if (selectedPart == null) {
            posStatusLabel.setText(
                    "Select a spare part first."
            );
            return;
        }

        String quantityText =
                saleQuantityField.getText().trim();

        if (quantityText.isEmpty()) {
            posStatusLabel.setText(
                    "Enter the sale quantity."
            );
            return;
        }

        int saleQuantity;

        try {
            saleQuantity =
                    Integer.parseInt(quantityText);

        } catch (NumberFormatException exception) {
            posStatusLabel.setText(
                    "Sale quantity must be a whole number."
            );
            return;
        }

        if (saleQuantity <= 0) {
            posStatusLabel.setText(
                    "Sale quantity must be greater than zero."
            );
            return;
        }

        if (saleQuantity > selectedPart.getQuantity()) {
            posStatusLabel.setText(
                    "Not enough stock is available."
            );
            return;
        }

        double saleTotal =
                selectedPart.getPrice() * saleQuantity;

        saleTotalLabel.setText(
                String.format(
                        "Rs. %.2f",
                        saleTotal
                )
        );

        posStatusLabel.setText(
                "Sale total calculated."
        );
    }
    @FXML
    private void onCompleteSaleButtonClick() {

        SparePart selectedPart =
                partComboBox.getValue();

        if (selectedPart == null) {
            posStatusLabel.setText(
                    "Select a spare part first."
            );
            return;
        }

        String quantityText =
                saleQuantityField.getText().trim();

        int saleQuantity;

        try {
            saleQuantity =
                    Integer.parseInt(quantityText);

        } catch (NumberFormatException exception) {
            posStatusLabel.setText(
                    "Enter a valid whole-number quantity."
            );
            return;
        }

        if (saleQuantity <= 0) {
            posStatusLabel.setText(
                    "Sale quantity must be greater than zero."
            );
            return;
        }

        if (saleQuantity > selectedPart.getQuantity()) {
            posStatusLabel.setText(
                    "Not enough stock is available."
            );
            return;
        }

        int newQuantity =
                selectedPart.getQuantity() - saleQuantity;

        selectedPart.setQuantity(newQuantity);

        availableStockLabel.setText(
                String.valueOf(newQuantity)
        );

        double saleTotal =
                selectedPart.getPrice() * saleQuantity;

        saleTotalLabel.setText(
                String.format(
                        "Rs. %.2f",
                        saleTotal
                )
        );

        posStatusLabel.setText(
                "Sale completed successfully."
        );

        AuditLogger.logAction(
                "CHECKOUT",
                selectedPart.getPartCode(),
                saleQuantity
        );

        if (onSaleCompleted != null) {
            onSaleCompleted.run();
        }
    }

    @FXML
    private void onClearButtonClick() {

        partComboBox.getSelectionModel().clearSelection();
        saleQuantityField.clear();

        unitPriceLabel.setText("Rs. 0.00");
        availableStockLabel.setText("0");
        saleTotalLabel.setText("Rs. 0.00");
        posStatusLabel.setText("");
    }
}
