package Logic;

//class for input(email) validation.

public class Validators {
    /**
     * Checks if the provided string is a valid email address (basic regex).
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}