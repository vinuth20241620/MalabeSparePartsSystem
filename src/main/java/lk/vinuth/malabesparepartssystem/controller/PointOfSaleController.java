package lk.vinuth.malabesparepartssystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.service.InventoryService;

import java.util.List;

/**
 * Controls the Point of Sale window.
 *
 * The user selects a spare part, enters a sale quantity,
 * checks the total price and confirms the sale.
 */
public class PointOfSaleController {

    // Dropdown containing all available spare parts.
    @FXML
    private ComboBox<SparePart> partComboBox;

    // Displays the selected part's unit price.
    @FXML
    private Label unitPriceLabel;

    // Displays the selected part's available quantity.
    @FXML
    private Label availableStockLabel;

    // Field used to enter the quantity being sold.
    @FXML
    private TextField saleQuantityField;

    // Displays the calculated sale total.
    @FXML
    private Label saleTotalLabel;

    // Displays validation messages and sale results.
    @FXML
    private Label posStatusLabel;

    // Service used to read and update inventory data.
    private InventoryService inventoryService;

    // Action used to refresh the main inventory table.
    private Runnable onSaleCompleted;

    /**
     * Runs automatically when the FXML file is loaded.
     */
    @FXML
    private void initialize() {

        unitPriceLabel.setText("Rs. 0.00");
        availableStockLabel.setText("0");
        saleTotalLabel.setText("Rs. 0.00");
        posStatusLabel.setText("");
    }

    /**
     * Supplies the inventory service used by this window.
     *
     * @param inventoryService service containing inventory data
     */
    public void setInventoryService(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;

        loadSpareParts();
    }

    /**
     * Supplies the action that refreshes the main inventory screen.
     *
     * @param onSaleCompleted action to run after a completed sale
     */
    public void setOnSaleCompleted(
            Runnable onSaleCompleted
    ) {
        this.onSaleCompleted = onSaleCompleted;
    }

    /**
     * Loads all spare parts into the dropdown.
     */
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
    /**
     * Runs when the user selects a spare part.
     *
     * The method displays the unit price and
     * the currently available stock quantity.
     */
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

        // Clear the previous total when another part is selected.
        saleTotalLabel.setText("Rs. 0.00");
        saleQuantityField.clear();
        posStatusLabel.setText("");
    }

    /**
     * Calculates the total price for the requested quantity.
     */
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
    /**
     * Completes the sale and reduces the selected part's stock.
     */
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

        /*
         * Calculate the new stock quantity after the sale.
         */
        int newQuantity =
                selectedPart.getQuantity() - saleQuantity;

        /*
         * Update the selected object.
         */
        selectedPart.setQuantity(newQuantity);

        /*
         * Refresh the labels inside the POS window.
         */
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

        /*
         * Refresh the main inventory table and totals.
         */
        if (onSaleCompleted != null) {
            onSaleCompleted.run();
        }
    }

    /**
     * Clears the current Point of Sale form.
     */
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
