package ro.iteahome.shop.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order extends WritableToDatabase {

    //order ID is initialized by the WritableToDatabase class as -1 by default;
    private String confirmationDate;
    private int userID;
    private LinkedHashMap<Product, Integer> products;
    private float totalPrice;
    private String currency = Product.CURRENCY;
    private PaymentMethod paymentMethod;
    private Status currentStatus;

    public enum PaymentMethod { //TODO: include this in payment logic.
        ON_DELIVERY,
        ONLINE
    }

    public enum Status {
        NOT_DELIVERED,
        DELIVERED,
        CANCELLED
    }

    public String productsToEncodedString() {
        StringBuilder productsBuilder = new StringBuilder();
        for (Map.Entry<Product, Integer> pair : products.entrySet()) {
            productsBuilder.append(pair.getKey().getID()).append("-").append(pair.getValue()).append("_");
        }
        return productsBuilder.substring(0, productsBuilder.length() - 1);
    }

    @Override
    public String toDatabaseString() {
        return ID + "|" + confirmationDate + "|" + userID + "|" + productsToEncodedString() + "|" + totalPrice + "|" + currency + "|" + paymentMethod + "|" + currentStatus;
    }

    /**
     * Constructor for adding new Orders to the database:
     */
    public Order(int userID, LinkedHashMap<Product, Integer> products, float totalPrice, PaymentMethod paymentMethod) {
        //order ID is updated automatically upon writing to database.
        this.confirmationDate = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(new Date());
        this.userID = userID;
        this.products = products;
        this.totalPrice = totalPrice;
        //currency is read from the Product class;
        this.paymentMethod = paymentMethod;
        this.currentStatus = Status.NOT_DELIVERED;
    }

    /**
     * Constructor reading/updating Orders in the database:
     */
    public Order(int ID, String confirmationDate, int userID, LinkedHashMap<Product, Integer> products, float totalPrice, PaymentMethod paymentMethod, Status currentStatus) {
        this.ID = ID;
        this.confirmationDate = confirmationDate;
        this.userID = userID;
        this.products = products;
        this.totalPrice = totalPrice;
        //currency is read from the Product class;
        this.paymentMethod = paymentMethod;
        this.currentStatus = currentStatus;
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public int getUserID() {
        return userID;
    }

    public LinkedHashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(LinkedHashMap<Product, Integer> products) {
        this.products = products;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }
}