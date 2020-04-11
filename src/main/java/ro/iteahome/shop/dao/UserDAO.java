package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.model.User;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;
import java.util.function.Function;

import static java.lang.Integer.parseInt;

/**
 * This class has 2 main responsibilities:
 * <p>
 * - Takes Strings and primitives from the different UIs, converts them to {@code User} objects and passes them to the
 * corresponding DAO class;
 * - Returns user information via its corresponding DAO class;
 */
public class UserDAO {

    /**
     * Every DAO class stores the paths ({@code String}) to its corresponding database and sequence .txt files and keeps
     * a reference to the {@code FileUtil} class, to access its methods.
     */
    final String DATABASE_PATH = "src/main/resources/users.txt";
    final String DATABASE_SEQUENCE_PATH = "src/main/resources/users_sequence.txt";

    private FileUtil<User> fileUtil = new FileUtil<>();

    /**
     * In order to convert lines of text to User objects, a {@code constructUser} lambda function is defined.
     */
    Function<String[], User> constructUser = (line) -> {

        int ID = parseInt(line[0]);
        User.Role currentRole = stringToRole(line[1]);
        String email = line[2];
        String password = line[3];
        String fullName = line[4];
        String phoneNo = line[5];
        String deliveryAddress = line[6];

        return new User(ID, currentRole, email, password, fullName, phoneNo, deliveryAddress);
    };

    /**
     * Methods that write to the User database:
     */

    public void addNewUser(User user) {
        try {

            user.setID(fileUtil.getSequence(DATABASE_SEQUENCE_PATH) + 1);
            fileUtil.addNewEntry(DATABASE_PATH, DATABASE_SEQUENCE_PATH, user);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert(e.getMessage());
        }
    }

    public void updateUser(User targetUser, User newUser) {
        try {

            fileUtil.updateEntry(DATABASE_PATH, targetUser, newUser, constructUser);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("USER NOT FOUND.");
        }
    }

    public void removeUser(User targetUser) {
        try {

            fileUtil.removeEntry(DATABASE_PATH, targetUser, constructUser);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("USER NOT FOUND.");
        }
    }

    /**
     * Methods that read the User database:
     */

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        try {

            return fileUtil.getAllEntries(DATABASE_PATH, constructUser);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("NO USERS FOUND.");
        }
        return users;
    }

    public User findUserByID(int ID) {
        User user = null;
        try {

            user = fileUtil.findFirstEntryByProperty(DATABASE_PATH, 0, String.valueOf(ID), constructUser);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("USER NOT FOUND.");
        }
        return user;
    }

    public User findUserByEmail(String email) {
        User user = null;
        try {

            user = fileUtil.findFirstEntryByProperty(DATABASE_PATH, 2, email, constructUser);

        } catch (ShopEntryNotFoundException e) {
            OutputFrame.printAlert("USER NOT FOUND.");
        }
        return user;
    }

    private User.Role stringToRole(String string) {
        User.Role currentRole = null;
        switch (string) {
            case "SHOPPER":
                currentRole = User.Role.SHOPPER;
                break;
            case "ADMIN":
                currentRole = User.Role.ADMIN;
                break;
            default:
                OutputFrame.printAlert("INVALID INPUT");
        }
        return currentRole;
    }
}