package lk.vinuth.malabesparepartssystem.model;

import java.time.LocalDate;

/**
 * Represents one spare part stored in the inventory.
 *
 * Each object created from this class stores all information
 * belonging to one spare part, such as its code, name, price,
 * quantity, category and date.
 */
public class SparePart {

    // A unique code used to identify the spare part.
    private String partCode;

    // The name or description of the spare part.
    private String partName;

    // The manufacturer or brand.
    private String brand;

    // Selling price of one unit.
    private double price;

    // Number of items available.
    private int quantity;

    // Category such as Engine, Electrical or Bodywork.
    private String category;

    // Date when the item was added.
    private LocalDate dateAdded;

    // Image filename associated with this spare part.
    private String imageFileName;

    /**
     * Creates an empty SparePart object.
     * Values can later be assigned using setter methods.
     */
    public SparePart() {
    }

    /**
     * Creates a new SparePart object with all required information.
     *
     * @param partCode unique code
     * @param partName name of the spare part
     * @param brand manufacturer or brand
     * @param price selling price
     * @param quantity stock quantity
     * @param category inventory category
     * @param dateAdded date stored
     * @param imageFileName image filename
     */
    public SparePart(
            String partCode,
            String partName,
            String brand,
            double price,
            int quantity,
            String category,
            LocalDate dateAdded,
            String imageFileName) {

        this.partCode = partCode;
        this.partName = partName;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.imageFileName = imageFileName;
    }
    // ==========================
    // Getters
    // ==========================

    public String getPartCode() {
        return partCode;
    }

    public String getPartName() {
        return partName;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public String getImageFileName() {
        return imageFileName;
    }
    // ==========================
    // Setters
    // ==========================

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * Calculates the total value of the stock for this spare part.
     *
     * @return stock value
     */
    public double calculateStockValue() {
        return price * quantity;
    }

    /**
     * Checks whether the stock quantity is below a specified limit.
     *
     * @param threshold minimum acceptable quantity
     * @return true if stock is low, otherwise false
     */
    public boolean isLowStock(int threshold) {
        return quantity < threshold;
    }

    /**
     * Returns the spare part as readable text.
     */
    @Override
    public String toString() {
        return "SparePart{" +
                "partCode='" + partCode + '\'' +
                ", partName='" + partName + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", dateAdded=" + dateAdded +
                ", imageFileName='" + imageFileName + '\'' +
                '}';
    }
}