package utils;

public class InputValidator {

    public static boolean isValidNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonEmptyString(String input) {
        return input != null && !input.trim().isEmpty();
    }
}
