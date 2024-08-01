package UserManager.services;

import UserManager.models.User;
import UserManager.models.Patient;
import UserManager.models.Admin;
import UserManager.models.UserRole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;

public class UserService {

    private static final String BASH_SCRIPT = "./user_manager.sh";
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public String initializePatientRegistration(String email) {
        try {
            String result = _handleBashCommands(BASH_SCRIPT, "initialize", email);
            if (result.startsWith("SUCCESS:")) {
                return result.split(":")[1].trim(); // Return the UUID
            } else {
                logger.warning("Failed to initialize patient registration: " + result);
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing patient registration", e);
            throw new RuntimeException("Failed to initialize patient registration", e);
        }
    }

    public boolean createUser(User user) {
        try {
            String result;
            if (user instanceof Patient patient) {
                result = _handleBashCommands(BASH_SCRIPT, "register",
                        patient.getFirstName(), patient.getLastName(), patient.getEmail(), patient.getPassword(),
                        patient.getRole().name().toLowerCase(), patient.getDateOfBirth().toString(),
                        String.valueOf(patient.isHIVPositive()),
                        patient.getDiagnosisDate() != null ? patient.getDiagnosisDate().toString() : "",
                        String.valueOf(patient.isOnART()),
                        patient.getArtStartDate() != null ? patient.getArtStartDate().toString() : "",
                        patient.getCountryCode());

            } else if (user instanceof Admin) {
                result = _handleBashCommands(BASH_SCRIPT, "register",
                        user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),
                        user.getRole().name().toLowerCase());
            } else {
                throw new IllegalArgumentException("Unsupported user type");
            }
            return result.startsWith("SUCCESS");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during user registration", e);
            return false;
        }
    }

    public boolean verifyLoginCredentials(String email, String password, UserRole role) {
        try {
            String result = _handleBashCommands(BASH_SCRIPT, "login", email, password, role.name().toLowerCase());
            return result.startsWith("SUCCESS");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during login", e);
            return false;
        }
    }

    private String _handleBashCommands(String... command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.warning("Bash script exited with code " + exitCode);
        }

        return output.toString().trim();
    }
}