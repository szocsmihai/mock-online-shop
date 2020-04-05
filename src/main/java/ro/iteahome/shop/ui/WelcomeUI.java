package ro.iteahome.shop.ui;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.security.UserContext;
import ro.iteahome.shop.service.UserService;
import ro.iteahome.shop.ui.options.UIOption;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;
import ro.iteahome.shop.ui.validators.EmailValidator;
import ro.iteahome.shop.ui.validators.PasswordValidator;

public class WelcomeUI extends SwitchUI {

    private String email;
    private String password;

    UserService userService = new UserService();
    EmailValidator emailValidator = new EmailValidator();
    PasswordValidator passwordValidator = new PasswordValidator();

    public WelcomeUI() {
        this.setMenuTitle("WELCOME MENU");

        this.uiOptions.add(new UIOption("1", "LOG IN", () -> {
            try {

                resetLocalCredentials();
                showCheckEmail();
                if (email != null) {
                    showCheckPassword();
                    if (password != null) {
                        userService.logIn(email, password);
                        OutputFrame.printConfirmation("CREDENTIALS ACCEPTED. USER LOGGED IN.");
                        if (!UserContext.isAdminLoggedIn()) {
                            new ShopperStartUI().start();
                        } else {
                            new AdminStartUI().start();
                        }
                    }
                }

            } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("2", "SIGN UP", () -> {
            try {

                resetLocalCredentials();
                showGetNewEmail();
                if (email != null) {
                    showGetNewPassword();
                    if (password != null) {
                        userService.signUp(email, password);
                        userService.logIn(email, password);
                        OutputFrame.printConfirmation("NEW USER CREATED AND LOGGED IN.");
                        new ShopperStartUI();
                    }
                }

            } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
                e.printStackTrace();
            }
        }));

        this.uiOptions.add(new UIOption("0", "LEAVE", () -> {
            OutputFrame.printInfo("COME BACK SOON.");
        }));

        this.setInstructionsBar();
    }

    private void showCheckEmail() throws ShopFileNotFoundException {
        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR EMAIL (0 : CANCEL):");
        while (!userInput.equals("0") && (!emailValidator.isInputValid(userInput) || !userService.doesUserExist(userInput))) {
            userInput = InputFrame.getInputFromAlert("INVALID OR UNREGISTERED EMAIL, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            email = userInput;
        }
    }

    private void showCheckPassword() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR PASSWORD (0 : CANCEL):");
        while (!userInput.equals("0") && (!passwordValidator.isInputValid(userInput) || !userService.doCredentialsMatch(email, userInput))) {
            userInput = InputFrame.getInputFromAlert("INVALID OR WRONG PASSWORD, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            password = userInput;
        }
    }

    private void showGetNewEmail() {
        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR EMAIL (0 : CANCEL):");
        while (!userInput.equals("0") && !emailValidator.isInputValid(userInput)) {
            userInput = InputFrame.getInputFromAlert("INVALID EMAIL ADDRESS, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            email = userInput;
        }
    }

    private void showGetNewPassword() throws ShopFileNotFoundException, ShopEntryNotFoundException {
        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR PASSWORD (0 : CANCEL):");
        while (!userInput.equals("0") && !passwordValidator.isInputValid(userInput)) {
            userInput = InputFrame.getInputFromAlert("PASSWORD TOO WEAK, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            password = userInput;
        }
    }

    private void resetLocalCredentials() {
        email = null;
        password = null;
    }
}