package utils.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import accounts.models.Patient;
import core.AppConfig;
import datacompute.controllers.StatisticsController;
import datacompute.services.StatisticsService;
import utils.enums.ExportType;
import utils.interractions.Reset;

public class DataExport {
    private static StatisticsService service = new StatisticsService();
    private static DataExport dataExport = new DataExport();
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
        String USER_STORE = AppConfig.getUserStorePath();
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
            Reset.clearConsole();
            System.out.println("Patient data exported successfully to " + exportFileName);

        } catch (IOException e) {
            System.err.println("Failed to export patient data: " + e.getMessage());
        }
    }

    public void saveStatisticsToCSV() throws IOException {
        List<Patient> patients = service.getPatientAsList();
        StatisticsController controller = new StatisticsController(patients);

        String filePath = AppConfig.getStaticticsPath();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Statistic,Value\n");
            writer.write("Number of Users," + patients.size() + "\n");
            writer.write("Average Age," + controller.calculateAverageAge() + "\n");
            writer.write("Median Age," + controller.calculateMedianAge() + "\n");
            writer.write("Average Duration on ART (days)," + controller.calculateAverageDurationOnArt() + "\n");
            writer.write("Median Duration on ART (days)," + controller.calculateMedianDurationOnArt() + "\n");

            // Write count of patients by country
            writer.write("Country Code,Patient Count\n");
            Map<String, Long> patientCountByCountry = controller.countPatientsByCountry();
            for (Map.Entry<String, Long> entry : patientCountByCountry.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

   

   public static void downloadCSV(ExportType type) {
       if (type == ExportType.PATIENT_INFO) {

           dataExport.exportPatientData(AppConfig.getPatientDataExportPath());
       }
       if (type == ExportType.PATIENT_STATS) {
           try {
            Reset.clearConsole();
               System.out.print("Downloading Patient stats.... to " + AppConfig.getStaticticsPath() +"\n\n");
               dataExport.saveStatisticsToCSV();
           } catch (IOException e) {
               e.printStackTrace();
               // TODO: #11 To be handled
           }
       }
   }
}
