package helpers;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

public class DataExport {

    // Define the headers for the CSV file
    private final String[] HEADERS = {
            "First Name",
            "Last Name",
            "Email",
            "Date of Birth",
            "HIV Positive",
            "Diagnosis Date",
            "On ART",
            "ART Start Date",
            "Country Code"
    };

    // Method to export patient data to a CSV file
    public void exportPatientData(String exportFileName) {
        String USER_STORE = "data/user-store.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_STORE));
                FileWriter writer = new FileWriter(exportFileName)) {

            // Write the headers
            for (int i = 0; i < HEADERS.length; i++) {
                writer.append(HEADERS[i]);
                if (i < HEADERS.length - 1) {
                    writer.append(",");
                }
            }
            writer.append("\n");

            // Read the user-store file line by line
            String record;
            while ((record = reader.readLine()) != null) {
                String[] entry = record.split(",");

                // Check if the user is a patient
                if (entry.length >= 12 && "patient".equalsIgnoreCase(entry[5])) {
                    // Write the patient's data to the CSV file
                    writer.append(entry[2]).append(",") // First Name
                            .append(entry[3]).append(",") // Last Name
                            .append(entry[0]).append(",") // Email
                            .append(entry[6]).append(",") // Date of Birth
                            .append(entry[7]).append(",") // HIV Positive
                            .append(entry[8]).append(",") // Diagnosis Date
                            .append(entry[9]).append(",") // On ART
                            .append(entry[10]).append(",") // ART Start Date
                            .append(entry[11]).append("\n"); // Country Code
                }
            }

            writer.flush();
            System.out.println("Patient data exported successfully to " + exportFileName);

        } catch (IOException e) {
            System.err.println("Failed to export patient data: " + e.getMessage());
        }
    }

}
