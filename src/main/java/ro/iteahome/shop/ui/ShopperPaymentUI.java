package ro.iteahome.shop.ui;

import ro.iteahome.shop.model.Order;
import ro.iteahome.shop.ui.config.UISettings;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.OutputFrame;

public class ShopperPaymentUI extends LoopUI {

    public Order.PaymentMethod selectedPaymentMethod;

    public ShopperPaymentUI() {
        this.setMenuTitle("PAYMENT MENU");

        this.uiOptions.add(new UIOption("1", "ON DELIVERY VIA CASH/CARD", () -> {
            try {

                selectedPaymentMethod = Order.PaymentMethod.ON_DELIVERY;
                OutputFrame.printConfirmation("THANK YOU FOR YOUR ORDER...");
                selectedOption = "0"; //close UI loop
                Thread.sleep(UISettings.popUpWaitTime);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("2", "ONLINE VIA CARD", () -> {
            try {

                selectedPaymentMethod = Order.PaymentMethod.ONLINE;
                OutputFrame.printConfirmation("[THIS EXERCISE WILL ASSUME YOU ENTERED VALID PAYMENT DATA JUST NOW] THANK YOU FOR YOUR ORDER...");
                selectedOption = "0"; //close UI loop
                Thread.sleep(UISettings.popUpWaitTime);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("0", "CANCEL", () -> {
            //Do nothing, let the UI loop close.
        }));

        this.setInstructionsBar();
    }

    @Override
    void showInitialInfo() {
        OutputFrame.printInfo("SELECT PAYMENT METHOD:");
    }
}