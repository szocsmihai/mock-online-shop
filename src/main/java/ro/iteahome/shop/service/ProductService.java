package ro.iteahome.shop.service;

import ro.iteahome.shop.dao.ProductDAO;
import ro.iteahome.shop.model.Cart;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class ProductService {

    /**
     * This class has 2 main responsibilities:
     * <p>
     * - Takes Strings and primitives from the different UIs, converts them to {@code Productr} objects and passes them
     * to the corresponding DAO class;
     * - Returns product information via its corresponding DAO class;
     */
    private ProductDAO productDAO = new ProductDAO();

    /**
     * Methods that write to the Product database through {@code ProductDAO}:
     */

    public void addNewProduct(String category, String name, String description, float price, int stock) {
        productDAO.addNewProduct(new Product(category, name, description, price, stock));
        //TODO: The UI for adding products should allow for "-" as the description, by prompting the admin.
    }

    public void updateProduct(String ID, String newCategory, String newName, String newDescription, String newPrice, String newStock, String newUnitsSold) {
        Product currentProduct = productDAO.findProductByID(parseInt(ID));
        Product newProduct = new Product(
                currentProduct.getID(),
                newCategory,
                newName,
                newDescription,
                parseFloat(newPrice),
                parseInt(newStock),
                parseInt(newUnitsSold));
        productDAO.updateProduct(currentProduct, newProduct);
    }

    public void updateStockAndSoldUnits(String ID, String unitsSold) {
        Product currentProduct = productDAO.findProductByID(parseInt(ID));
        Product newProduct = new Product(
                currentProduct.getID(),
                currentProduct.getCategory(),
                currentProduct.getName(),
                currentProduct.getDescription(),
                currentProduct.getPrice(),
                currentProduct.getStock() - parseInt(unitsSold),
                currentProduct.getUnitsSold() + parseInt(unitsSold));
        productDAO.updateProduct(currentProduct, newProduct);
    }

    public void updateDatabaseAfterOrderConfirmation() {
        for (Map.Entry<Product, Integer> pair : Cart.content.entrySet()) {
            updateStockAndSoldUnits(String.valueOf(pair.getKey().getID()), String.valueOf(pair.getValue()));
        }
    }

    public void removeProduct(Product product) {
        productDAO.removeProduct(product);
    }

    /**
     * Methods that read from the Product database through {@code ProductDAO}:
     */

    public ArrayList<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public ArrayList<Product> getSoldProducts() {
        ArrayList<Product> soldProducts = new ArrayList<>();
        for (Product product : getAllProducts()) {
            if (product.getUnitsSold() > 0) {
                soldProducts.add(product);
            }
        }
        return soldProducts;
    }

    public ArrayList<Product> getPopularProducts() {
        ArrayList<Product> popularProducts = new ArrayList<>();
        ArrayList<Product> soldProducts = getSoldProducts();
        if (soldProducts.size() > 2) {
            soldProducts.sort(Comparator.comparing(Product::getUnitsSold));
            for (int i = 0; i < 3; i++) {
                popularProducts.add(soldProducts.get(soldProducts.size() - 1 - i));
            }
        }
        return popularProducts;
    }

    public Product findProductByID(int ID) {
        return productDAO.findProductByID(ID);
    }

    public ArrayList<Product> findProductsByCategory(String category) {
        return productDAO.findProductsByCategory(category);
    }

    public ArrayList<Product> findPossibleProductsByCategory(String pattern) {
        return productDAO.findPossibleProductsByCategory(pattern);
    }

    public ArrayList<Product> findPossibleProductsByName(String pattern) {
        return productDAO.findPossibleProductsByName(pattern);
    }

    public ArrayList<Product> findPossibleProductsByDescription(String pattern) {
        return productDAO.findPossibleProductsByDescription(pattern);
    }

    public HashMap<Integer, String> getNumberedCategories() {
        ArrayList<String> list = productDAO.getProductCategories();
        HashMap<Integer, String> numberedList = new HashMap<>();
        int position = 0;
        for (String category : list) {
            numberedList.put(++position, category);
        }
        return numberedList;
    }

    /**
     * Extra: method that prints out the relevant information about the Products of a given ArrayList of Products:
     */
    public void outputProductList(ArrayList<Product> products) {
        for (Product product : products) {
            ArrayList<String> productProperties = new ArrayList<>();

            productProperties.add("NAME:       " + product.getName());
            productProperties.add("CATEGORY:   " + product.getCategory());
            productProperties.add("SPECS:      " + product.getDescription());
            productProperties.add("PRICE:      " + product.getPrice() + " " + product.getCurrency());
            productProperties.add("SHOP STOCK: " + product.getStock() + " " + product.getStockUM());
            productProperties.add("");
            productProperties.add("ID:         " + product.getID() + " (USE THIS ID TO ADD PRODUCT TO CART)");

            OutputFrame.printList(productProperties);
        }
    }
}