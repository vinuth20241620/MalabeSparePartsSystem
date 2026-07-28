package lk.vinuth.malabesparepartssystem.repository;

import lk.vinuth.malabesparepartssystem.model.Dealer;

import java.util.ArrayList;
import java.util.List;

public class DealerRepository {

    private final List<Dealer> dealers = new ArrayList<>();

    public DealerRepository() {

    }

    public List<Dealer> getAllDealers() {
        return new ArrayList<>(dealers);
    }

    public Dealer findByDealerId(String dealerId) {

        for (Dealer dealer : dealers) {

            if (dealer.getDealerId().equalsIgnoreCase(dealerId)) {
                return dealer;
            }
        }

        return null;
    }

    public boolean containsDealerId(String dealerId) {
        return findByDealerId(dealerId) != null;
    }

    public void addDealer(Dealer dealer) {

        dealers.add(dealer);
    }

    public boolean removeDealer(Dealer dealer) {

        return dealers.remove(dealer);
    }

    public boolean removeByDealerId(String dealerId) {

        Dealer dealer = findByDealerId(dealerId);

        if (dealer != null) {
            return dealers.remove(dealer);
        }

        return false;
    }

    public int getTotalDealers() {
        return dealers.size();
    }

    public void clearRepository() {
        dealers.clear();
    }

    public boolean updateDealer(
            String dealerId,
            Dealer updatedDealer
    ) {

        Dealer existingDealer = findByDealerId(dealerId);

        if (existingDealer == null) {
            return false;
        }

        existingDealer.setDealerId(updatedDealer.getDealerId());
        existingDealer.setDealerName(updatedDealer.getDealerName());
        existingDealer.setPhoneNumber(updatedDealer.getPhoneNumber());
        existingDealer.setEmail(updatedDealer.getEmail());
        existingDealer.setAddress(updatedDealer.getAddress());

        return true;
    }
}