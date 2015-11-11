package com.partypeople.www.partypeople.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tacademy on 2015-11-11.
 */
public class Validate {
    private static Validate instance;
    public static Validate getInstance() {
        if (instance == null) {
            instance = new Validate();
        }
        return instance;
    }

    public boolean validEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(expression);
        matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public boolean validPsaaword(String password) {
        boolean isValid = false;
        if(password.length() >= 4 && password.length() <= 10) {
            isValid = true;
        }
        return isValid;
    }
}
