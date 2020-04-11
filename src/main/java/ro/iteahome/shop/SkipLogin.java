package ro.iteahome.shop;

import ro.iteahome.shop.service.UserService;
import ro.iteahome.shop.ui.ShopperStartUI;

public class SkipLogin {

    public static void main(String[] args) {
        new UserService().logIn("shopper@shop.com", "P@ssW0rd");
        new ShopperStartUI().start();
    }
}