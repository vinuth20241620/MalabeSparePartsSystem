package lk.vinuth.malabesparepartssystem.service;

import java.util.List;

import lk.vinuth.malabesparepartssystem.model.Dealer;
import lk.vinuth.malabesparepartssystem.repository.DealerRepository;

/**
 * DealerService
 *
 * This class contains the business logic
 * related to dealer management.
 *
 * It sits between the user interface
 * and the DealerRepository.
 *
 * The service validates data before
 * passing it to the repository.
 */
public class DealerService {

    // Repository responsible for storing dealers.
    private final DealerRepository repository;

    /**
     * Creates the service.
     *
     * @param repository dealer repository
     */
    public DealerService(DealerRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns every dealer.
     *
     * @return list of dealers
     */
    public List<Dealer> getAllDealers() {
        return repository.getAllDealers();
    }
    /**
     * Finds one dealer using the dealer ID.
     *
     * @param dealerId ID to search for
     * @return matching Dealer object, or null if not found
     */
    public Dealer findDealer(String dealerId) {
        return repository.findByDealerId(dealerId);
    }

    /**
     * Adds a new dealer after checking that
     * the dealer ID is not already being used.
     *
     * @param dealer dealer object to add
     * @return true if added successfully, otherwise false
     */
    public boolean addDealer(Dealer dealer) {

        // Prevent two dealers from having the same ID.
        if (repository.containsDealerId(dealer.getDealerId())) {
            return false;
        }

        // Add the dealer when its ID is unique.
        repository.addDealer(dealer);
        return true;
    }

    /**
     * Deletes a dealer using the dealer ID.
     *
     * @param dealerId ID of the dealer to delete
     * @return true if deleted successfully, otherwise false
     */
    public boolean deleteDealer(String dealerId) {
        return repository.removeByDealerId(dealerId);
    }
    /**
     * Updates an existing dealer.
     *
     * @param originalDealerId ID of the dealer being updated
     * @param updatedDealer object containing the new information
     * @return true if updated successfully, otherwise false
     */
    public boolean updateDealer(
            String originalDealerId,
            Dealer updatedDealer
    ) {

        // If the ID is being changed, make sure the new ID
        // is not already used by another dealer.
        if (!originalDealerId.equalsIgnoreCase(updatedDealer.getDealerId())
                && repository.containsDealerId(updatedDealer.getDealerId())) {

            return false;
        }

        // Ask the repository to update the stored dealer.
        return repository.updateDealer(originalDealerId, updatedDealer);
    }

    /**
     * Returns the total number of dealers.
     *
     * @return number of dealer records
     */
    public int getTotalDealers() {
        return repository.getTotalDealers();
    }

    /**
     * Removes every dealer from memory.
     *
     * This does not delete the original legacy text file.
     */
    public void clearDealers() {
        repository.clearRepository();
    }
}
