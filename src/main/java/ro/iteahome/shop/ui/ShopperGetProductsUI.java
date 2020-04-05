package ro.iteahome.shop.ui;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.service.CartService;
import ro.iteahome.shop.service.ProductService;
import ro.iteahome.shop.ui.config.UISettings;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.HashSet;

import static java.lang.Integer.parseInt;

/**
 * This class employs the functionality of {@code LoopUI} to add {@code Products} to the {@code Cart}. It is an
 * intermediary step in achieving this goal, in that it first needs a way to display a list of {@code Products}.
 * That method is provided by the inheriting class.
 */

public abstract class ShopperGetProductsUI extends LoopUI {

    HashSet<Product> displayedProducts = new HashSet<>();
    Product selectedProduct;
    String selectedQuantity;

    ProductService productService = new ProductService();
    CartService cartService = new CartService();

    @Override
    void resetLocalVariables() {
        displayedProducts.clear();
        selectedProduct = null;
        selectedQuantity = null;
    }

    void showAddProductToCart() {
        try {

            if (!displayedProducts.isEmpty()) {
                showSelectProduct();
                if (selectedProduct != null) {
                    showSelectQuantity();
                    if (selectedQuantity != null) {
                        cartService.addProductToCart(String.valueOf(selectedProduct.getID()), selectedQuantity);
                        OutputFrame.printConfirmation("PRODUCT ADDED TO CART...");
                        Thread.sleep(UISettings.popUpWaitTime);
                    }
                }
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showSelectProduct() {
        try {

            String userInput = InputFrame.getInputFromPrompt("SELECT A PRODUCT ID TO ADD IT TO YOUR CART (0 : CANCEL):");
            while (!userInput.equals("0") && (!userInput.matches("\\d+") || !wasProductDisplayed(userInput))) {
                userInput = InputFrame.getInputFromAlert("INVALID INPUT. TRY AGAIN (0 : CANCEL):");
            }
            if (!userInput.equals("0")) {
                selectedProduct = productService.findProductByID(parseInt(userInput));
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showSelectQuantity() {
        try {

            String userInput = InputFrame.getInputFromPrompt("ENTER DESIRED QUANTITY FOR \"" + selectedProduct.getName() + "\" (0 : CANCEL):");
            while (!userInput.equals("0") && (!userInput.matches("\\d+") || !cartService.canExtraQuantityBeAdded(String.valueOf(selectedProduct.getID()), userInput))) {
                userInput = InputFrame.getInputFromAlert("INVALID INPUT OR INSUFFICIENT STOCK. TRY AGAIN (0 : CANCEL):");
            }
            if (!userInput.equals("0")) {
                selectedQuantity = userInput;
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean wasProductDisplayed(String ID) {
        boolean wasProductDisplayed = false;
        for (Product product : displayedProducts) {
            if (parseInt(ID) == product.getID()) {
                wasProductDisplayed = true;
                break;
            }
        }
        return wasProductDisplayed;
    }
}