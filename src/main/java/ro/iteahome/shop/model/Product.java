package ro.iteahome.shop.model;

public class Product extends WritableToDatabase {

    //product ID is initialized by the WritableToDatabase class as -1 by default;
    private String category;
    private String name;
    private String description = "-";
    private float price;
    public static final String CURRENCY = "RON";
    private int stock;
    private int unitsSold = 0;
    public static final String STOCK_UM = "PC.";

    @Override
    public String toDatabaseString() {
        return ID + "|" + category + "|" + name + "|" + description + "|" + price + "|" + CURRENCY + "|" + stock + "|" + unitsSold + "|" + STOCK_UM;
    }

    /**
     * Constructor for adding new Products to the database:
     */
    public Product(String category, String name, String description, float price, int stock) {
        //product ID is updated automatically upon writing to database.
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        //CURRENCY is declared final and initialized by this class;
        this.stock = stock;
        //unitsSold is initialized by this class as 0 by default;
        //STOCK_UM is declared final and initialized by this class;
    }

    /**
     * Constructor reading/updating Products in the database:
     */
    public Product(int ID, String category, String name, String description, float price, int stock, int unitsSold) {
        this.ID = ID;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        //CURRENCY is declared and initialized by this class;
        this.stock = stock;
        this.unitsSold = unitsSold;
        //STOCK_UM is declared and initialized by this class;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getCurrency() {
        return CURRENCY;
    }

    public int getStock() {
        return stock;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public String getStockUM() {
        return STOCK_UM;
    }
}