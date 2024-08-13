package accounts.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import accounts.models.Patient;
import accounts.services.UserManagementService;
import datacompute.services.SurvivalRate;
import utils.user.PatientDetailsUpdater;
import utils.user.SessionUtils;

public class ProfileController {
    private static final UserManagementService userService = new UserManagementService();

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
            Patient patient = SessionUtils.getPatientByEmail(email);
            if (patient != null) {
             
                // Use PatientDetailsUpdater to allow the user to update their profile
                patient = PatientDetailsUpdater.updateDetails(patient);

                // Update the patient profile with the new values
                String updateResult = userService.editUserProfile(patient);

                if (!updateResult.startsWith("SUCCESS")) {
                    System.out.println("Failed to update patient profile: " + updateResult);
                } else {
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
        String icalendarFile = "expected_death_schedule.ics";
        String survivalRateStr = SurvivalRate.calculateSurvivalRate(email);
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
