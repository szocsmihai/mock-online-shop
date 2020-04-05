package ro.iteahome.shop.ui;

import ro.iteahome.shop.ui.options.UIOption;

public class ShopperShopUI extends SwitchUI {

    public ShopperShopUI() {
        this.setMenuTitle("SHOP MENU");

        this.uiOptions.add(new UIOption("1", "BROWSE PRODUCTS", () -> {
            new ShopperBrowseProductsUI().start();
        }));

        this.uiOptions.add(new UIOption("2", "FIND PRODUCTS", () -> {
            new ShopperFindProductsUI().start();
        }));

        this.uiOptions.add(new UIOption("3", "MANAGE CART", () -> {
            new ShopperManageCartUI().start();
        }));

        this.uiOptions.add(new UIOption("4", "MANAGE ORDERS", () -> {
            //TODO: Develop this.
        }));

        this.uiOptions.add(new UIOption("0", "PREVIOUS MENU", () -> {
            //Do nothing, let the UI loop close.
        }));

        this.setInstructionsBar();
    }
}