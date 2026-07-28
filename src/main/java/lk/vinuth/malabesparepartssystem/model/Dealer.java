package lk.vinuth.malabesparepartssystem.model;

public class Dealer {

    private String dealerId;

    private String dealerName;

    private String phoneNumber;

    private String email;

    private String address;
    public Dealer() {

        // Empty constructor
    }

    public Dealer(
            String dealerId,
            String dealerName,
            String phoneNumber,
            String email,
            String address
    ) {

        this.dealerId = dealerId;
        this.dealerName = dealerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public String getDealerId() {
        return dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {

        return "Dealer{" +
                "dealerId='" + dealerId + '\'' +
                ", dealerName='" + dealerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}