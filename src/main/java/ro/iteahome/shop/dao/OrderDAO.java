package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Order;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.service.ProductService;

import java.util.HashMap;
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
        Order order = null;
        try {

            int orderID = parseInt(line[0]);
            String confirmationDate = line[1];
            int userID = parseInt(line[2]);
            HashMap<Product, Integer> products = getOrderProductsAndQuantities(line[3]);
            float totalPrice = parseFloat(line[4]);
            //currency is read by the Order class from the Product class;
            Order.status currentStatus = mapStringToStatusEnum(line[6]);

            order = new Order(orderID, confirmationDate, userID, products, totalPrice, currentStatus);
        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
        return order;
    };

    /**
     * Methods that write to the Order database:
     */

    public void addNewOrder(Order order) throws ShopFileNotFoundException {
        order.setID(fileUtil.getSequence(DATABASE_SEQUENCE_PATH) + 1);
        fileUtil.addNewEntry(DATABASE_PATH, DATABASE_SEQUENCE_PATH, order);
    }

    public void updateOrder(Order oldOrder, Order newOrder) {
        //TODO: develop this for admins.
    }

    public void removeOrder(Order order) {
        //TODO: develop this for admins.
    }

    /**
     * Methods that read the Order database:
     * <p>
     * {@code getOrderProductsAndQuantities()} converts a specific piece of text from the database line into a {@code HashMap} of
     * {@code Products} and quantities. Every Order contains a collection of {@code Products} and their corresponding
     * quantities. Within the database .txt file, this information is condensed into a string containing product IDs,
     * product quantities and specific markers for separating this information.
     */
    private HashMap<Product, Integer> getOrderProductsAndQuantities(String line) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        HashMap<Product, Integer> orderProductsAndQuantities = new HashMap<>();
        String[] orderProperties = line.split("|");
        String productsToken = orderProperties[3];
        String[] productProperties = productsToken.split("_");
        for (String pair : productProperties) {
            String[] productIDAndQuantity = pair.split("-");
            orderProductsAndQuantities.put(productService.findProductByID(parseInt(productIDAndQuantity[0])), parseInt(productIDAndQuantity[1]));
        }
        return orderProductsAndQuantities;
    }

    private Order.status mapStringToStatusEnum(String string) {
        Order.status currentStatus;
        if (string.equals(Order.status.NOT_DELIVERED.toString())) {
            currentStatus = Order.status.NOT_DELIVERED;
        } else {
            currentStatus = Order.status.DELIVERED;
        }
        return currentStatus;
    }
}