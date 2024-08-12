package accounts.controllers;

import java.time.LocalDate;
import java.util.Scanner;

import accounts.models.Patient;
import accounts.services.RegistrationService;
import datacompute.services.CountryService;
import utils.interractions.Reset;
import utils.user.RegistrationUtils;
import utils.validators.BinaryDecisionValidator;
import utils.validators.DateValidator;
import utils.validators.EmailValidator;
import utils.validators.InputValidator;
import utils.validators.PasswordValidator;




public class PatientRegistrationController {
    private static final RegistrationService registrationService = new RegistrationService();
    private static final PasswordValidator passwordValidator = new PasswordValidator();
    private static final InputValidator inputValidator = new InputValidator();
    private static final EmailValidator emailValidator = new EmailValidator();
    private static final BinaryDecisionValidator decisionValidator = new BinaryDecisionValidator();
 
    public static void completePatientRegistration() {
        Reset.clearConsole();
        // Check if UUID exists
        System.out.println("Complete your Registration\n");

        String uuid;
        while (true) {
            uuid = inputValidator.getNonEmptyInput("Enter Your Code or (99 to cancel): ");
            if (uuid.equals("99")) {
                Reset.clearConsole(); // Clear console before returning to the main menu
                System.out.println("Cancelling registration and returning to the main menu...");

                return; // Exit the login method and return to the main menu
            } else {

                if (!RegistrationUtils.isUUIDValid(uuid)) {
                    Reset.clearConsole();
                    System.out.println("Unable to verify code. Please contact the admin for assistance.");
                    return;
                } else {
                    break;
                }
            }

        }
        String firstName = inputValidator.getNonEmptyInput("Enter first name: ");
        String lastName = inputValidator.getNonEmptyInput("Enter last name: ");
        String  email = emailValidator.getValidEmail("Enter email: ");
        String password = passwordValidator.getPassword();

        // Validate date format for date of birth
        LocalDate dateOfBirth = null;
        DateValidator dateValidator = new DateValidator();
        while (dateOfBirth == null) {
            String dobInput = inputValidator.getNonEmptyInput("Enter date of birth (YYYY-MM-DD): ");
            if (dateValidator.validate(dobInput)) {
                dateOfBirth = LocalDate.parse(dobInput);
            } else {
                System.err.println(dateValidator.getErrorMessage());
            }
        }

        // Validate HIV status and related dates
        boolean isHIVPositive = decisionValidator. getBinaryDecision("Are you HIV positive? (1. Yes / 0. No): ");

        LocalDate diagnosisDate = null;
        boolean isOnART = false;
        LocalDate artStartDate = null;

        if (isHIVPositive) {
            while (diagnosisDate == null) {
                String diagnosisInput = inputValidator.getNonEmptyInput("Enter diagnosis date (YYYY-MM-DD): ");
                if (dateValidator.validate(diagnosisInput)) {
                    diagnosisDate = LocalDate.parse(diagnosisInput);
                    if (!dateValidator.validateDateOrder(dateOfBirth, diagnosisDate, null)) {
                        System.out.println(dateValidator.getErrorMessage());
                        diagnosisDate = null;
                    }
                } else {
                    System.out.println(dateValidator.getErrorMessage());
                }
            }

            isOnART = decisionValidator. getBinaryDecision("Are you on ART drugs? (1. Yes / 0. No): ");

            if (isOnART) {
                while (artStartDate == null) {
                    String artStartInput = inputValidator.getNonEmptyInput("Enter ART start date (YYYY-MM-DD): ");
                    if (dateValidator.validate(artStartInput)) {
                        artStartDate = LocalDate.parse(artStartInput);
                        if (!dateValidator.validateDateOrder(null, diagnosisDate, artStartDate)) {
                            System.out.println(dateValidator.getErrorMessage());
                            artStartDate = null;
                        }
                    } else {
                        System.out.println(dateValidator.getErrorMessage());
                    }
                }
            }
        }

        String countryCode = CountryService.getCountryCode(); // Assuming getCountryCode() method is implemented

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

        boolean confirmation = decisionValidator. getBinaryDecision("\nIs this information correct? (1. Yes / 0. No): ");
        if (!confirmation) {
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
