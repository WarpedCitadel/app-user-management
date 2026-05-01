package com.warpedcitadel.appusermanagement.user.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUsernameFormat {

//    Alphanumeric, underscores, dots, or hyphens. 3–20 characters.
//    No symbols at the start/end. No double symbols
    private static final String regex = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,18}[a-zA-Z0-9]$";
    private static final Pattern pattern = Pattern.compile(regex);

    public boolean isValid(String username){
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
