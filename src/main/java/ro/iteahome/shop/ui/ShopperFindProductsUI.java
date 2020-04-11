package ro.iteahome.shop.ui;

import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;

public class ShopperFindProductsUI extends ShopperGetProductsUI {

    private String searchPattern;

    ShopperFindProductsUI() {
        this.setMenuTitle("FIND PRODUCTS MENU");

        this.uiOptions.add(new UIOption("1", "CATEGORY", () -> {
            searchPattern = InputFrame.getInputFromPrompt("ENTER SEARCH PHRASE (0 : CANCEL):");
            showByCategory(searchPattern);
            showAddProductToCart();
        }));

        this.uiOptions.add(new UIOption("2", "NAME", () -> {
            searchPattern = InputFrame.getInputFromPrompt("ENTER SEARCH PHRASE (0 : CANCEL):");
            showByName(searchPattern);
            showAddProductToCart();
        }));

        this.uiOptions.add(new UIOption("3", "DESCRIPTION", () -> {
            searchPattern = InputFrame.getInputFromPrompt("ENTER SEARCH PHRASE (0 : CANCEL):");
            showByDescription(searchPattern);
            showAddProductToCart();
        }));

        this.uiOptions.add(new UIOption("4", "ALL", () -> {
            searchPattern = InputFrame.getInputFromPrompt("ENTER SEARCH PHRASE (0 : CANCEL):");
            showByCategory(searchPattern);
            showByName(searchPattern);
            showByDescription(searchPattern);
            showAddProductToCart();
        }));

        this.uiOptions.add(new UIOption("0", "PREVIOUS MENU", () -> {
            //Do nothing, let the UI loop close.
        }));

        this.setInstructionsBar();
    }

    @Override
    void showInitialInfo() {
        OutputFrame.printInfo("SEARCH BY:");
    }

    @Override
    void resetLocalVariables() {
        searchPattern = null;
        displayedProducts.clear();
        selectedProduct = null;
        selectedQuantity = null;
    }

    private void showByCategory(String pattern) {
        ArrayList<Product> resultsByCategory = productService.findPossibleProductsByCategory(pattern);
        OutputFrame.printConfirmation("MATCHES BY CATEGORY:");
        productService.outputProductList(resultsByCategory);
        displayedProducts.addAll(resultsByCategory);
    }

    private void showByName(String pattern) {
        ArrayList<Product> resultsByName = productService.findPossibleProductsByName(pattern);
        OutputFrame.printConfirmation("MATCHES BY NAME:");
        productService.outputProductList(resultsByName);
        displayedProducts.addAll(resultsByName);
    }

    private void showByDescription(String pattern) {
        ArrayList<Product> resultsByDescription = productService.findPossibleProductsByDescription(pattern);
        OutputFrame.printConfirmation("MATCHES BY DESCRIPTION:");
        productService.outputProductList(resultsByDescription);
        displayedProducts.addAll(resultsByDescription);
    }
}