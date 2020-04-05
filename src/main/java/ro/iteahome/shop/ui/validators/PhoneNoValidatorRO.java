package ro.iteahome.shop.ui.validators;

public class PhoneNoValidatorRO extends Validator {

    @Override
    public boolean isInputValid(String input) {
        return super.isInputValid(formatPhoneNo(input));
    }

    public String formatPhoneNo(String input) {
        String phoneNo = input.replaceFirst("\\+4", "004");
        phoneNo = phoneNo.replaceAll("\\D", "");
        if (!phoneNo.matches("^0040.+")) {
            phoneNo = "004" + phoneNo;
        }
        return phoneNo;
    }

    public PhoneNoValidatorRO() {
        this.setRegex("^0040\\d{9}$");
    }
}