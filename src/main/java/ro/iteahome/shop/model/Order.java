package ro.iteahome.shop.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Order extends WritableToDatabase {

    //order ID is initialized by the WritableToDatabase class as -1 by default;
    private String confirmationDate;
    private int userID;
    private HashMap<Product, Integer> products;
    private float totalPrice;
    private String currency = Product.CURRENCY;
    private status currentStatus;

    public enum status {
        NOT_DELIVERED,
        DELIVERED
    }

    private String productsToString() {
        StringBuilder productsBuilder = new StringBuilder();
        for (Map.Entry<Product, Integer> pair : products.entrySet()) {
            productsBuilder.append(pair.getKey().getID()).append("-").append(pair.getValue()).append("_");
        }
        return productsBuilder.substring(0, productsBuilder.length() - 1);
    }

    @Override
    public String toDatabaseString() {
        return ID + "|" + confirmationDate + "|" + userID + "|" + productsToString() + "|" + totalPrice + "|" + currency + "|" + currentStatus;
    }

    /**
     * Constructor for adding new Orders to the database:
     */
    public Order(int userID, HashMap<Product, Integer> products, float totalPrice) {
        //order ID is updated automatically upon writing to database.
        this.confirmationDate = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss").format(new Date());
        this.userID = userID;
        this.products = products;
        this.totalPrice = totalPrice;
        //currency is read from the Product class;
        this.currentStatus = status.NOT_DELIVERED;
    }

    /**
     * Constructor reading/updating Orders in the database:
     */
    public Order(int ID, String confirmationDate, int userID, HashMap<Product, Integer> products, float totalPrice, status currentStatus) {
        this.ID = ID;
        this.confirmationDate = confirmationDate;
        this.userID = userID;
        this.products = products;
        this.totalPrice = totalPrice;
        //currency is read from the Product class;
        this.currentStatus = currentStatus;
    }

    public String getProductsAsString() {
        return productsToString();
    }

    public String getConfirmationDate() {
        return confirmationDate;
    }

    public int getUserID() {
        return userID;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Product, Integer> products) {
        this.products = products;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(status currentStatus) {
        this.currentStatus = currentStatus;
    }
}