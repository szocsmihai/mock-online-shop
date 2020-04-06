package ro.iteahome.shop.service;

import ro.iteahome.shop.dao.UserDAO;
import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.model.Cart;
import ro.iteahome.shop.model.User;
import ro.iteahome.shop.security.UserContext;
import ro.iteahome.shop.ui.popups.OutputFrame;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * This class has 3 main responsibilities:
 * <p>
 * - Takes {@code String} objects from the different UIs, converts them to {@code User} objects and passes them to the
 * {@code UserDAO} class;
 * - Returns user information via its corresponding DAO class;
 * - Manipulates the {@code UserContext} class to read or set the user that is logged in.
 */
public class UserService {

    private UserDAO userDAO = new UserDAO();

    /**
     * Methods that manipulate the {@code UserContext}:
     */

    public void logIn(String email, String password) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        if (doCredentialsMatch(email, password)) {
            UserContext.setLoggedInUser(userDAO.findUserByEmail(email));
        } else {
            OutputFrame.printAlert("WRONG CREDENTIALS.");
        }
    }

    public void logOut() {
        UserContext.setLoggedInUser(null);
        Cart.content.entrySet().clear();
    }

    /**
     * Methods that interact with the User database through {@code UserDAO}:
     */

    public void signUp(String email, String password) throws ShopFileNotFoundException {
        userDAO.addNewUser(new User(email, password));
    }

    public void updateUserInfo(String currentEmail, String newEmail, String newPassword, String newFullName, String newPhoneNo, String newDeliveryAddress) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        User user = userDAO.findUserByEmail(currentEmail);

        //User ID is not modified in this context.
        //User role is not modified in this context.
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        user.setFullName(newFullName);
        user.setPhoneNo(newPhoneNo);
        user.setDeliveryAddress(newDeliveryAddress);

        userDAO.updateUser(user, user); //ID is used for identifying current user, so there is no conflict here.
    }

    public void changeRoleToAdmin(String email) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        User user = userDAO.findUserByEmail(email);
        user.setCurrentRole(User.Role.ADMIN);
        userDAO.updateUser(user, user); //ID is used for identifying current user, so there is no conflict here.
    }

    public void changeRoleToShopper(String email) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        User user = userDAO.findUserByEmail(email);
        user.setCurrentRole(User.Role.SHOPPER);
        userDAO.updateUser(user, user); //ID is used for identifying current user, so there is no conflict here.
    }

    public void deleteUser(String ID) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        userDAO.removeUser(userDAO.findUserByID(parseInt(ID)));
    }

    public boolean doesUserExist(String email) throws ShopFileNotFoundException {
        try {
            return (userDAO.findUserByEmail(email) != null);
        } catch (ShopEntryNotFoundException e) {
            return false;
        }
    }

    public User findUserByEmail(String email) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        return userDAO.findUserByEmail(email);
    }

    public boolean doCredentialsMatch(String email, String password) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        User user = userDAO.findUserByEmail(email);
        String userPassword = user.getPassword();
        return password.matches(userPassword);
    }

    /**
     * Extra: methods that print out relevant information about Users:
     */

    public void outputUserList(ArrayList<User> users) {
        for (User user : users) {
            ArrayList<String> userInfo = new ArrayList<>();

            userInfo.add("ROLE:       " + user.getCurrentRole());
            userInfo.add("EMAIL:      " + user.getEmail());
            userInfo.add("FULL NAME:  " + user.getFullName());
            userInfo.add("PHONE NO.:  " + user.getPhoneNo());
            userInfo.add("");
            userInfo.add("ID:         " + user.getID());

            OutputFrame.printList(userInfo);
        }
    }

    public void outputUserCompleteInfo(User user) throws ShopFileNotFoundException, ShopEntryNotFoundException {
        ArrayList<String> userInfo = new ArrayList<>();

        userInfo.add("ROLE:       " + user.getCurrentRole());
        userInfo.add("EMAIL:      " + user.getEmail());
        userInfo.add("PASSWORD:   " + user.getPassword());
        userInfo.add("FULL NAME:  " + user.getFullName());
        userInfo.add("PHONE NO.:  " + user.getPhoneNo());
        userInfo.add("");
        userInfo.add("ID:         " + user.getID());

        OutputFrame.printList(userInfo);
    }
}