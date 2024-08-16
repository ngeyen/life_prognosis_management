package accounts.controllers;

import java.time.LocalDate;

import accounts.models.Patient;
import accounts.services.RegistrationService;
import datacompute.services.CountryService;
import utils.interractions.Reset;
import utils.user.PatientDetailsUpdater;
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
        System.out.println("Complete your Registration\n" +
        "======================\n");

        String uuid;
        while (true) {
            uuid = inputValidator.getNonEmptyInput("Enter Your Code or (99 to cancel): ");
            if (uuid.equals("99")) {
                Reset.clearConsole(); // Clear console before returning to the main menu
                System.out.println("Cancelling registration and returning to the main menu...");
                return;
            }

            if (!RegistrationUtils.isUUIDValid(uuid)) {
                Reset.clearConsole();
                System.out.println("Unable to verify code. Please contact the admin for assistance.");
                return;
            } else {
                break;
            }
        }

        // Collect user information
        String firstName = inputValidator.getNonEmptyInput("\nFirst name: ");
        String lastName = inputValidator.getNonEmptyInput("Last name: ");
        String email = emailValidator.getValidEmail("E-mail address: "); 
        String password = passwordValidator.getPassword();
        System.out.println("\n\nHIV Diagnosis Details:");
        // Validate date of birth
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

        // HIV Status and related dates
        boolean isHivPositive = decisionValidator.getBinaryDecision("Are you HIV positive? (1. Yes / 0. No): ");

        LocalDate diagnosisDate = null;
        boolean isOnArt = false;
        LocalDate artStartDate = null;

        if (isHivPositive) {
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

            isOnArt = decisionValidator.getBinaryDecision("Are you on ART drugs? (1. Yes / 0. No): ");

            if (isOnArt) {
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

        // Get country code
        String countryCode = CountryService.getCountryCode();
        Patient patient = new Patient(firstName, lastName, email, password, dateOfBirth, isHivPositive, diagnosisDate, isOnArt, artStartDate, countryCode, null);

        // Allow user to review and update details
        patient = PatientDetailsUpdater.updateDetails(patient, "Review Details");

        // Confirm and finalize registration
        boolean confirmation = decisionValidator.getBinaryDecision("\nIs this information correct? (1. Yes / 0. No): ");
        if (!confirmation) {
            System.err.println("Registration cancelled.");
            return;
        }

        boolean success = registrationService.createUser(patient);

        if (success) {
            Reset.clearConsole();
            
            System.out.println("Patient registration completed successfully.");

        } else {
            System.err.println("Failed to complete registration. Please try again.");
        }
    }
}
