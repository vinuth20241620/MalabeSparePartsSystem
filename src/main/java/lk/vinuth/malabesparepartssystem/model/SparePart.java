package lk.vinuth.malabesparepartssystem.model;

import java.time.LocalDate;

public class SparePart {

    private String partCode;

    private String partName;

    private String brand;

    private double price;

    private int quantity;

    private String category;

    private LocalDate dateAdded;

    private String imageFileName;

    public SparePart() {
    }

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

    public double calculateStockValue() {
        return price * quantity;
    }

    public boolean isLowStock(int threshold) {
        return quantity < threshold;
    }

    @Override
    public String toString() {
        return partName + " (" + partCode + ")";
    }
}