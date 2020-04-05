package ro.iteahome.shop.ui;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ShopperBrowseProductsUI extends ShopperGetProductsUI {

    private HashMap<Integer, String> allCategories = new HashMap<>();
    private String selectedCategory = null;

    public ShopperBrowseProductsUI() {
        this.setMenuTitle("BROWSE PRODUCTS MENU");

        this.uiOptions.add(new UIOption("1", "ALL CATEGORIES", () -> {
            showAllCategories();
            showSelectCategory();
            if (selectedCategory != null) {
                showProductsOfCategory();
                showAddProductToCart();
            }
        }));

        this.uiOptions.add(new UIOption("2", "POPULAR PRODUCTS", () -> {
            showPopularProducts();
            showAddProductToCart();
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
        super.resetLocalVariables();
        allCategories.clear();
        selectedCategory = null;
    }

    private void showAllCategories() {
        OutputFrame.printInfo("AVAILABLE PRODUCT CATEGORIES:");
        try {

            allCategories = productService.getNumberedCategories();
            OutputFrame.printMapPairs(allCategories);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO CATEGORIES AVAILABLE...");
        } catch (ShopFileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showSelectCategory() {
        String userInput = InputFrame.getInputFromPrompt("CHOOSE A CATEGORY (0 : CANCEL):");
        while (!userInput.equals("0") && (!userInput.matches("\\d+") || parseInt(userInput) > getMaxCategoryKey(allCategories))) {
            userInput = InputFrame.getInputFromAlert("INVALID INPUT. TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            selectedCategory = userInput;
        }
    }

    private void showProductsOfCategory() {
        try {

            for (Map.Entry<Integer, String> pair : allCategories.entrySet()) {
                if (parseInt(selectedCategory) == pair.getKey()) {
                    ArrayList<Product> resultsByCategory = productService.findProductsByCategory(pair.getValue());
                    OutputFrame.printConfirmation("PRODUCTS IN SELECTED CATEGORY:");
                    productService.outputProductList(resultsByCategory);
                    displayedProducts.addAll(resultsByCategory);
                    break;
                }
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void showPopularProducts() {
        try {

            ArrayList<Product> popularProducts = productService.getPopularProducts();
            if (popularProducts.size() != 0) {
                OutputFrame.printInfo("TOP 3 BEST-SELLING PRODUCTS:");
                productService.outputProductList(popularProducts);
                displayedProducts.addAll(popularProducts);
            } else {
                OutputFrame.printInfo("NOT ENOUGH PRODUCTS HAVE BEEN SOLD YET.");
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int getMaxCategoryKey(HashMap<Integer, String> categoryMap) {
        int maxKey = 0;
        for (Map.Entry<Integer, String> pair : categoryMap.entrySet()) {
            if (pair.getKey() > maxKey) {
                maxKey = pair.getKey();
            }
        }
        return maxKey;
    }
}