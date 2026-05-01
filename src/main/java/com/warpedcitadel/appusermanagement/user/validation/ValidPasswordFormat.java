package com.warpedcitadel.appusermanagement.user.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPasswordFormat {

//    Min. 8 chars, Max. 18 chars, 1 Upper, 1 Lower, 1 Number
    private static final String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,18}$";
    private static final Pattern pattern = Pattern.compile(regex);

    public boolean isValid(String passwordHash){
        Matcher matcher = pattern.matcher(passwordHash);
        return matcher.matches();
    }
}
