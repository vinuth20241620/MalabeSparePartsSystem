package lk.vinuth.malabesparepartssystem.repository;

import lk.vinuth.malabesparepartssystem.model.Dealer;

import java.util.ArrayList;
import java.util.List;

/**
 * DealerRepository
 *
 * This class stores and manages all Dealer objects
 * used by the application.
 *
 * Think of this class as the dealer database.
 * Every dealer loaded from the legacy text file
 * will be stored inside an ArrayList.
 */
public class DealerRepository {

    /*
     * Stores every dealer currently available.
     */
    private final List<Dealer> dealers = new ArrayList<>();
    /**
     * Creates an empty DealerRepository.
     *
     * The ArrayList has already been created,
     * so nothing else is required here.
     */
    public DealerRepository() {

        // Start with an empty dealer list.
    }

    /**
     * Returns every dealer currently stored.
     *
     * A copy of the ArrayList is returned to
     * protect the original dealer list.
     *
     * @return a copy of all dealers
     */
    public List<Dealer> getAllDealers() {
        return new ArrayList<>(dealers);
    }

    /**
     * Finds a dealer using its unique dealer ID.
     *
     * @param dealerId dealer ID to search for
     * @return Dealer object if found, otherwise null
     */
    public Dealer findByDealerId(String dealerId) {

        // Check every dealer in the repository.
        for (Dealer dealer : dealers) {

            // Compare dealer IDs without considering upper/lower case.
            if (dealer.getDealerId().equalsIgnoreCase(dealerId)) {
                return dealer;
            }
        }

        // Dealer was not found.
        return null;
    }

    /**
     * Checks whether a dealer ID already exists.
     *
     * @param dealerId dealer ID to check
     * @return true if found, otherwise false
     */
    public boolean containsDealerId(String dealerId) {
        return findByDealerId(dealerId) != null;
    }
    /**
     * Adds a new dealer to the repository.
     *
     * @param dealer the Dealer object to add
     */
    public void addDealer(Dealer dealer) {

        // Add the dealer object to the ArrayList.
        dealers.add(dealer);
    }

    /**
     * Removes a dealer object from the repository.
     *
     * @param dealer the Dealer object to remove
     * @return true if the dealer was removed, otherwise false
     */
    public boolean removeDealer(Dealer dealer) {

        // remove() returns true when the dealer existed in the list.
        return dealers.remove(dealer);
    }

    /**
     * Removes a dealer using the dealer ID.
     *
     * @param dealerId the ID of the dealer to remove
     * @return true if the dealer was removed, otherwise false
     */
    public boolean removeByDealerId(String dealerId) {

        // Search for the dealer first.
        Dealer dealer = findByDealerId(dealerId);

        // Remove the dealer only when a match was found.
        if (dealer != null) {
            return dealers.remove(dealer);
        }

        // Return false when no matching dealer exists.
        return false;
    }

    /**
     * Returns the number of dealers stored in the repository.
     *
     * @return total number of dealers
     */
    public int getTotalDealers() {
        return dealers.size();
    }

    /**
     * Removes every dealer from the repository.
     *
     * This only clears the ArrayList in memory.
     * It does not delete dealers_legacy.txt.
     */
    public void clearRepository() {
        dealers.clear();
    }
    /**
     * Updates an existing dealer.
     *
     * The method searches for the original dealer using
     * the dealer ID, then copies the new values into it.
     *
     * @param dealerId the ID of the dealer to update
     * @param updatedDealer the object containing the new details
     * @return true if updated successfully, otherwise false
     */
    public boolean updateDealer(
            String dealerId,
            Dealer updatedDealer
    ) {

        // Find the existing dealer first.
        Dealer existingDealer = findByDealerId(dealerId);

        // Stop if no dealer was found.
        if (existingDealer == null) {
            return false;
        }

        // Copy the updated values into the existing object.
        existingDealer.setDealerId(updatedDealer.getDealerId());
        existingDealer.setDealerName(updatedDealer.getDealerName());
        existingDealer.setPhoneNumber(updatedDealer.getPhoneNumber());
        existingDealer.setEmail(updatedDealer.getEmail());
        existingDealer.setAddress(updatedDealer.getAddress());

        // Confirm that the update was successful.
        return true;
    }
}