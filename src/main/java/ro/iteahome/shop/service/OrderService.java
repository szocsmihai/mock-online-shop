package ro.iteahome.shop.service;

import ro.iteahome.shop.dao.OrderDAO;
import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Order;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrderService {

    /**
     * This class has 2 main responsibilities:
     * <p>
     * - Takes Strings and primitives from the different UIs, converts them to {@code Order} objects and passes them to
     * the corresponding DAO class;
     * - Returns order information via its corresponding DAO class;
     */
    private OrderDAO orderDAO = new OrderDAO();
    private ProductService productService = new ProductService();

    public void addNewOrder(int userID, LinkedHashMap<Product, Integer> products, float totalPrice, Order.PaymentMethod paymentMethod) {
        orderDAO.addNewOrder(new Order(userID, products, totalPrice, paymentMethod));
    }

    public void cancelOrder() {
        //TODO: Develop this. Update stock and sold units.
    }

    public ArrayList<Order> getAllOrders() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return orderDAO.getAllOrders();
    }

    public Order findOrderByID(int ID) {
        return orderDAO.findOrderByID(ID);
    }

    public ArrayList<Order> findOrdersByUserID(int ID) {
        return orderDAO.findOrdersByUserID(ID);
    }

    public void outputOrderList(ArrayList<Order> orders) {
        for (Order order : orders) {
            String encodedString = order.productsToEncodedString();
            String productList = encodedProductsStringToInfoString(encodedString);
            ArrayList<String> orderProperties = new ArrayList<>();

            orderProperties.add("DATE:       " + order.getConfirmationDate());
            orderProperties.add("PRODUCTS:");
            orderProperties.add(productList);
            orderProperties.add("PRICE:      " + order.getTotalPrice());
            orderProperties.add("STATUS:     " + order.getCurrentStatus());
            orderProperties.add("ID:         " + order.getID());

            OutputFrame.printList(orderProperties);
        }
    }

    public String encodedProductsStringToInfoString(String encodedString) {
        LinkedHashMap<Product, Integer> productMap = orderDAO.encodedProductsStringToMap(encodedString);
        StringBuilder productListBuilder = new StringBuilder();
        String indent = "            ";
        for (Map.Entry<Product, Integer> pair : productMap.entrySet()) {
            productListBuilder.append(indent).append(pair.getValue()).append("x ").append(pair.getKey().getName()).append("\n");
        }
        return productListBuilder.toString();
    }
}