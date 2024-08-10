package helpers;

import UserManager.models.Patient;
import statistics.services.SurvivalRate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserUtils {
    private static final String SURVIVAL_RATE = "./assets/scripts/survival_rate.sh";
    private static final String BASH_SCRIPT = "./assets/scripts/user_manager.sh";
    private static final Logger logger = Logger.getLogger("UserManager");

    public static String getLifeExpectancy(String email) {
        try {
            return BashConnect.run(SURVIVAL_RATE, "country_le", email);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving life expectancy", e);
            throw new RuntimeException("Failed to retrieve life expectancy", e);
        }
    }

    // Fetch patient details by email
    public static Patient getPatientByEmail(String email) {
        try {
            String result = BashConnect.run(BASH_SCRIPT, "get_patient", email);

            if (result.startsWith("SUCCESS:")) {
                String[] parts = result.split(":")[1].trim().split(",");

                // Assuming the order matches your previous bash script order
                String uuid = parts[1];
                String firstName = parts[2];
                String lastName = parts[3];
                // String role = parts[4];
                LocalDate dateOfBirth = LocalDate.parse(parts[5]);
                boolean isHIVPositive = Boolean.parseBoolean(parts[6]);
                LocalDate diagnosisDate = !parts[7].isEmpty() ? LocalDate.parse(parts[7]) : null;
                boolean isOnART = Boolean.parseBoolean(parts[8]);
                LocalDate artStartDate = !parts[9].isEmpty() ? LocalDate.parse(parts[9]) : null;
                String countryCode = parts[10];

                return new Patient(firstName, lastName, email, uuid, dateOfBirth, isHIVPositive, diagnosisDate, isOnART,
                        artStartDate, countryCode);
            } else {
                System.out.println("RESULTS: " + result);

                logger.log(Level.WARNING, "User not found.");
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error Finding patient", e.getMessage());
            return null;
        }
    }

    public static void downloadDeathScheduleICS(String email) {
        String icalendarFile = "download/expected_death_schedule.ics";
        String survivalRateStr = SurvivalRate.calculateSurvivalRate(email);
        if (survivalRateStr.startsWith("Error") || survivalRateStr.startsWith("Patient not found")) {
            System.out.println(survivalRateStr);
            return;
        }

        // Extract the survival rate in years from the string
        double survivalRateYears = Double.parseDouble(survivalRateStr.replaceAll("[^0-9.]", ""));

        // Get the patient's birthdate
        Patient patient = UserUtils.getPatientByEmail(email);
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
            System.out.println("ICS schedule downloaded into " + icalendarFile);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the ICS file: " + e.getMessage());
        }
    }
}