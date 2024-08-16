package utils.user;

import java.time.LocalDate;

import accounts.models.Patient;
import datacompute.services.CountryService;
import utils.interractions.Reset;
import utils.validators.BinaryDecisionValidator;
import utils.validators.DateValidator;
import utils.validators.EmailValidator;
import utils.validators.InputValidator;

public class PatientDetailsUpdater {

    private static final InputValidator inputValidator = new InputValidator();
    private static final EmailValidator emailValidator = new EmailValidator();
    private static final DateValidator dateValidator = new DateValidator();
    private static final BinaryDecisionValidator decisionValidator = new BinaryDecisionValidator();

    public static Patient updateDetails(Patient patient, String action) {
        boolean updating = true;

        while (updating) {
            Reset.clearConsole();
            System.out.println(action +"\n====================");
            System.out.println("Review your profile Details:");
            System.out.println("1. First Name: " + patient.getFirstName());
            System.out.println("2. Last Name: " + patient.getLastName());
            System.out.println("3. Email: " + patient.getEmail());
            System.out.println("4. Date of Birth: " + patient.getDateOfBirth());
            System.out.println("5. HIV Positive: " + patient.isHivPositive());
            if (patient.isHivPositive()) {
                System.out.println("6. Diagnosis Date: " + patient.getDiagnosisDate());
                System.out.println("7. On ART: " + patient.isOnArt());
                if (patient.isOnArt()) {
                    System.out.println("8. ART Start Date: " + patient.getArtStartDate());
                }
            }
            System.out.println("9. Country Code: " + patient.getCountryCode());
            System.out.println("\n0. I'm Done! Submit");

            int choice = inputValidator.getValidatedIntegerInput(
                    "\nSelect the number of the field you want to update or press 0 to Submit: ");

            switch (choice) {
                case 1:
                    patient.setFirstName(inputValidator.getNonEmptyInput("Enter new first name: "));
                    break;
                case 2:
                    patient.setLastName(inputValidator.getNonEmptyInput("Enter new last name: "));
                    break;
                case 3:
                    patient.setEmail(emailValidator.getValidEmail("Enter new email: "));
                    break;
                case 4:
                    LocalDate dateOfBirth = null;
                    while (dateOfBirth == null) {
                        String dobInput = inputValidator.getNonEmptyInput("Enter new date of birth (YYYY-MM-DD): ");
                        if (dateValidator.validate(dobInput)) {
                            dateOfBirth = LocalDate.parse(dobInput);
                            patient.setDateOfBirth(dateOfBirth);
                        } else {
                            System.out.println(dateValidator.getErrorMessage());
                        }
                    }
                    break;
                case 5:
                    boolean isHivPositive = decisionValidator.getBinaryDecision(
                            "Are you HIV positive? (1. Yes / 0. No): ");
                    patient.setHivPositive(isHivPositive);

                    // If HIV status changes, reset related fields
                    if (!isHivPositive) {
                        patient.setDiagnosisDate(null);
                        patient.setOnArt(false);
                        patient.setArtStartDate(null);
                    }
                    break;
                case 6:
                    if (patient.isHivPositive()) {
                        LocalDate diagnosisDate = null;
                        while (diagnosisDate == null) {
                            String diagnosisInput = inputValidator
                                    .getNonEmptyInput("Enter new diagnosis date (YYYY-MM-DD): ");
                            if (dateValidator.validate(diagnosisInput)) {
                                diagnosisDate = LocalDate.parse(diagnosisInput);
                                patient.setDiagnosisDate(diagnosisDate);
                            } else {
                                System.out.println(dateValidator.getErrorMessage());
                            }
                        }
                    } else {
                        System.out.println("Diagnosis date is only applicable if HIV positive.");
                    }
                    break;
                case 7:
                    if (patient.isHivPositive()) {
                        boolean isOnArt = decisionValidator.getBinaryDecision(
                                "Are you on ART drugs? (1. Yes / 0. No): ");
                        patient.setOnArt(isOnArt);

                        // If ART status changes, reset the ART start date
                        if (!isOnArt) {
                            patient.setArtStartDate(null);
                        }
                    } else {
                        System.out.println("ART status is only applicable if HIV positive.");
                    }
                    break;
                case 8:
                    if (patient.isHivPositive() && patient.isOnArt()) {
                        LocalDate artStartDate = null;
                        while (artStartDate == null) {
                            String artStartInput = inputValidator
                                    .getNonEmptyInput("Enter new ART start date (YYYY-MM-DD): ");
                            if (dateValidator.validate(artStartInput)) {
                                artStartDate = LocalDate.parse(artStartInput);
                                patient.setArtStartDate(artStartDate);
                            } else {
                                System.out.println(dateValidator.getErrorMessage());
                            }
                        }
                    } else {
                        System.out.println("ART start date is only applicable if HIV positive and on ART.");
                    }
                    break;
                case 9:
                    String countryCode = CountryService.getCountryCode();
                    if (countryCode != null) {
                        patient.setCountryCode(countryCode);
                    }
                    break;
                case 0:
                    updating = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }

        return patient;
    }
}
