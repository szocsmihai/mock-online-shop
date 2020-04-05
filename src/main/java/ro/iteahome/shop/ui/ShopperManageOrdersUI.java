package ro.iteahome.shop.ui;

import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.OutputFrame;

public class ShopperManageOrdersUI extends LoopUI {

    //TODO: Develop this.
    //TODO: update stock if order is cancelled.

    public ShopperManageOrdersUI() {
        this.setMenuTitle("MANAGE ORDERS MENU");

        this.uiOptions.add(new UIOption("1", "ACTIVE ORDERS", () -> {
            //TODO: Develop this.
        }));

        this.uiOptions.add(new UIOption("2", "CLOSED ORDERS", () -> {
            //TODO: Develop this.
        }));

        this.setInstructionsBar();
    }

    @Override
    void showInitialInfo() {
        OutputFrame.printInfo("SHOW:");
    }
}