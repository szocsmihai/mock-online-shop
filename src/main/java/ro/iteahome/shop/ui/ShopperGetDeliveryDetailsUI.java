package ro.iteahome.shop.ui;

import ro.iteahome.shop.exceptions.business.ShopEntryNotFoundException;
import ro.iteahome.shop.exceptions.technical.ShopFileNotFoundException;
import ro.iteahome.shop.security.UserContext;
import ro.iteahome.shop.service.UserService;
import ro.iteahome.shop.ui.popups.InputFrame;
import ro.iteahome.shop.ui.popups.OutputFrame;
import ro.iteahome.shop.ui.validators.NameValidator;
import ro.iteahome.shop.ui.validators.PhoneNoValidatorRO;

public class ShopperGetDeliveryDetailsUI {

    UserService userService = new UserService();

    public void start() {
        OutputFrame.printInfo("VALID BILLING DETAILS ARE NEEDED FOR COMPLETING ORDERS.");
        showGetValidDeliveryDetails();
    }

    private void showGetValidDeliveryDetails() {
        try {

            String email = UserContext.getLoggedInUser().getEmail();
            String password = UserContext.getLoggedInUser().getPassword();

            String firstName = showGetFirstName();
            if (firstName != null) {
                String lastName = showGetLastName();
                if (lastName != null) {
                    String fullName = firstName + ", " + lastName;
                    String phoneNo = showGetPhoneNo();
                    if (phoneNo != null) {
                        String deliveryAddress = showGetDeliveryAddress();
                        if (deliveryAddress != null) {
                            userService.updateUserInfo(email, email, password, fullName, phoneNo, deliveryAddress);
                            userService.logIn(email, password);
                        }
                    }
                }
            }

        } catch (ShopFileNotFoundException | ShopEntryNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String showGetFirstName() {
        String firstName = null;
        NameValidator nameValidator = new NameValidator();

        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR FIRST (GIVEN) NAME (0 : CANCEL):");
        while (!userInput.equals("0") && !nameValidator.isInputValid(userInput)) {
            userInput = InputFrame.getInputFromAlert("INVALID INPUT, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            firstName = userInput;
        }
        return firstName;
    }

    private String showGetLastName() {
        String lastName = null;
        NameValidator nameValidator = new NameValidator();

        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR LAST (FAMILY) NAME (0 : CANCEL):");
        while (!userInput.equals("0") && !nameValidator.isInputValid(userInput)) {
            userInput = InputFrame.getInputFromAlert("INVALID INPUT, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            lastName = userInput;
        }
        return lastName;
    }

    private String showGetPhoneNo() {
        String phoneNo = null;
        PhoneNoValidatorRO phoneNoValidatorRO = new PhoneNoValidatorRO();

        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR ROMANIAN PHONE NUMBER (0 : CANCEL):");
        while (!userInput.equals("0") && !phoneNoValidatorRO.isInputValid(userInput)) {
            userInput = InputFrame.getInputFromAlert("INVALID INPUT, TRY AGAIN (0 : CANCEL):");
        }
        if (!userInput.equals("0")) {
            phoneNo = phoneNoValidatorRO.formatPhoneNo(userInput);
        }
        return phoneNo;
    }

    private String showGetDeliveryAddress() {
        String deliveryAddress = null;
        String userInput = InputFrame.getInputFromPrompt("ENTER YOUR DELIVERY ADDRESS (0 : CANCEL):");
        if (!userInput.equals("0")) {
            deliveryAddress = userInput;
        }
        return deliveryAddress;
    }
}