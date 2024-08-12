package accounts.controllers;

import accounts.models.Admin;
import accounts.services.RegistrationService;

import utils.validators.EmailValidator;
import utils.validators.InputValidator;
import utils.validators.PasswordValidator;

public class RegistrationController {
    private static final RegistrationService registrationService = new RegistrationService();

    private static final EmailValidator emailValidator = new EmailValidator();
    private static final PasswordValidator passwordValidator = new PasswordValidator();
    private static final InputValidator inputValidator = new InputValidator();

    public static void createAdmin() {

        String firstName = inputValidator.getNonEmptyInput("First Name: ");
        String lastName = inputValidator.getNonEmptyInput("Last Name: ");
        String email = emailValidator. getValidEmail("Email: ");
        String password = passwordValidator.getPassword();

        Admin admin = new Admin(firstName, lastName, email, password);
        boolean success = registrationService.createUser(admin);
        if (success) {
            System.out.println("Admin added created.");
        } else {
            System.out.println("Failed to create Admin. Please try again.");
        }
    }

    public static void initiateUserRegistration() {
        String email = emailValidator.getValidEmail("Patient Email: ");
        String uuid = registrationService.initializeRegistration(email);
        if (uuid != null) {
            System.out.println("Registration initiated. Share this UUID with the user: " + uuid);
        } else {
            System.out.println("Failed to initiate registration. Please try again.");
        }
    }
}
