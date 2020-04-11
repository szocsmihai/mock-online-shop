package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.model.Product;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.function.Function;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

/**
 * This class has 2 main responsibilities:
 * <p>
 * - Takes {@code Entry} objects from the {@code FileUtil} class, converts them to {@code Product} objects and passes
 * them to the {@code ProductService} class;
 * - Takes {@code Product} objects from the {@code ProductService} class and passes them to the {@code FileUtil} class
 * for persistence.
 */
public class ProductDAO {

    /**
     * Every DAO class stores the paths ({@code String}) to its corresponding database and sequence .txt files and keeps
     * a reference to the {@code FileUtil} class, to access its methods.
     */
    final String DATABASE_PATH = "src/main/resources/products.txt";
    final String DATABASE_SEQUENCE_PATH = "src/main/resources/products_sequence.txt";

    private FileUtil<Product> fileUtil = new FileUtil<>();

    /**
     * In order to convert lines of text to Product objects, a {@code constructProduct} lambda function is defined.
     */
    Function<String[], Product> constructProduct = (line) -> {

        int ID = parseInt(line[0]);
        String category = line[1];
        String name = line[2];
        String description = line[3];
        float price = parseFloat(line[4]);
        int stock = parseInt(line[6]);
        int unitsSold = parseInt(line[7]);

        return new Product(ID, category, name, description, price, stock, unitsSold);
    };

    /**
     * Methods that write to the Product database:
     */

    public void addNewProduct(Product product) {
        try {

            product.setID(fileUtil.getSequence(DATABASE_SEQUENCE_PATH) + 1);
            fileUtil.addNewEntry(DATABASE_PATH, DATABASE_SEQUENCE_PATH, product);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert(e.getMessage());
        }
    }

    public void updateProduct(Product oldProduct, Product newProduct) {
        try {

            fileUtil.updateEntry(DATABASE_PATH, oldProduct, newProduct, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("PRODUCT NOT FOUND.");
        }
    }

    public void removeProduct(Product targetProduct) {
        try {

            fileUtil.removeEntry(DATABASE_PATH, targetProduct, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("PRODUCT NOT FOUND.");
        }
    }

    /**
     * Methods that read the Product database:
     */

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try {

            products = fileUtil.getAllEntries(DATABASE_PATH, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO PRODUCTS FOUND.");
        }
        return products;
    }

    public ArrayList<String> getProductCategories() {
        ArrayList<String> categories = new ArrayList<>();
        try {

            categories = fileUtil.getSortedPropertyValues(DATABASE_PATH, 1);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO CATEGORIES FOUND.");
        }
        return categories;
    }

    public Product findProductByID(int ID) {
        Product product = null;
        try {

            product = fileUtil.findFirstEntryByProperty(DATABASE_PATH, 0, String.valueOf(ID), constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printConfirmation("PRODUCT NOT FOUND.");
        }
        return product;
    }

    public ArrayList<Product> findProductsByCategory(String pattern) {
        ArrayList<Product> products = new ArrayList<>();
        try {

            products = fileUtil.findEntriesByProperty(DATABASE_PATH, 1, pattern, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO PRODUCTS FOUND.");
        }
        return products;
    }

    public ArrayList<Product> findPossibleProductsByCategory(String pattern) {
        ArrayList<Product> products = new ArrayList<>();
        try {

            products = fileUtil.findPossibleEntries(DATABASE_PATH, 1, pattern, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO PRODUCTS FOUND.");
        }
        return products;
    }

    public ArrayList<Product> findPossibleProductsByName(String pattern) {
        ArrayList<Product> products = new ArrayList<>();
        try {

            products = fileUtil.findPossibleEntries(DATABASE_PATH, 2, pattern, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO PRODUCTS FOUND.");
        }
        return products;
    }

    public ArrayList<Product> findPossibleProductsByDescription(String pattern) {
        ArrayList<Product> products = new ArrayList<>();
        try {

            return fileUtil.findPossibleEntries(DATABASE_PATH, 3, pattern, constructProduct);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO PRODUCTS FOUND.");
        }
        return products;
    }
}