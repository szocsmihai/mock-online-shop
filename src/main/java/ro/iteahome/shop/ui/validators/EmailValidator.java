package ro.iteahome.shop.ui.validators;

public class EmailValidator extends Validator {

    public EmailValidator() {
        this.setRegex(".+@.+\\.\\w+"); //TODO: Replace this regex with one more specific to the standard email composition rules.
    }
}