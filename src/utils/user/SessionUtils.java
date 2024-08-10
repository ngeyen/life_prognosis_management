package utils.user;

import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import accounts.models.Patient;
import core.AppConfig;
import core.BashConnect;
import utils.enums.ExportType;

public class SessionUtils {

    private static final Logger logger = Logger.getLogger("UserManager");

    public static String getLifeExpectancy(String email) {

        try {
            return BashConnect.run(AppConfig.getSurvialRateScript(), "country_le", email);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving life expectancy", e);
            throw new RuntimeException("Failed to retrieve life expectancy", e);
        }
    }

    // Fetch patient details by email
    public static Patient getPatientByEmail(String email) {
        try {
            String result = BashConnect.run(AppConfig.getUserMangerScript(), "get_patient", email);

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

    public static void downloadCSV(ExportType type) {
        if (type == ExportType.PATIENT_INFO) {
            DataExport dataExport = new DataExport();
            dataExport.exportPatientData("assets/downloads/patient_data.csv");
        }
    }
}