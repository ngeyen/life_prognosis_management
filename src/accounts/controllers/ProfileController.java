package accounts.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import accounts.models.Patient;
import accounts.services.UserManagementService;
import datacompute.services.SurvivalRate;
import utils.user.SessionUtils;

public class ProfileController {
    private static final UserManagementService userService = new UserManagementService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void viewPatientDetails(String email) {

        try {
            String result = userService.getUserDetail(email);

            if (result.startsWith("SUCCESS")) {
                String[] details = result.replace("SUCCESS: ", "").split(",");

                // Display patient details excluding the password
                System.out.println("Patient Details:");
                System.out.println("First Name: " + details[2]);
                System.out.println("Last Name: " + details[3]);
                // System.out.println("Role: " + details[4]);
                System.out.println("Date of Birth: " + details[5]);
                System.out.println("HIV Positive: " + details[6]);
                System.out.println("Diagnosis Date: " + details[7]);
                System.out.println("On ART: " + details[8]);
                System.out.println("ART Start Date: " + details[9]);
                System.out.println("Country Code: " + details[10]);
            } else {
                System.out.println("Patient not found.");

            }
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving patient details: " + e.getMessage());
        }
    }

    public static void editPatientProfile(String email) {
        try {
            String result = userService.getUserDetail(email);

            if (result.startsWith("SUCCESS")) {
                String[] details = result.replace("SUCCESS: ", "").split(",");
                String currentFirstName = details[2];
                String currentLastName = details[3];
                String currentDateOfBirth = details[5];
                String currentHIVStatus = details[6];
                String currentDiagnosisDate = details[7];
                String currentOnARTStatus = details[8];
                String currentARTStartDate = details[9];
                String currentCountryCode = details[10];

                boolean keepUpdating = true;

                while (keepUpdating) {
                    System.out.println("\nChoose the field you want to update:");
                    System.out.println("1. First Name (" + currentFirstName + ")");
                    System.out.println("2. Last Name (" + currentLastName + ")");
                    System.out.println("3. Date of Birth (" + currentDateOfBirth + ")");
                    System.out.println("4. HIV Positive (" + currentHIVStatus + ")");
                    System.out.println("5. Diagnosis Date (" + currentDiagnosisDate + ")");
                    System.out.println("6. On ART (" + currentOnARTStatus + ")");
                    System.out.println("7. ART Start Date (" + currentARTStartDate + ")");
                    System.out.println("8. Country ISO Code (" + currentCountryCode + ")");
                    System.out.println("0.  Back");

                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            System.out.print("Update First Name (" + currentFirstName + "): ");
                            currentFirstName = scanner.nextLine();
                            break;
                        case 2:
                            System.out.print("Update Last Name (" + currentLastName + "): ");
                            currentLastName = scanner.nextLine();
                            break;
                        case 3:
                            System.out.print("Update Date of Birth (" + currentDateOfBirth + ") (yyyy-mm-dd): ");
                            currentDateOfBirth = scanner.nextLine();
                            break;
                        case 4:
                            System.out.print("Update HIV Positive (" + currentHIVStatus + ") (true/false): ");
                            currentHIVStatus = scanner.nextLine();
                            break;
                        case 5:
                            System.out.print("Update Diagnosis Date (" + currentDiagnosisDate + ") (yyyy-mm-dd): ");
                            currentDiagnosisDate = scanner.nextLine();
                            break;
                        case 6:
                            System.out.print("Update On ART (" + currentOnARTStatus + ") (true/false): ");
                            currentOnARTStatus = scanner.nextLine();
                            break;
                        case 7:
                            System.out.print("Update ART Start Date (" + currentARTStartDate + ") (yyyy-mm-dd): ");
                            currentARTStartDate = scanner.nextLine();
                            break;
                        case 8:
                            System.out.print("Update Country ISO Code (" + currentCountryCode + "): ");
                            currentCountryCode = scanner.nextLine();
                            break;
                        case 0:
                            keepUpdating = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            continue;
                    }

                    // Update the patient profile with the new values
                    String updateResult = userService.editUserProfile(
                            email, currentFirstName, currentLastName, currentDateOfBirth,
                            Boolean.parseBoolean(currentHIVStatus), currentDiagnosisDate,
                            Boolean.parseBoolean(currentOnARTStatus), currentARTStartDate, currentCountryCode);

                    if (!updateResult.startsWith("SUCCESS")) {
                        System.out.println("Failed to update patient profile: " + updateResult);
                    }
                }

                System.out.println("Profile update completed.");
                System.out.println(SurvivalRate.calculateSurvivalRate(email));

            } else {
                System.out.println("Patient not found.");
            }

        } catch (Exception e) {
            System.out.println("An error occurred while updating the patient profile: " + e.getMessage());
        }
    }

    public static void downloadDeathScheduleICS(String email) {
        String icalendarFile = "expected_death_schedule.ics";
        String survivalRateStr =

                SurvivalRate.calculateSurvivalRate(email);
        if (survivalRateStr.startsWith("Error") || survivalRateStr.startsWith("Patient not found")) {
            System.out.println(survivalRateStr);
            return;
        }

        // Extract the survival rate in years from the string
        double survivalRateYears = Double.parseDouble(survivalRateStr.replaceAll("[^0-9.]", ""));

        // Get the patient's birthdate
        Patient patient = SessionUtils.getPatientByEmail(email);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        LocalDate expectedDeathDate = LocalDate.now().plusYears((long) survivalRateYears);

        String icsContent = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "BEGIN:VEVENT\n" +
                "DTSTART:" + expectedDeathDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "\n" +
                "SUMMARY:Expected Date of Death\n" +
                "DESCRIPTION:Based on the calculated survival rate, the expected date of death.\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

        // Save the ICS file
        try {
            Files.write(Paths.get(icalendarFile), icsContent.getBytes());
            System.out.println("ICS schedule downloaded: " + icalendarFile);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the ICS file: " + e.getMessage());
        }
    }
}
