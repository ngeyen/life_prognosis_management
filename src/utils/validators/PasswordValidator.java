package utils.validators;

import java.io.Console;
import java.util.Scanner;

public class PasswordValidator implements Validator {
    private String errorMessage;
private static final Scanner scanner = new Scanner(System.in);
    @Override
    public boolean validate(String password) {
        if (password == null || password.isEmpty()) {
            errorMessage = "Password cannot be empty.";
            return false;
        }
        return true;
    }

    // Method to get and confirm the password
    public String getPassword() {
        while (true) {
            String password = readPassword("\nEnter password: ");
            String confirmPassword = readPassword("Confirm password: ");

            if (password.isEmpty()) {
                System.out.println("Please set a password");
            } else if (password.equals(confirmPassword)) {
                return password;
            } else {
                System.out.println("Passwords do not match. Please try again.");
            }
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
    
    // Method to read password with input hidden
    public static String readPassword(String prompt) {
        System.out.print(prompt);
        Console console = System.console();
        if (console != null) {
            char[] passwordArray = console.readPassword();
            return new String(passwordArray);
        } else {
            // Fallback to standard input if console is not available (e.g., in IDEs)
            return scanner.nextLine();
        }
    }
}
