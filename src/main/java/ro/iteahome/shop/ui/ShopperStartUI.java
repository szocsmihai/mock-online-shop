package ro.iteahome.shop.ui;

import ro.iteahome.shop.service.UserService;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.OutputFrame;

public class ShopperStartUI extends SwitchUI {

    public ShopperStartUI() {
        this.setMenuTitle("START MENU");

        this.uiOptions.add(new UIOption("1", "ENTER SHOP", () -> {
            new ShopperShopUI().start();
        }));

        this.uiOptions.add(new UIOption("2", "MANAGE ACCOUNT", () -> {
            new ShopperManageAccountUI().start();
        }));

        this.uiOptions.add(new UIOption("0", "LOG OUT", () -> {
            new UserService().logOut();
            OutputFrame.printConfirmation("USER LOGGED OUT.");
        }));

        this.setInstructionsBar();
    }
}