package ro.iteahome.shop.ui.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Validator {

    String regex;

    public boolean isInputValid(String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}