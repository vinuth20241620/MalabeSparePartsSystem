package lk.vinuth.malabesparepartssystem.service;

import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.repository.SparePartRepository;

import java.util.List;

/**
 * InventoryService
 *
 * This class acts as the business layer
 * between the JavaFX user interface
 * and the SparePartRepository.
 *
 * The service performs inventory operations
 * while the repository stores the data.
 */
public class InventoryService {

    /*
     * Repository used to store
     * every SparePart object.
     */
    private final SparePartRepository repository =
            new SparePartRepository();
    /**
     * Returns every spare part currently stored.
     *
     * @return list containing all spare parts
     */
    public List<SparePart> getAllSpareParts() {
        return repository.getAllSpareParts();
    }

    /**
     * Finds one spare part using its unique code.
     *
     * @param partCode code to search for
     * @return matching SparePart, or null if not found
     */
    public SparePart findSparePart(String partCode) {
        return repository.findByPartCode(partCode);
    }

    /**
     * Adds a new spare part after checking that
     * another part with the same code does not already exist.
     *
     * @param sparePart spare part to add
     * @return true if added successfully, otherwise false
     */
    public boolean addSparePart(SparePart sparePart) {

        // Reject the new part if its code already exists.
        if (repository.containsPartCode(sparePart.getPartCode())) {
            return false;
        }

        // Add the part when the code is unique.
        repository.addSparePart(sparePart);
        return true;
    }

    /**
     * Deletes a spare part using its unique part code.
     *
     * @param partCode code of the part to delete
     * @return true if deleted, otherwise false
     */
    public boolean deleteSparePart(String partCode) {
        return repository.removeByPartCode(partCode);
    }
    /**
     * Updates an existing spare part.
     *
     * @param originalPartCode code of the part being updated
     * @param updatedPart object containing the new information
     * @return true if updated successfully, otherwise false
     */
    public boolean updateSparePart(
            String originalPartCode,
            SparePart updatedPart
    ) {

        // If the part code is being changed, make sure
        // the new code is not already used by another part.
        if (!originalPartCode.equalsIgnoreCase(updatedPart.getPartCode())
                && repository.containsPartCode(updatedPart.getPartCode())) {

            return false;
        }

        // Ask the repository to update the stored object.
        return repository.updateSparePart(originalPartCode, updatedPart);
    }

    /**
     * Returns the number of spare-part records
     * currently stored in the inventory.
     *
     * @return number of spare-part records
     */
    public int getTotalParts() {
        return repository.getTotalParts();
    }

    /**
     * Calculates the total value of all stock.
     *
     * @return complete inventory value
     */
    public double calculateTotalInventoryValue() {
        return repository.calculateTotalInventoryValue();
    }

    /**
     * Checks whether a spare part is below
     * the selected low-stock threshold.
     *
     * @param sparePart spare part to check
     * @param threshold minimum acceptable quantity
     * @return true if the quantity is below the threshold
     */
    public boolean isLowStock(
            SparePart sparePart,
            int threshold
    ) {
        return sparePart.isLowStock(threshold);
    }

    /**
     * Removes all spare parts from memory.
     *
     * This does not delete the original text file.
     */
    public void clearInventory() {
        repository.clearRepository();
    }
}
