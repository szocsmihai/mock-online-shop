package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Order;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.service.ProductService;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Function;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class OrderDAO {

    /**
     * Every DAO class stores the paths ({@code String}) to its corresponding database and sequence .txt files and keeps
     * a reference to the {@code FileUtil} class, to access its methods.
     */
    final String DATABASE_PATH = "src/main/resources/orders.txt";
    final String DATABASE_SEQUENCE_PATH = "src/main/resources/orders_sequence.txt";

    private ProductService productService = new ProductService();
    private FileUtil<Order> fileUtil = new FileUtil<>();

    /**
     * In order to convert lines of text to Order objects, a {@code constructOrder} lambda function is defined.
     */
    Function<String[], Order> constructOrder = (line) -> {
        int orderID = parseInt(line[0]);
        String confirmationDate = line[1];
        int userID = parseInt(line[2]);
        LinkedHashMap<Product, Integer> products = encodedProductsStringToMap(line[3]);
        float totalPrice = parseFloat(line[4]);
        //currency is read by the Order class from the Product class;
        Order.PaymentMethod paymentMethod = stringToOrderPaymentMethodEnum(line[6]);
        Order.Status currentStatus = stringToOrderStatusEnum(line[7]);

        return new Order(orderID, confirmationDate, userID, products, totalPrice, paymentMethod, currentStatus);
    };

    /**
     * Methods that write to the Order database:
     */

    public void addNewOrder(Order order) {
        try {

            order.setID(fileUtil.getSequence(DATABASE_SEQUENCE_PATH) + 1);
            fileUtil.addNewEntry(DATABASE_PATH, DATABASE_SEQUENCE_PATH, order);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert(e.getMessage());
        }
    }

    public void updateOrder(Order oldOrder, Order newOrder) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        fileUtil.updateEntry(DATABASE_PATH, oldOrder, newOrder, constructOrder);
    }

    public void removeOrder(Order targetOrder) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        fileUtil.removeEntry(DATABASE_PATH, targetOrder, constructOrder);
    }

    /**
     * Methods that read the Order database:
     * <p>
     * {@code getOrderProductsAndQuantities()} converts a specific piece of text from the database line into a {@code HashMap} of
     * {@code Products} and quantities. Every Order contains a collection of {@code Products} and their corresponding
     * quantities. Within the database .txt file, this information is condensed into a string containing product IDs,
     * product quantities and specific markers for separating this information.
     */

    public ArrayList<Order> getAllOrders() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.getAllEntries(DATABASE_PATH, constructOrder);
    }

    public Order findOrderByID(int ID) {
        Order order = null;
        try {

            order = fileUtil.findFirstEntryByProperty(DATABASE_PATH, 0, String.valueOf(ID), constructOrder);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printConfirmation("ORDER NOT FOUND.");
        }
        return order;
    }

    public ArrayList<Order> findOrdersByUserID(int ID) {
        ArrayList<Order> userOrders = new ArrayList<>();
        try {

            userOrders = fileUtil.findEntriesByProperty(DATABASE_PATH, 2, String.valueOf(ID), constructOrder);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printConfirmation("NO ORDERS HAVE BEEN CONFIRMED BY CURRENT USER.");
        }
        return userOrders;
    }

    public LinkedHashMap<Product, Integer> encodedProductsStringToMap(String encodedString) {
        LinkedHashMap<Product, Integer> orderProductsAndQuantities = new LinkedHashMap<>();
        String[] productProperties = encodedString.split("_");
        for (String pair : productProperties) {
            String[] productIDAndQuantity = pair.split("-");
            orderProductsAndQuantities.put(productService.findProductByID(parseInt(productIDAndQuantity[0])), parseInt(productIDAndQuantity[1]));
        }
        return orderProductsAndQuantities;
    }

    public LinkedHashMap<Product, Integer> databaseLineToProductMap(String line) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        LinkedHashMap<Product, Integer> orderProductsAndQuantities = new LinkedHashMap<>();
        String[] orderProperties = line.split("|");
        String encodedProductsString = orderProperties[3];
        String[] productProperties = encodedProductsString.split("_");
        for (String pair : productProperties) {
            String[] productIDAndQuantity = pair.split("-");
            orderProductsAndQuantities.put(productService.findProductByID(parseInt(productIDAndQuantity[0])), parseInt(productIDAndQuantity[1]));
        }
        return orderProductsAndQuantities;
    }

    private Order.PaymentMethod stringToOrderPaymentMethodEnum(String string) {
        Order.PaymentMethod paymentMethod = null;
        switch (string) {
            case "ON_DELIVERY":
                paymentMethod = Order.PaymentMethod.ON_DELIVERY;
                break;
            case "ONLINE":
                paymentMethod = Order.PaymentMethod.ONLINE;
                break;
            default:
                OutputFrame.printAlert("INVALID INPUT");
        }
        return paymentMethod;
    }

    private Order.Status stringToOrderStatusEnum(String string) {
        Order.Status currentStatus = null;
        switch (string) {
            case "NOT_DELIVERED":
                currentStatus = Order.Status.NOT_DELIVERED;
                break;
            case "DELIVERED":
                currentStatus = Order.Status.DELIVERED;
                break;
            case "CANCELLED":
                currentStatus = Order.Status.CANCELLED;
                break;
            default:
                OutputFrame.printAlert("INVALID INPUT");
        }
        return currentStatus;
    }
}