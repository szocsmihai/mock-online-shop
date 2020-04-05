package ro.iteahome.shop.service;

import ro.iteahome.shop.dao.OrderDAO;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Order;
import ro.iteahome.shop.model.Product;

import java.util.HashMap;

public class OrderService {

    /**
     * This class has 2 main responsibilities:
     * <p>
     * - Takes Strings and primitives from the different UIs, converts them to {@code Order} objects and passes them to
     * the corresponding DAO class;
     * - Returns order information via its corresponding DAO class;
     */
    private OrderDAO orderDAO = new OrderDAO();

    public void addNewOrder(int userID, HashMap<Product, Integer> products, float totalPrice) throws ShopFileNotFoundException {
        orderDAO.addNewOrder(new Order(userID, products, totalPrice));
    }

    public void updateOrder() {
        //TODO: Develop this for admins. Shoppers should send a request for updates and get a confirmation back after the fact.
    }

    public void removeOrder() {
        //TODO: Develop this for admins. Shoppers should send a request for cancellation and get a confirmation back after the fact.
    }
}