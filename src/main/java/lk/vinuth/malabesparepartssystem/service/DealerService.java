package lk.vinuth.malabesparepartssystem.service;

import java.util.List;

import lk.vinuth.malabesparepartssystem.model.Dealer;
import lk.vinuth.malabesparepartssystem.repository.DealerRepository;

public class DealerService {

    private final DealerRepository repository;

    public DealerService(DealerRepository repository) {
        this.repository = repository;
    }

    public List<Dealer> getAllDealers() {
        return repository.getAllDealers();
    }

    public Dealer findDealer(String dealerId) {
        return repository.findByDealerId(dealerId);
    }

    public boolean addDealer(Dealer dealer) {

        if (repository.containsDealerId(dealer.getDealerId())) {
            return false;
        }

        repository.addDealer(dealer);
        return true;
    }

    public boolean deleteDealer(String dealerId) {
        return repository.removeByDealerId(dealerId);
    }

    public boolean updateDealer(
            String originalDealerId,
            Dealer updatedDealer
    ) {

        if (!originalDealerId.equalsIgnoreCase(updatedDealer.getDealerId())
                && repository.containsDealerId(updatedDealer.getDealerId())) {

            return false;
        }

        return repository.updateDealer(originalDealerId, updatedDealer);
    }

    public int getTotalDealers() {
        return repository.getTotalDealers();
    }


    public void clearDealers() {
        repository.clearRepository();
    }
}
