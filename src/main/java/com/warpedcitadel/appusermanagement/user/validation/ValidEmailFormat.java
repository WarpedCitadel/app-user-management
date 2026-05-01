package com.warpedcitadel.appusermanagement.user.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidEmailFormat {

    private static final String regex = "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
            + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
    private static final Pattern pattern = Pattern.compile(regex);

    public boolean isValid(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
