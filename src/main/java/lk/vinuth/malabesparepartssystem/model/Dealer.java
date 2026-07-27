package lk.vinuth.malabesparepartssystem.model;

/**
 *
 * Dealer Class
 *
 * This class represents one spare part dealer.
 *
 * Every Dealer object stores information about one dealer.
 *
 * When the program reads dealers_legacy.txt,
 * each line will become one Dealer object.
 *
 */
public class Dealer {

    // Unique dealer ID (Example: D001)
    private String dealerId;

    // Dealer or company name
    private String dealerName;

    // Contact phone number
    private String phoneNumber;

    // Dealer email address
    private String email;

    // Dealer address
    private String address;
    /**
     * Creates an empty Dealer object.
     *
     * This constructor is useful when values
     * will be entered later.
     */
    public Dealer() {

        // Empty constructor
    }

    /**
     * Creates a Dealer object with all information.
     *
     * @param dealerId unique dealer ID
     * @param dealerName dealer name
     * @param phoneNumber dealer phone number
     * @param email dealer email address
     * @param address dealer address
     */
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
    // ============================
    // Getter Methods
    // ============================

    /**
     * Returns the dealer ID.
     *
     * @return dealer ID
     */
    public String getDealerId() {
        return dealerId;
    }

    /**
     * Returns the dealer name.
     *
     * @return dealer name
     */
    public String getDealerName() {
        return dealerName;
    }

    /**
     * Returns the phone number.
     *
     * @return dealer phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the email address.
     *
     * @return dealer email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the dealer address.
     *
     * @return dealer address
     */
    public String getAddress() {
        return address;
    }
    // ============================
    // Setter Methods
    // ============================

    /**
     * Sets the dealer ID.
     *
     * @param dealerId new dealer ID
     */
    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    /**
     * Sets the dealer name.
     *
     * @param dealerName new dealer name
     */
    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    /**
     * Sets the phone number.
     *
     * @param phoneNumber new phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the email address.
     *
     * @param email new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the dealer address.
     *
     * @param address new dealer address
     */
    public void setAddress(String address) {
        this.address = address;
    }
    // ============================
    // Utility Methods
    // ============================

    /**
     * Returns the dealer object as readable text.
     *
     * This is useful for debugging and testing.
     */
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