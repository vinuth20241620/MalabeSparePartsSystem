package lk.vinuth.malabesparepartssystem.repository;

import lk.vinuth.malabesparepartssystem.model.SparePart;

import java.util.ArrayList;
import java.util.List;

public class SparePartRepository {

    /*
     * This ArrayList stores every spare part
     * currently loaded into memory.
     */
    private final List<SparePart> spareParts = new ArrayList<>();
    public SparePartRepository() {
        // The repository starts with an empty inventory list.
    }

    public List<SparePart> getAllSpareParts() {
        return new ArrayList<>(spareParts);
    }


    public SparePart findByPartCode(String partCode) {

        for (SparePart sparePart : spareParts) {

            if (sparePart.getPartCode().equalsIgnoreCase(partCode)) {
                return sparePart;
            }
        }

        return null;
    }

    public boolean containsPartCode(String partCode) {
        return findByPartCode(partCode) != null;
    }

    public void addSparePart(SparePart sparePart) {

        spareParts.add(sparePart);
    }


    public boolean removeSparePart(SparePart sparePart) {

        return spareParts.remove(sparePart);
    }

    public boolean removeByPartCode(String partCode) {

        SparePart sparePart = findByPartCode(partCode);

        if (sparePart != null) {
            return spareParts.remove(sparePart);
        }

        return false;
    }

    public int getTotalParts() {
        return spareParts.size();
    }

    public void clearRepository() {
        spareParts.clear();
    }

    public boolean updateSparePart(
            String partCode,
            SparePart updatedPart
    ) {


        SparePart existingPart = findByPartCode(partCode);

        if (existingPart == null) {
            return false;
        }

        existingPart.setPartCode(updatedPart.getPartCode());
        existingPart.setPartName(updatedPart.getPartName());
        existingPart.setBrand(updatedPart.getBrand());
        existingPart.setPrice(updatedPart.getPrice());
        existingPart.setQuantity(updatedPart.getQuantity());
        existingPart.setCategory(updatedPart.getCategory());
        existingPart.setDateAdded(updatedPart.getDateAdded());
        existingPart.setImageFileName(updatedPart.getImageFileName());

        return true;
    }

    public double calculateTotalInventoryValue() {

        double totalValue = 0.0;

        for (SparePart sparePart : spareParts) {

            totalValue += sparePart.calculateStockValue();
        }

        return totalValue;
    }
}