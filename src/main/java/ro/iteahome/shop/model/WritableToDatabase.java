package ro.iteahome.shop.model;

/**
 * Objects writable to database, henceforth referred to as "entries", are objects that must be stored in dedicated
 * databases (e.g. Users, Products, Orders etc.). The 2 common traits of all entries are:
 * <p>
 * - ID (initialized here as -1), with its own getter and setter methods. Will be automatically reset to a final,
 * correct, value by the {@code FileUtil} class upon writing the entry to the database;
 * - A method for building a string from the property values of the entry.
 */

public abstract class WritableToDatabase {

    int ID = -1;

    abstract public String toDatabaseString();

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}