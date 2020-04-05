package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Product;

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

    public void addNewProduct(Product product) throws ShopFileNotFoundException {
        product.setID(fileUtil.getSequence(DATABASE_SEQUENCE_PATH) + 1);
        fileUtil.addNewEntry(DATABASE_PATH, DATABASE_SEQUENCE_PATH, product);
    }

    public void updateProduct(Product oldProduct, Product newProduct) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        fileUtil.updateEntry(DATABASE_PATH, oldProduct, newProduct, constructProduct);
    }

    public void removeProduct(Product targetProduct) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        fileUtil.removeEntry(DATABASE_PATH, targetProduct, constructProduct);
    }

    /**
     * Methods that read the Product database:
     */

    public ArrayList<Product> getAllProducts() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.getAllEntries(DATABASE_PATH, constructProduct);
    }

    public ArrayList<String> getProductCategories() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.getSortedPropertyValues(DATABASE_PATH, 1);
    }

    public Product findProductByID(int ID) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findFirstEntryByProperty(DATABASE_PATH, 0, String.valueOf(ID), constructProduct);
    }

    public ArrayList<Product> findProductsByCategory(String pattern) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findEntriesByProperty(DATABASE_PATH, 1, pattern, constructProduct);
    }

    public ArrayList<Product> findPossibleProductsByCategory(String pattern) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findPossibleEntries(DATABASE_PATH, 1, pattern, constructProduct);
    }

    public ArrayList<Product> findPossibleProductsByName(String pattern) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findPossibleEntries(DATABASE_PATH, 2, pattern, constructProduct);
    }

    public ArrayList<Product> findPossibleProductsByDescription(String pattern) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findPossibleEntries(DATABASE_PATH, 3, pattern, constructProduct);
    }
}