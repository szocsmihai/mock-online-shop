package ro.iteahome.shop.service;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Cart;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class CartService {

    private ProductService productService = new ProductService();

    public void addProductToCart(String ID, String quantity) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        Product product = productService.findProductByID(parseInt(ID));
        if (Cart.content.isEmpty()) {
            Cart.content.put(product, parseInt(quantity));
        } else {
            boolean alreadyAdded = false;
            for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
                if (pair.getKey().getID() == product.getID()) {
                    alreadyAdded = true;
                    pair.setValue(pair.getValue() + parseInt(quantity));
                    break;
                }
            }
            if (!alreadyAdded) {
                Cart.content.put(product, parseInt(quantity));
            }
        }
    }

    public void removeProduct(String ID) {
        Product product = findCartProductByID(ID);
        Cart.content.remove(product);
    }

    public void updateCartProductQuantity(String ID, String quantity) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        Product product = findCartProductByID(ID);
        Cart.content.replace(product, parseInt(quantity));
    }

    public Product findCartProductByID(String ID) {
        Product product = null;
        for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
            if (parseInt(ID) == pair.getKey().getID()) {
                product = pair.getKey();
                break;
            }
        }
        return product;
    }

    public int productQuantitySoFar(String ID) {
        int quantitySoFar = 0;
        for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
            if (parseInt(ID) == pair.getKey().getID()) {
                quantitySoFar = pair.getValue();
                break;
            }
        }
        return quantitySoFar;
    }

    public boolean canExtraQuantityBeAdded(String ID, String quantity) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        Product databaseProduct = productService.findProductByID(parseInt(ID));
        int totalQuantity = parseInt(quantity) + productQuantitySoFar(String.valueOf(databaseProduct.getID()));
        return (totalQuantity <= databaseProduct.getStock());
    }

    public boolean isQuantityWithinStock(String ID, String quantity) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        Product databaseProduct = productService.findProductByID(parseInt(ID));
        return parseInt(quantity) <= databaseProduct.getStock();
    }

    //TODO: Determine if this is still useful:
    public ArrayList<Product> cartProductsAsArray() {
        ArrayList<Product> cartArray = new ArrayList<>();
        for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
            cartArray.add(pair.getKey());
        }
        return cartArray;
    }

    /**
     * Extra: method that prints out the relevant information about the Products in the cart:
     */
    public void outputCartProducts() {
        for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
            ArrayList<String> productProperties = new ArrayList<>();

            productProperties.add("NAME:       " + pair.getKey().getName());
            productProperties.add("CATEGORY:   " + pair.getKey().getCategory());
            productProperties.add("SPECS:      " + pair.getKey().getDescription());
            productProperties.add("PRICE:      " + pair.getKey().getPrice() + " " + pair.getKey().getCurrency());
            productProperties.add("");
            productProperties.add("QUANTITY:   " + pair.getValue() + " " + pair.getKey().getStockUM());
            productProperties.add("ID:         " + pair.getKey().getID() + " (USE THIS ID TO UPDATE/REMOVE PRODUCT FROM CART)");

            OutputFrame.printList(productProperties);
        }
    }
}