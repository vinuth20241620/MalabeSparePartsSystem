package lk.vinuth.malabesparepartssystem.repository;

import lk.vinuth.malabesparepartssystem.model.SparePart;

import java.util.ArrayList;
import java.util.List;

/**
 * SparePartRepository
 *
 * This class is responsible for storing and managing
 * every SparePart object used in the application.
 *
 * Think of this class as the inventory database.
 * Every spare part is stored inside an ArrayList.
 *
 * Later this repository will also read data from
 * inventory_legacy.txt.
 */
public class SparePartRepository {

    /*
     * This ArrayList stores every spare part
     * currently loaded into memory.
     */
    private final List<SparePart> spareParts = new ArrayList<>();
    /**
     * Creates an empty SparePartRepository.
     *
     * The ArrayList has already been created above,
     * so this constructor does not need additional code.
     */
    public SparePartRepository() {
        // The repository starts with an empty inventory list.
    }

    /**
     * Returns every spare part currently stored in the repository.
     *
     * A new ArrayList is returned so that another class cannot
     * directly replace or damage the repository's original list.
     *
     * @return a copy of the complete spare-parts list
     */
    public List<SparePart> getAllSpareParts() {
        return new ArrayList<>(spareParts);
    }

    /**
     * Finds a spare part by its unique part code.
     *
     * The method manually checks each object using a loop.
     * It does not use Java Streams or lambda expressions.
     *
     * @param partCode the code to search for, such as P001
     * @return the matching SparePart, or null when no match exists
     */
    public SparePart findByPartCode(String partCode) {

        // Check every spare part currently stored in the list.
        for (SparePart sparePart : spareParts) {

            // Ignore letter-case differences when comparing codes.
            if (sparePart.getPartCode().equalsIgnoreCase(partCode)) {
                return sparePart;
            }
        }

        // Return null when the requested code was not found.
        return null;
    }

    /**
     * Checks whether a particular part code already exists.
     *
     * @param partCode the code that must be checked
     * @return true if the code exists, otherwise false
     */
    public boolean containsPartCode(String partCode) {
        return findByPartCode(partCode) != null;
    }
    /**
     * Adds a new spare part to the repository.
     *
     * @param sparePart the spare part to add
     */
    public void addSparePart(SparePart sparePart) {

        // Add the object into the ArrayList.
        spareParts.add(sparePart);
    }

    /**
     * Removes a spare part from the repository.
     *
     * @param sparePart the object that should be removed
     * @return true if removed successfully, otherwise false
     */
    public boolean removeSparePart(SparePart sparePart) {

        // ArrayList.remove() returns true if the item existed.
        return spareParts.remove(sparePart);
    }

    /**
     * Removes a spare part using its unique part code.
     *
     * @param partCode the part code to remove
     * @return true if removed successfully
     */
    public boolean removeByPartCode(String partCode) {

        // First locate the spare part.
        SparePart sparePart = findByPartCode(partCode);

        // If found, remove it.
        if (sparePart != null) {
            return spareParts.remove(sparePart);
        }

        // Nothing was removed.
        return false;
    }

    /**
     * Returns the total number of spare parts
     * currently stored in the repository.
     *
     * @return inventory size
     */
    public int getTotalParts() {
        return spareParts.size();
    }

    /**
     * Removes every spare part from memory.
     *
     * This does not delete the legacy text file.
     * It only clears the ArrayList.
     */
    public void clearRepository() {
        spareParts.clear();
    }
    /**
     * Updates an existing spare part.
     *
     * The method first searches for the original spare part
     * using its unique part code.
     *
     * @param partCode the code of the part that must be updated
     * @param updatedPart the object containing the new information
     * @return true if the part was updated, otherwise false
     */
    public boolean updateSparePart(
            String partCode,
            SparePart updatedPart
    ) {

        // Find the existing spare part using its code.
        SparePart existingPart = findByPartCode(partCode);

        // If no matching part exists, the update cannot continue.
        if (existingPart == null) {
            return false;
        }

        // Copy the new values into the existing object.
        existingPart.setPartCode(updatedPart.getPartCode());
        existingPart.setPartName(updatedPart.getPartName());
        existingPart.setBrand(updatedPart.getBrand());
        existingPart.setPrice(updatedPart.getPrice());
        existingPart.setQuantity(updatedPart.getQuantity());
        existingPart.setCategory(updatedPart.getCategory());
        existingPart.setDateAdded(updatedPart.getDateAdded());
        existingPart.setImageFileName(updatedPart.getImageFileName());

        // Return true to show that the update was successful.
        return true;
    }

    /**
     * Calculates the total monetary value of the inventory.
     *
     * The method manually visits every spare part and adds
     * its stock value to the running total.
     *
     * @return total inventory value
     */
    public double calculateTotalInventoryValue() {

        // Start the total at zero.
        double totalValue = 0.0;

        // Go through every spare part stored in the repository.
        for (SparePart sparePart : spareParts) {

            // Add this part's stock value to the total.
            totalValue += sparePart.calculateStockValue();
        }

        // Return the final inventory value.
        return totalValue;
    }
}