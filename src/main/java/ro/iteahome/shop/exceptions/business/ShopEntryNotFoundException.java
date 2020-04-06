package ro.iteahome.shop.exceptions.business;

public class ShopEntryNotFoundException extends ShopBusinessException {

    public ShopEntryNotFoundException() {
    }

    public ShopEntryNotFoundException(String message) {
        super(message);
    }
}