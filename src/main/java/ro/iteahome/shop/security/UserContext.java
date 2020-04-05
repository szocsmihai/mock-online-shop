package ro.iteahome.shop.security;

import ro.iteahome.shop.model.User;

public class UserContext {

    private static User loggedInUser;

    public static boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    public static boolean isAdminLoggedIn() {
        return (loggedInUser != null && loggedInUser.getCurrentRole().equals(User.Role.ADMIN));
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        UserContext.loggedInUser = loggedInUser;
    }
}