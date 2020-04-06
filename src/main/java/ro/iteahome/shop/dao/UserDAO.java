package ro.iteahome.shop.dao;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
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
     * In order to convert lines of text to Product objects, a {@code constructUser} lambda function is defined.
     */
    Function<String[], User> constructUser = (line) -> {

        int ID = parseInt(line[0]);
        User.Role currentRole = mapStringToRoleEnum(line[1]);
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

    public void updateUser(User targetUser, User newUser) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        fileUtil.updateEntry(DATABASE_PATH, targetUser, newUser, constructUser);
    }

    public void removeUser(User targetUser) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        fileUtil.removeEntry(DATABASE_PATH, targetUser, constructUser);
    }

    /**
     * Methods that read the User database:
     */

    public ArrayList<User> getAllUsers() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.getAllEntries(DATABASE_PATH, constructUser);
    }

    public User findUserByID(int ID) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findFirstEntryByProperty(DATABASE_PATH, 0, String.valueOf(ID), constructUser);
    }

    public User findUserByEmail(String email) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return fileUtil.findFirstEntryByProperty(DATABASE_PATH, 2, email, constructUser);
    }

    private User.Role mapStringToRoleEnum(String string) {
        User.Role currentRole;
        if (string.equals(User.Role.SHOPPER.toString())) {
            currentRole = User.Role.SHOPPER;
        } else {
            currentRole = User.Role.ADMIN;
        }
        return currentRole;
    }
}