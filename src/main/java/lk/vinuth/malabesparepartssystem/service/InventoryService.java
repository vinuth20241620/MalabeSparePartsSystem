package lk.vinuth.malabesparepartssystem.service;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.repository.SparePartRepository;

import java.util.List;

public class InventoryService {

    private final SparePartRepository repository =
            new SparePartRepository();

    public List<SparePart> getAllSpareParts() {
        return repository.getAllSpareParts();
    }

    public SparePart findSparePart(String partCode) {
        return repository.findByPartCode(partCode);
    }

    public boolean addSparePart(SparePart sparePart) {

        if (repository.containsPartCode(sparePart.getPartCode())) {
            return false;
        }

        repository.addSparePart(sparePart);
        return true;
    }

    public boolean deleteSparePart(String partCode) {
        return repository.removeByPartCode(partCode);
    }

    public boolean updateSparePart(
            String originalPartCode,
            SparePart updatedPart
    ) {

        if (!originalPartCode.equalsIgnoreCase(updatedPart.getPartCode())
                && repository.containsPartCode(updatedPart.getPartCode())) {

            return false;
        }

        return repository.updateSparePart(originalPartCode, updatedPart);
    }

    public int getTotalParts() {
        return repository.getTotalParts();
    }

    public double calculateTotalInventoryValue() {
        return repository.calculateTotalInventoryValue();
    }

    public boolean isLowStock(
            SparePart sparePart,
            int threshold
    ) {
        return sparePart.isLowStock(threshold);
    }

    public void clearInventory() {
        repository.clearRepository();
    }
}
