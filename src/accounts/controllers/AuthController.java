package accounts.controllers;

import java.io.Console;
import java.util.Scanner;

import accounts.services.AuthService;
import dashboard.controllers.AdminDashboardController;
import dashboard.controllers.PatientDashboardController;
import utils.enums.UserRole;
import utils.validators.EmailValidator;
import utils.validators.Validator;

public class AuthController {
    private static final Scanner scanner = new Scanner(System.in);
    private static Validator emailValidator = new EmailValidator();

    public static void login() {
        String email;
        while (true) {
            System.out.print("Enter email: ");
            email = scanner.nextLine();

            // Email validation using regex
            if (emailValidator.validate(email)) {
                break;
            } else {
                System.out.println("Invalid email format. Please try again.");
            }
        }

        // Hiding password input
        String password = readPassword("Enter password: ");

        // Verifying login credentials
        UserRole role = AuthService.verifyLoginCredentials(email, password);
        if (role != null) {
            System.out.println("Login successful.");
            // Redirecting to dashboard based on user role
            if (role == UserRole.ADMIN) {
                AdminDashboardController.showDashboard();
            } else {
                PatientDashboardController.showDashboard(email);
            }
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    // Method to read password with input hidden
    private static String readPassword(String prompt) {
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
