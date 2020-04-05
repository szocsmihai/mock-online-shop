package ro.iteahome.shop.ui;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Cart;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.security.UserContext;
import ro.iteahome.shop.service.CartService;
import ro.iteahome.shop.service.OrderService;
import ro.iteahome.shop.service.ProductService;
import ro.iteahome.shop.ui.config.UISettings;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.Map;

import static java.lang.Integer.parseInt;

public class ShopperManageCartUI extends LoopUI {

    Product selectedProduct;
    String selectedQuantity;

    ProductService productService = new ProductService();
    CartService cartService = new CartService();
    OrderService orderService = new OrderService();

    public ShopperManageCartUI() {
        this.setMenuTitle("CART MENU");

        this.uiOptions.add(new UIOption("1", "CONFIRM ORDER", () -> {
            try {

                if (!areCurrentUserBillingDetailsSet()) {
                    new ShopperGetDeliveryDetailsUI().start();
                }
                if (areCurrentUserBillingDetailsSet()) {
                    new ShopperPaymentUI().start();

                    orderService.addNewOrder(UserContext.getLoggedInUser().getID(), Cart.content, Cart.getTotalCost());
                    productService.updateDatabaseAfterOrderConfirmation();
                    Cart.content.clear();

                    selectedOption = "0"; //close UI loop
                }

            } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("2", "UPDATE QUANTITY", () -> {
            try {

                showSelectProduct();
                if (selectedProduct != null) {
                    showSelectQuantity();
                    if (selectedQuantity != null) {
                        cartService.updateCartProductQuantity(String.valueOf(selectedProduct.getID()), selectedQuantity);
                        OutputFrame.printConfirmation("QUANTITY UPDATED...");
                        Thread.sleep(UISettings.popUpWaitTime);
                    }
                }

            } catch (ShopFileNotFoundException | ShopEntryNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("3", "REMOVE PRODUCT", () -> {
            try {

                showSelectProduct();
                if (selectedProduct != null) {
                    cartService.removeProduct(String.valueOf(selectedProduct.getID()));
                    OutputFrame.printConfirmation("\"" + selectedProduct.getName() + "\" REMOVED FROM CART.");
                    if (Cart.content.isEmpty()) {
                        OutputFrame.printConfirmation("YOUR CART IS CURRENTLY EMPTY...");
                        selectedOption = "0"; //close UI loop
                    }
                    Thread.sleep(UISettings.popUpWaitTime);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("4", "CLEAR CART", () -> {
            try {

                Cart.content.clear();
                OutputFrame.printConfirmation("YOUR CART HAS BEEN CLEARED...");
                Thread.sleep(UISettings.popUpWaitTime);
                selectedOption = "0"; //close UI loop

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("0", "PREVIOUS MENU", () -> {
            //Do nothing, let the UI loop close.
        }));

        this.setInstructionsBar();
    }

    @Override
    public void start() {
        try {

            if (!Cart.content.isEmpty()) {
                super.start();
            } else {
                OutputFrame.printTitle(menuTitle);
                OutputFrame.printConfirmation("YOUR CART IS CURRENTLY EMPTY...");
                Thread.sleep(UISettings.popUpWaitTime);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    void showInitialInfo() {
        OutputFrame.printInfo("PRODUCTS IN YOUR CART:");
        cartService.outputCartProducts();
        showCartTotalCost();
    }

    private void showCartTotalCost() {
        OutputFrame.printInfo("TOTAL COST: " + Cart.getTotalCost() + " " + Product.CURRENCY);
    }

    private boolean areCurrentUserBillingDetailsSet() {
        String fullName = UserContext.getLoggedInUser().getFullName();
        String phoneNo = UserContext.getLoggedInUser().getPhoneNo();
        String deliveryAddress = UserContext.getLoggedInUser().getDeliveryAddress();
        return !fullName.equals("-") && !phoneNo.equals("-") && !deliveryAddress.equals("-");
    }

    private void showSelectProduct() {
        String userInput = InputFrame.getInputFromPrompt("SELECT A PRODUCT ID TO UPDATE ITS QUANTITY (0 : CANCEL):");
        while (!userInput.equals("0") && (!userInput.matches("\\d+") || !isProductInCart(userInput))) {
            userInput = InputFrame.getInputFromAlert("INVALID INPUT. TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            selectedProduct = cartService.findCartProductByID(userInput);
        }
    }

    private void showSelectQuantity() {
        try {

            String userInput = InputFrame.getInputFromPrompt("ENTER NEW QUANTITY FOR \"" + selectedProduct.getName() + "\" (0 : CANCEL):");
            while (!userInput.equals("0") && (!userInput.matches("\\d+") || !cartService.isQuantityWithinStock(String.valueOf(selectedProduct.getID()), userInput))) {
                userInput = InputFrame.getInputFromAlert("INVALID INPUT OR INSUFFICIENT STOCK. TRY AGAIN (0 : CANCEL):");
            }
            if (!userInput.equals("0")) {
                selectedQuantity = userInput;
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isProductInCart(String ID) {
        boolean isProductInCart = false;
        for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
            if (parseInt(ID) == pair.getKey().getID()) {
                isProductInCart = true;
                break;
            }
        }
        return isProductInCart;
    }
}