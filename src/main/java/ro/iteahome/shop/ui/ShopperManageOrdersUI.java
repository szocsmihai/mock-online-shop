package ro.iteahome.shop.ui;

import ro.iteahome.shop.model.Order;
import ro.iteahome.shop.model.User;
import ro.iteahome.shop.security.UserContext;
import ro.iteahome.shop.service.OrderService;
import ro.iteahome.shop.ui.config.UISettings;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class ShopperManageOrdersUI extends LoopUI {

    ArrayList<Order> displayedOrders = new ArrayList<>();
    Order selectedOrder = null;

    OrderService orderService = new OrderService();

    public ShopperManageOrdersUI() {
        this.setMenuTitle("MANAGE ORDERS MENU");

        this.uiOptions.add(new UIOption("1", "ACTIVE ORDERS", () -> {
            showManageActiveOrders();
        }));

        this.uiOptions.add(new UIOption("2", "CLOSED ORDERS", () -> {
            showClosedOrders();
        }));

        this.uiOptions.add(new UIOption("0", "PREVIOUS MENU", () -> {
            //Do nothing, let the UI loop close.
        }));

        this.setInstructionsBar();
    }

    @Override
    void showInitialInfo() {
        OutputFrame.printInfo("SHOW:");
    }

    @Override
    void resetLocalVariables() {
        displayedOrders.clear();
        selectedOrder = null;
    }

    private void showManageActiveOrders() {
        try {

            showActiveOrders();
            if (!displayedOrders.isEmpty()) {
                showSelectOrder();
                if (selectedOrder != null) {
                    orderService.cancelOrder();
                    OutputFrame.printConfirmation("ORDER CANCELLED...");
                    Thread.sleep(UISettings.popUpWaitTime);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showActiveOrders() {
        User user = UserContext.getLoggedInUser();
        ArrayList<Order> userOrders = orderService.findOrdersByUserID(user.getID());
        if (!userOrders.isEmpty()) {
            displayedOrders = userOrders
                    .stream()
                    .filter(order -> order.getCurrentStatus().equals(Order.Status.NOT_DELIVERED))
                    .collect(Collectors.toCollection(ArrayList::new));

            if (!displayedOrders.isEmpty()) {
                OutputFrame.printInfo("YOUR ACTIVE ORDERS:");
                orderService.outputOrderList(displayedOrders);
            } else {
                OutputFrame.printConfirmation("YOU HAVE NO ACTIVE ORDERS.");
            }
        }
    }

    private void showClosedOrders() {
        User user = UserContext.getLoggedInUser();
        ArrayList<Order> userOrders = orderService.findOrdersByUserID(user.getID());
        if (!userOrders.isEmpty()) {
            ArrayList<Order> deliveredOrders = userOrders
                    .stream()
                    .filter(order -> order.getCurrentStatus().equals(Order.Status.DELIVERED))
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<Order> cancelledOrders = userOrders
                    .stream()
                    .filter(order -> order.getCurrentStatus().equals(Order.Status.CANCELLED))
                    .collect(Collectors.toCollection(ArrayList::new));
            displayedOrders.addAll(deliveredOrders);
            displayedOrders.addAll(cancelledOrders);

            if (!displayedOrders.isEmpty()) {
                OutputFrame.printInfo("YOUR CLOSED ORDERS:");
                orderService.outputOrderList(displayedOrders);
            } else {
                OutputFrame.printConfirmation("YOU HAVE NO CLOSED ORDERS.");
            }
        }
    }

    private void showSelectOrder() {
        String userInput = InputFrame.getInputFromPrompt("CANCEL AN ORDER BY SELECTING ITS ID (0 : GO BACK):");
        while (!userInput.equals("0") && (!userInput.matches("\\d+") || !wasOrderDisplayed(userInput))) {
            userInput = InputFrame.getInputFromAlert("INVALID INPUT. TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            selectedOrder = orderService.findOrderByID(parseInt(userInput));
        }
    }

    private boolean wasOrderDisplayed(String ID) {
        boolean wasOrderDisplayed = false;
        for (Order order : displayedOrders) {
            if (parseInt(ID) == order.getID()) {
                wasOrderDisplayed = true;
                break;
            }
        }
        return wasOrderDisplayed;
    }
}