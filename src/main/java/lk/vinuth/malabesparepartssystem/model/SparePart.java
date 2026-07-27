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

    // A unique code used to identify the spare part, for example P001.
    private String partCode;

    // The name or description of the spare part.
    private String partName;

    // The manufacturer or brand. This may be empty in the legacy file.
    private String brand;

    // The selling price of one unit of the spare part.
    private double price;

    // The number of units currently available in stock.
    private int quantity;

    // The type of part, such as Engine, Electrical, Brakes or Bodywork.
    private String category;

    // The date associated with the inventory record.
    private LocalDate dateAdded;

    // The optional image filename stored in the legacy inventory file.
    private String imageFileName;

    /**
     * Creates a new SparePart object using all required inventory values.
     *
     * @param partCode     unique part code
     * @param partName     name of the part
     * @param brand        manufacturer or brand
     * @param price        price of one unit
     * @param quantity     available stock quantity
     * @param category     inventory category
     * @param dateAdded    date stored for the item
     * @param imageFileName optional image filename
     */
    public SparePart(
            String partCode,
            String partName,
            String brand,
            double price,
            int quantity,
            String category,
            LocalDate dateAdded,
            String imageFileName
    ) {
        this.partCode = partCode;
        this.partName = partName;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.imageFileName = imageFileName;
    }

    // Returns the unique code of this spare part.
    public String getPartCode() {
        return partCode;
    }

    // Changes the unique code of this spare part.
    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    // Returns the part name.
    public String getPartName() {
        return partName;
    }

    // Changes the part name.
    public void setPartName(String partName) {
        this.partName = partName;
    }

    // Returns the brand.
    public String getBrand() {
        return brand;
    }

    // Changes the brand.
    public void setBrand(String brand) {
        this.brand = brand;
    }

    // Returns the price of one unit.
    public double getPrice() {
        return price;
    }

    // Changes the price of one unit.
    public void setPrice(double price) {
        this.price = price;
    }

    // Returns the current stock quantity.
    public int getQuantity() {
        return quantity;
    }

    // Changes the current stock quantity.
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Returns the inventory category.
    public String getCategory() {
        return category;
    }

    // Changes the inventory category.
    public void setCategory(String category) {
        this.category = category;
    }

    // Returns the date stored for the item.
    public LocalDate getDateAdded() {
        return dateAdded;
    }

    // Changes the date stored for the item.
    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }

    // Returns the optional image filename.
    public String getImageFileName() {
        return imageFileName;
    }

    // Changes the optional image filename.
    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * Calculates the total value of the available stock for this item.
     *
     * Example:
     * price = 4500 and quantity = 15
     * total value = 4500 × 15
     *
     * @return total stock value for this item
     */
    public double calculateStockValue() {
        return price * quantity;
    }

    /**
     * Checks whether this item is below the selected low-stock threshold.
     *
     * @param threshold minimum acceptable quantity
     * @return true when the quantity is below the threshold
     */
    public boolean isLowStock(int threshold) {
        return quantity < threshold;
    }
}