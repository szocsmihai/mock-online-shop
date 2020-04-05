package ro.iteahome.shop.ui;

import ro.iteahome.shop.service.UserService;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.OutputFrame;

public class AdminStartUI extends SwitchUI {
    public AdminStartUI() {
        this.setMenuTitle("ADMIN START MENU");

        this.uiOptions.add(new UIOption("1", "MANAGE PRODUCTS", () -> {
            //TODO: Develop this.
        }));

        this.uiOptions.add(new UIOption("2", "MANAGE ORDERS", () -> {
            //TODO: Develop this.
        }));

        this.uiOptions.add(new UIOption("3", "MANAGE USERS", () -> {
            //TODO: Develop this.
        }));

        this.uiOptions.add(new UIOption("4", "MANAGE PERSONAL ACCOUNT", () -> {
            //TODO: Develop this.
        }));

        this.uiOptions.add(new UIOption("0", "LOG OUT", () -> {
            new UserService().logOut();
            OutputFrame.printConfirmation("USER LOGGED OUT.");
        }));

        this.setInstructionsBar();
    }
}