package accounts.controllers;

import java.util.Scanner;

import accounts.services.AuthService;
import dashboard.controllers.AdminDashboardController;
import dashboard.controllers.PatientDashboardController;
import utils.enums.UserRole;
import utils.interractions.Reset;
import utils.validators.EmailValidator;
import utils.validators.PasswordValidator;

public class AuthController {
    private static final Scanner scanner = new Scanner(System.in);
    private static final EmailValidator emailValidator = new EmailValidator();

    public static void login() {
        while (true) { // Loop for retrying the login process
            Reset.clearConsole(); // Clear console before showing the login form
            System.out.println("=== Welcome back, Login ===");
            System.out.println("Provide your credentials to login or \npress 99 to cancel (Return to Main menu)\n");

            String email;

            // Email input and validation loop
            while (true) {
                System.out.print("Enter email / (press 99 to cancel): ");
                email = scanner.nextLine();

                if (email.equals("99")) {
                    System.out.println("Cancelling login and returning to the main menu...");
                    Reset.clearConsole(); // Clear console before returning to the main menu
                    return; // Exit the login method and return to the main menu
                }

                // Email validation using regex
                if (emailValidator.validate(email)) {
                    break; // Exit the email input loop if valid email is entered
                } else {
                    System.err.println(emailValidator.getErrorMessage());
                }
            }

            // Password input and validation loop
            while (true) {
                // Hiding password input
                String password = PasswordValidator. readPassword("Enter password (or press 99 to cancel): ");

                // Allow the user to cancel if they enter 99 as the password
                if (password.equals("99")) {
                    System.out.println("Cancelling login and returning to the main menu...");
                    Reset.clearConsole(); // Clear console before returning to the main menu
                    return;
                }

                // Verifying login credentials
                UserRole role = AuthService.verifyLoginCredentials(email, password);
                if (role != null) {
                    System.out.println("Login successful.");
                    Reset.clearConsole();
                    // Redirecting to dashboard based on user role
                    if (role == UserRole.ADMIN) {
                        AdminDashboardController.showDashboard();
                    } else {
                        PatientDashboardController.showDashboard(email);
                    }
                    return; // Exit after successful login
                } else {
                    // If credentials are incorrect, prompt for retry or cancel
                    System.out.print("\n==> Press 1 to retry, or 99 to cancel: ");
                    String choice = scanner.nextLine();
                    if (choice.equals("99")) {
                        System.out.println("Cancelling login and returning to the main menu...");
                        Reset.clearConsole();
                        return;
                    } else if (choice.equals("1")) {
                        // Clear the console and restart the login process
                        break; // Breaks out of the password loop to retry the entire login process
                    } else {
                        System.out.println("Invalid input. Cancelling login and returning to the main menu...");
                        Reset.clearConsole();
                        return;
                    }
                }
            }
        }
    }

}
