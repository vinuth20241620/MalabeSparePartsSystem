package lk.vinuth.malabesparepartssystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.service.InventoryService;
import lk.vinuth.malabesparepartssystem.util.AuditLogger;

import java.time.LocalDate;

public class PartFormController {

    @FXML
    private TextField partCodeField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField categoryField;

    @FXML
    private DatePicker dateAddedPicker;

    @FXML
    private TextField imageFileNameField;

    @FXML
    private Label formStatusLabel;

    private InventoryService inventoryService;

    private Runnable onPartSaved;

    private String originalPartCode;

    private boolean updateMode;

    @FXML
    private void initialize() {

        dateAddedPicker.setValue(LocalDate.now());

        formStatusLabel.setText("");
    }

    public void setInventoryService(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    public void setPartToEdit(SparePart part) {

        updateMode = true;
        originalPartCode = part.getPartCode();

        partCodeField.setText(part.getPartCode());
        partNameField.setText(part.getPartName());
        brandField.setText(part.getBrand());
        priceField.setText(String.valueOf(part.getPrice()));
        quantityField.setText(String.valueOf(part.getQuantity()));
        categoryField.setText(part.getCategory());
        dateAddedPicker.setValue(part.getDateAdded());
        imageFileNameField.setText(part.getImageFileName());

        partCodeField.setDisable(true);


    }

    public void setOnPartSaved(Runnable onPartSaved) {
        this.onPartSaved = onPartSaved;
    }
    @FXML
    private void onSaveButtonClick() {

        if (inventoryService == null) {
            formStatusLabel.setText(
                    "Inventory service is not available."
            );
            return;
        }

        String partCode = partCodeField.getText().trim();
        String partName = partNameField.getText().trim();
        String brand = brandField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String category = categoryField.getText().trim();
        String imageFileName =
                imageFileNameField.getText().trim();

        LocalDate dateAdded = dateAddedPicker.getValue();

        if (partCode.isEmpty()
                || partName.isEmpty()
                || priceText.isEmpty()
                || quantityText.isEmpty()
                || category.isEmpty()
                || dateAdded == null) {

            formStatusLabel.setText(
                    "Complete all required fields."
            );
            return;
        }

        if (!partCode.matches("(?i)P\\d+")) {
            formStatusLabel.setText(
                    "Part code must look like P001."
            );
            return;
        }

        double price;
        int quantity;

        try {
            price = Double.parseDouble(priceText);

            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException exception) {

            formStatusLabel.setText(
                    "Price and quantity must be valid numbers."
            );
            return;
        }

        if (price <= 0) {
            formStatusLabel.setText(
                    "Price must be greater than zero."
            );
            return;
        }

        if (quantity < 0) {
            formStatusLabel.setText(
                    "Quantity cannot be negative."
            );
            return;
        }

        partCode = partCode.toUpperCase();
        category = standardiseCategory(category);

        SparePart newPart = new SparePart(
                partCode,
                partName,
                brand,
                price,
                quantity,
                category,
                dateAdded,
                imageFileName
        );

        if (updateMode) {

            boolean updated = inventoryService.updateSparePart(
                    originalPartCode,
                    newPart
            );

            if (!updated) {
                formStatusLabel.setText(
                        "The spare part could not be updated."
                );
                return;
            }

            formStatusLabel.setText(
                    "Spare part updated successfully."
            );

            AuditLogger.logAction(
                    "UPDATE_PART",
                    newPart.getPartCode(),
                    newPart.getQuantity()
            );

        } else {

            boolean added =
                    inventoryService.addSparePart(newPart);

            if (!added) {
                formStatusLabel.setText(
                        "A spare part with this code already exists."
                );
                return;
            }

            formStatusLabel.setText(
                    "Spare part added successfully."
            );

            AuditLogger.logAction(
                    "ADD_PART",
                    newPart.getPartCode(),
                    newPart.getQuantity()
            );
        }

        if (onPartSaved != null) {
            onPartSaved.run();
        }

        if (!updateMode) {
            clearForm();
        }
    }
    private String standardiseCategory(String category) {

        String cleanedCategory = category.trim();

        if (cleanedCategory.isEmpty()) {
            return "";
        }

        cleanedCategory = cleanedCategory.toLowerCase();

        return cleanedCategory.substring(0, 1).toUpperCase()
                + cleanedCategory.substring(1);
    }

    private void clearForm() {

        partCodeField.clear();
        partNameField.clear();
        brandField.clear();
        priceField.clear();
        quantityField.clear();
        categoryField.clear();
        imageFileNameField.clear();

        dateAddedPicker.setValue(LocalDate.now());

        partCodeField.requestFocus();
    }

    @FXML
    private void onClearButtonClick() {

        clearForm();
        formStatusLabel.setText("Form cleared.");
    }
}
