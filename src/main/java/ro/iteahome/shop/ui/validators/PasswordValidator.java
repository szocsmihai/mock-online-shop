package ro.iteahome.shop.ui.validators;

public class PasswordValidator extends Validator {

    public PasswordValidator() {

        //Min. 1 lowercase letter;
        //Min. 1 uppercase letter;
        //Min. 1 numeric digit;
        //Min. 8, max. 32 characters;

        this.setRegex("((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,32})");
    }
}