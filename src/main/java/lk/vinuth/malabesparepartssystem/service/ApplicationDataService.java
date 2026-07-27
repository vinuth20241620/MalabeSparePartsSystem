package lk.vinuth.malabesparepartssystem.service;

import lk.vinuth.malabesparepartssystem.model.Dealer;
import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.repository.DealerRepository;
import lk.vinuth.malabesparepartssystem.util.LegacyFileLoader;

import java.util.List;

/**
 * ApplicationDataService
 *
 * This class loads the supplied legacy text files
 * and places the converted objects into the services.
 *
 * It also provides one central place where controllers
 * can access inventory and dealer information.
 */
public class ApplicationDataService {

    // File paths for the two supplied legacy data files.
    private static final String INVENTORY_FILE = "inventory_legacy.txt";
    private static final String DEALER_FILE = "dealers_legacy.txt";

    // Utility responsible for reading and parsing text files.
    private final LegacyFileLoader fileLoader;

    // Service responsible for inventory operations.
    private final InventoryService inventoryService;

    // Repository used by DealerService to store dealer objects.
    private final DealerRepository dealerRepository;

    // Service responsible for dealer operations.
    private final DealerService dealerService;
    /**
     * Creates the application services and repositories.
     *
     * Everything is connected together here so that the
     * rest of the application only needs one object.
     */
    public ApplicationDataService() {

        // Create the file loader.
        fileLoader = new LegacyFileLoader();

        // Create the inventory service.
        inventoryService = new InventoryService();

        // Create the dealer repository.
        dealerRepository = new DealerRepository();

        // Create the dealer service using the repository.
        dealerService = new DealerService(dealerRepository);
    }

    /**
     * Loads both legacy text files into memory.
     *
     * Inventory data is loaded into InventoryService.
     * Dealer data is loaded into DealerService.
     */
    public void loadLegacyData() {

        // Read every spare part from the inventory file.
        List<SparePart> spareParts =
                fileLoader.loadInventory(INVENTORY_FILE);

        // Add every spare part into the inventory service.
        for (SparePart sparePart : spareParts) {
            inventoryService.addSparePart(sparePart);
        }

        // Read every dealer from the dealer file.
        List<Dealer> dealers =
                fileLoader.loadDealers(DEALER_FILE);

        // Add every dealer into the dealer service.
        for (Dealer dealer : dealers) {
            dealerService.addDealer(dealer);
        }
    }
    /**
     * Returns the inventory service.
     *
     * Controllers use this method to access
     * inventory operations and inventory data.
     *
     * @return inventory service object
     */
    public InventoryService getInventoryService() {
        return inventoryService;
    }

    /**
     * Returns the dealer service.
     *
     * Controllers use this method to access
     * dealer operations and dealer data.
     *
     * @return dealer service object
     */
    public DealerService getDealerService() {
        return dealerService;
    }
}