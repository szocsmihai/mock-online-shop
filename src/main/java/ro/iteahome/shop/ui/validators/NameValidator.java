package ro.iteahome.shop.ui.validators;

public class NameValidator extends Validator {

    public NameValidator() {
        this.setRegex(".*"); //TODO: Create a more specific regex (letters, spaces and hyphens grouped by naming logic).
    }
}