package lk.vinuth.malabesparepartssystem.service;

import lk.vinuth.malabesparepartssystem.model.Dealer;
import lk.vinuth.malabesparepartssystem.model.SparePart;
import lk.vinuth.malabesparepartssystem.repository.DealerRepository;
import lk.vinuth.malabesparepartssystem.util.LegacyFileLoader;

import java.util.List;

public class ApplicationDataService {

    private static final String INVENTORY_FILE = "inventory_legacy.txt";
    private static final String DEALER_FILE = "dealers_legacy.txt";

    private final LegacyFileLoader fileLoader;

    private final InventoryService inventoryService;

    private final DealerRepository dealerRepository;

    private final DealerService dealerService;

    public ApplicationDataService() {

        fileLoader = new LegacyFileLoader();

        inventoryService = new InventoryService();

        dealerRepository = new DealerRepository();

        dealerService = new DealerService(dealerRepository);
    }

    public void loadLegacyData() {

        List<SparePart> spareParts =
                fileLoader.loadInventory(INVENTORY_FILE);

        for (SparePart sparePart : spareParts) {
            inventoryService.addSparePart(sparePart);
        }

        List<Dealer> dealers =
                fileLoader.loadDealers(DEALER_FILE);

        for (Dealer dealer : dealers) {
            dealerService.addDealer(dealer);
        }
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public DealerService getDealerService() {
        return dealerService;
    }
}