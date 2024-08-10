package accounts.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import accounts.models.Admin;
import accounts.models.Patient;
import accounts.services.RegistrationService;
import utils.user.RegistrationUtils;

public class RegistrationController {
    private static final RegistrationService registrationService = new RegistrationService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void createAdmin() {
        System.out.print("Enter first name: ");

        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Admin admin = new Admin(firstName, lastName, email, password);
        boolean success = registrationService.createUser(admin);
        if (success) {
            System.out.println("Admin added created.");
        } else {
            System.out.println("Failed to create Admin. Please try again.");
        }
    }

    public static void initiateUserRegistration() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        String uuid = registrationService.initializeRegistration(email);
        if (uuid != null) {
            System.out.println("Registration initiated. Share this UUID with the user: " + uuid);
        } else {
            System.out.println("Failed to initiate registration. Please try again.");
        }
    }

    public static void completePatientRegistration() {
        // Check if UUID exists
        System.out.print("Enter patient UUID: ");
        String uuid = scanner.nextLine();
        if (!RegistrationUtils.isUUIDValid(uuid)) {
            System.out.println("UUID does not exist. Please contact the admin to initiate your registration.");
            return;
        }

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        // Validate password and ensure it matches
        String password, confirmPassword;
        do {
            System.out.print("Enter password: ");
            password = new String(System.console().readPassword());
            System.out.print("Confirm password: ");
            confirmPassword = new String(System.console().readPassword());

            if (!password.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Please try again.");
            }
        } while (!password.equals(confirmPassword));

        // Validate date format for date of birth
        LocalDate dateOfBirth = null;
        while (dateOfBirth == null) {
            System.out.print("Enter date of birth (YYYY-MM-DD): ");
            String dobInput = scanner.nextLine();
            try {
                dateOfBirth = LocalDate.parse(dobInput);
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date format. Please enter a valid date in the format YYYY-MM-DD (e.g., 2000-01-23).");
            }
        }

        System.out.print("Are you HIV positive? (true/false): ");
        boolean isHIVPositive = Boolean.parseBoolean(scanner.nextLine());

        LocalDate diagnosisDate = null;
        boolean isOnART = false;
        LocalDate artStartDate = null;

        if (isHIVPositive) {
            // Validate diagnosis date
            while (diagnosisDate == null) {
                System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
                String diagnosisInput = scanner.nextLine();
                try {
                    diagnosisDate = LocalDate.parse(diagnosisInput);
                    if (diagnosisDate.isBefore(dateOfBirth)) {
                        System.out.println("Diagnosis date cannot be before date of birth. Please enter a valid date.");
                        diagnosisDate = null;
                    }
                } catch (DateTimeParseException e) {
                    System.out.println(
                            "Invalid date format. Please enter a valid date in the format YYYY-MM-DD (e.g., 2000-01-23).");
                }
            }

            System.out.print("Are you on ART drugs? (true/false): ");
            isOnART = Boolean.parseBoolean(scanner.nextLine());

            if (isOnART) {
                // Validate ART start date
                while (artStartDate == null) {
                    System.out.print("Enter ART start date (YYYY-MM-DD): ");
                    String artStartInput = scanner.nextLine();
                    try {
                        artStartDate = LocalDate.parse(artStartInput);
                        if (artStartDate.isBefore(diagnosisDate)) {
                            System.out.println(
                                    "ART start date cannot be before diagnosis date. Please enter a valid date.");
                            artStartDate = null;
                        }
                    } catch (DateTimeParseException e) {
                        System.out.println(
                                "Invalid date format. Please enter a valid date in the format YYYY-MM-DD (e.g., 2000-01-23).");
                    }
                }
            }
        }

        System.out.print("Enter country code (ISO): ");
        String countryCode = scanner.nextLine();

        // Display details for confirmation
        System.out.println("\nPlease review the details:");
        System.out.println("UUID: " + uuid);
        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("HIV Positive: " + isHIVPositive);
        if (isHIVPositive) {
            System.out.println("Diagnosis Date: " + diagnosisDate);
            System.out.println("On ART: " + isOnART);
            if (isOnART) {
                System.out.println("ART Start Date: " + artStartDate);
            }
        }
        System.out.println("Country Code: " + countryCode);

        System.out.print("\nIs this information correct? (yes/no): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Registration cancelled.");
            return;
        }

        Patient patient = new Patient(firstName, lastName, email, password, dateOfBirth, isHIVPositive, diagnosisDate,
                isOnART, artStartDate, countryCode);

        boolean success = registrationService.createUser(patient);

        if (success) {
            System.out.println("Patient registration completed successfully.");
        } else {
            System.out.println("Failed to complete registration. Please try again.");
        }
    }

}
