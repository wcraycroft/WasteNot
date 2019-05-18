package cs134.miracosta.wastenot.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Validator {
    private final static Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    /**
     * Validate if a text is not empty or blank
     *
     * @param text the text to validateEmail
     * @return true if text is not empty otherwise false
     */
    public static boolean validateText(String text) {
        return !TextUtils.isEmpty(text) && text.trim().length() > 0;
    }

    /**
     * validateEmail an email address
     *
     * @param email the address to validateEmail
     * @return true if valid otherwise false
     */
    public static boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
}
