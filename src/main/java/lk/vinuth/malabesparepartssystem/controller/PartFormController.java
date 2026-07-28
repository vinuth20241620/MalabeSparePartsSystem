package lk.vinuth.malabesparepartssystem.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.service.InventoryService;

import java.time.LocalDate;

/**
 * Controls the form used to add a new spare part.
 *
 * This controller reads values entered in the form,
 * validates them, creates a SparePart object and sends
 * it to InventoryService.
 */
public class PartFormController {

    // Text field used to enter the unique part code.
    @FXML
    private TextField partCodeField;

    // Text field used to enter the part name.
    @FXML
    private TextField partNameField;

    // Brand is optional because some legacy records have no brand.
    @FXML
    private TextField brandField;

    // Text field used to enter the selling price.
    @FXML
    private TextField priceField;

    // Text field used to enter the available stock quantity.
    @FXML
    private TextField quantityField;

    // Text field used to enter the inventory category.
    @FXML
    private TextField categoryField;

    // Date picker used to select the date the part was added.
    @FXML
    private DatePicker dateAddedPicker;

    // Optional image filename.
    @FXML
    private TextField imageFileNameField;

    // Displays validation errors and success messages.
    @FXML
    private Label formStatusLabel;

    // Service used to add the completed SparePart object.
    private InventoryService inventoryService;

    /*
     * This action will refresh the main inventory table
     * after a part is successfully added.
     */
    private Runnable onPartSaved;

    /*
     * Stores the original part code when the form
     * is being used to update an existing part.
     *
     * It remains null when adding a new part.
     */
    private String originalPartCode;

    /*
     * True when this form is updating an existing part.
     * False when this form is adding a new part.
     */
    private boolean updateMode;

    /**
     * Runs automatically when the form FXML is loaded.
     */
    @FXML
    private void initialize() {

        // Use today's date as the default value.
        dateAddedPicker.setValue(LocalDate.now());

        // Start with no message.
        formStatusLabel.setText("");
    }

    /**
     * Supplies the inventory service used by this controller.
     *
     * @param inventoryService service containing inventory operations
     */
    public void setInventoryService(
            InventoryService inventoryService
    ) {
        this.inventoryService = inventoryService;
    }

    /**
     * Loads an existing spare part into the form
     * so the user can edit it.
     */
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

    /**
     * Supplies the action that refreshes the main inventory screen.
     *
     * @param onPartSaved action to run after saving
     */
    public void setOnPartSaved(Runnable onPartSaved) {
        this.onPartSaved = onPartSaved;
    }
    /**
     * Reads and validates all form values.
     *
     * If every value is valid, a new SparePart object
     * is created and added through InventoryService.
     */
    @FXML
    private void onSaveButtonClick() {

        // Stop if the service was not supplied by the main controller.
        if (inventoryService == null) {
            formStatusLabel.setText(
                    "Inventory service is not available."
            );
            return;
        }

        // Read and remove unnecessary spaces from text fields.
        String partCode = partCodeField.getText().trim();
        String partName = partNameField.getText().trim();
        String brand = brandField.getText().trim();
        String priceText = priceField.getText().trim();
        String quantityText = quantityField.getText().trim();
        String category = categoryField.getText().trim();
        String imageFileName =
                imageFileNameField.getText().trim();

        LocalDate dateAdded = dateAddedPicker.getValue();

        /*
         * Validate required text fields.
         *
         * Brand and image filename are optional,
         * so they are not checked here.
         */
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

        // Part codes must begin with P and contain digits.
        if (!partCode.matches("(?i)P\\d+")) {
            formStatusLabel.setText(
                    "Part code must look like P001."
            );
            return;
        }

        double price;
        int quantity;

        try {
            // Convert the price text into a decimal number.
            price = Double.parseDouble(priceText);

            // Convert the quantity text into a whole number.
            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException exception) {

            formStatusLabel.setText(
                    "Price and quantity must be valid numbers."
            );
            return;
        }

        // Prevent zero or negative prices.
        if (price <= 0) {
            formStatusLabel.setText(
                    "Price must be greater than zero."
            );
            return;
        }

        // Stock quantity cannot be negative.
        if (quantity < 0) {
            formStatusLabel.setText(
                    "Quantity cannot be negative."
            );
            return;
        }

        /*
         * Standardise the part code and category
         * before creating the object.
         */
        partCode = partCode.toUpperCase();
        category = standardiseCategory(category);

        // Create the completed SparePart object.
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

        /*
         * Use different service operations depending on whether
         * the form is adding or updating a spare part.
         */
        if (updateMode) {

            /*
             * Update the existing object using its original code.
             */
            boolean updated = inventoryService.updateSparePart(
                    originalPartCode,
                    newPart
            );

            // Stop if the update was unsuccessful.
            if (!updated) {
                formStatusLabel.setText(
                        "The spare part could not be updated."
                );
                return;
            }

            // Confirm the successful update.
            formStatusLabel.setText(
                    "Spare part updated successfully."
            );

        } else {

            /*
             * Add a completely new spare part.
             */
            boolean added =
                    inventoryService.addSparePart(newPart);

            // Reject duplicate part codes.
            if (!added) {
                formStatusLabel.setText(
                        "A spare part with this code already exists."
                );
                return;
            }

            // Confirm the successful addition.
            formStatusLabel.setText(
                    "Spare part added successfully."
            );
        }

        /*
         * Refresh the inventory table after either
         * a successful addition or a successful update.
         */
        if (onPartSaved != null) {
            onPartSaved.run();
        }

        /*
         * Clear the form only when adding a new part.
         * During an update, keep the updated values visible.
         */
        if (!updateMode) {
            clearForm();
        }
    }
    /**
     * Converts category text into one consistent format.
     *
     * Example:
     * "ENGINE" becomes "Engine"
     * "electrical" becomes "Electrical"
     *
     * @param category original category text
     * @return formatted category text
     */
    private String standardiseCategory(String category) {

        // Remove unnecessary spaces.
        String cleanedCategory = category.trim();

        // Return an empty value if no category was entered.
        if (cleanedCategory.isEmpty()) {
            return "";
        }

        // Convert the full word to lowercase first.
        cleanedCategory = cleanedCategory.toLowerCase();

        // Capitalise the first letter.
        return cleanedCategory.substring(0, 1).toUpperCase()
                + cleanedCategory.substring(1);
    }

    /**
     * Clears all form fields after a successful save.
     */
    private void clearForm() {

        partCodeField.clear();
        partNameField.clear();
        brandField.clear();
        priceField.clear();
        quantityField.clear();
        categoryField.clear();
        imageFileNameField.clear();

        // Reset the date to today.
        dateAddedPicker.setValue(LocalDate.now());

        // Return the cursor to the first field.
        partCodeField.requestFocus();
    }

    /**
     * Clears the form when the user presses the Clear button.
     */
    @FXML
    private void onClearButtonClick() {

        clearForm();
        formStatusLabel.setText("Form cleared.");
    }
}
