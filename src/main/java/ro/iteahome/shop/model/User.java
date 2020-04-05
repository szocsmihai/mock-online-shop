package ro.iteahome.shop.model;

public class User extends WritableToDatabase {

    //user ID is initialized by the WritableToDatabase class as -1 by default;
    private Role currentRole;
    private String email;
    private String password;
    private String fullName = "-";
    private String phoneNo = "-";
    private String deliveryAddress = "-";

    public enum Role {
        ADMIN,
        SHOPPER
    }

    @Override
    public String toDatabaseString() {
        return ID + "|" + currentRole + "|" + email + "|" + password + "|" + fullName + "|" + phoneNo + "|" + deliveryAddress;
    }

    /**
     * Constructor for adding new Users to the database:
     */
    public User(String email, String password) {
        //user ID is updated automatically upon writing to database.
        this.currentRole = Role.SHOPPER;
        this.email = email;
        this.password = password;
        //fullName is initialized by this class as "-" by default;
        //phoneNo is initialized by this class as "-" by default;
        //deliveryAddress is initialized by this class as "-" by default;
    }

    /**
     * Constructor reading/updating Users in the database:
     */
    public User(int ID, Role currentRole, String email, String password, String fullName, String phoneNo, String deliveryAddress) {
        this.ID = ID;
        this.currentRole = currentRole;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.deliveryAddress = deliveryAddress;
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}