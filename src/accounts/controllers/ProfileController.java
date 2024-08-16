package accounts.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import accounts.models.Patient;
import accounts.services.UserManagementService;
import core.AppConfig;
import datacompute.services.SurvivalRate;
import utils.interractions.Reset;
import utils.user.PatientDetailsUpdater;
import utils.user.PatientUtils;

public class ProfileController {
    private static final UserManagementService userService = new UserManagementService();

    public static void viewPatientDetails(String email) {

        try {

            Patient patient = PatientUtils.getPatientByEmail(email);
            if (patient != null) {

                // Display patient details excluding the password
                System.out.println("Patient Details:");
                System.out.println("First Name: " + patient.getFirstName());
                System.out.println("Last Name: " + patient.getLastName());
                // System.out.println("Role: " + details[4]);
                System.out.println("Date of Birth: " + patient.getDateOfBirth());
                System.out.println("HIV Positive: " + patient.isHivPositive());
                System.out.println("Diagnosis Date: " + patient.getDiagnosisDate());
                System.out.println("On ART: " + patient.isOnArt());
                System.out.println("ART Start Date: " +patient.getArtStartDate());
                System.out.println("Country Code: " + patient.getCountryCode());            } else {
                System.err.println("Patient not found.");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving patient details: " + e.getMessage());
        }
    }

    public static void editPatientProfile(String email, String action) {
        try {
            Patient patient = PatientUtils.getPatientByEmail(email);
            if (patient != null) {
             
                // Use PatientDetailsUpdater to allow the user to update their profile
                patient = PatientDetailsUpdater.updateDetails(patient, action);

                // Update the patient profile with the new values
                String updateResult = userService.editUserProfile(patient);

                if (!updateResult.startsWith("SUCCESS")) {
                    System.out.println("Failed to update patient profile: " + updateResult);
                } else {
                    Reset.clearConsole();
                    System.out.println("Profile update completed.");
                    System.out.println(SurvivalRate.calculateSurvivalRate(email));
                }
            } else {
                System.err.println("Patient not found.");
            }

        } catch (Exception e) {
            System.err.println("An error occurred while updating the patient profile: " + e.getMessage());
        }
    }

    public static void downloadDeathScheduleICS(String email) {
        String icalendarFile = AppConfig.getIcalendarPath();
        String survivalRateStr = SurvivalRate.calculateSurvivalRate(email);
        if (survivalRateStr.startsWith("Error") || survivalRateStr.startsWith("Patient not found")) {
            System.out.println(survivalRateStr);
            return;
        }

        // Extract the survival rate in years from the string
        double survivalRateYears = Double.parseDouble(survivalRateStr.replaceAll("[^0-9.]", ""));

        // Get the patient's birthdate
        Patient patient = PatientUtils.getPatientByEmail(email);
        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        LocalDate expectedDeathDate = LocalDate.now().plusYears((long) survivalRateYears);

        String icsContent = "BEGIN:VCALENDAR\n" +
                "VERSION:2.0\n" +
                "BEGIN:VEVENT\n" +
                "DTSTART:" + expectedDeathDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "\n" +
                "SUMMARY:Your Estimated end of life span\n" +
                "DESCRIPTION:Based on the calculated survival rate, the expected end of life span\n" +
                "END:VEVENT\n" +
                "END:VCALENDAR";

        // Save the ICS file
        try {
            Reset.clearConsole();
            Files.write(Paths.get(icalendarFile), icsContent.getBytes());
            System.out.println("ICS schedule downloaded: " + icalendarFile);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the ICS file: " + e.getMessage());
        }
    }

 
}
