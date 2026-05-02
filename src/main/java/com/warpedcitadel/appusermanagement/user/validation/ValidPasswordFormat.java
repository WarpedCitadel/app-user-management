package com.warpedcitadel.appusermanagement.user.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPasswordFormat implements ConstraintValidator<PasswordFormat, String> {

//    Min. 8 chars, Max. 18 chars, 1 Upper, 1 Lower, 1 Number
    private static final String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,18}$";
    private static final Pattern pattern = Pattern.compile(regex);

    @Override
    public boolean isValid(String passwordHash, ConstraintValidatorContext context){
        String stripPasswordHash = passwordHash.strip();
        if (stripPasswordHash.isBlank() || stripPasswordHash.isEmpty()) {
            return false;
        }
        Matcher matcher = pattern.matcher(stripPasswordHash);
        return matcher.matches();
    }
}
