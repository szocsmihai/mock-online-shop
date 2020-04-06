package ro.iteahome.shop;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.service.UserService;
import ro.iteahome.shop.ui.ShopperStartUI;

public class SkipLogin {

    public static void main(String[] args) {
        try {
            new UserService().logIn("shopper@shop.com", "P@ssW0rd");
        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
        new ShopperStartUI().start();
    }
}