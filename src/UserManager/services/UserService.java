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

    private static final String SCRIPT = "./src/user_manager.sh";
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public String initializePatientRegistration(String email) {
        try {

            String result = _handleBashCommands(SCRIPT, "initialize", email);
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
                result = _handleBashCommands(SCRIPT, "register",
                        patient.getFirstName(), patient.getLastName(), patient.getEmail(), patient.getPassword(),
                        patient.getRole().name().toLowerCase(), patient.getDateOfBirth().toString(),
                        String.valueOf(patient.isHIVPositive()),
                        patient.getDiagnosisDate() != null ? patient.getDiagnosisDate().toString() : "",
                        String.valueOf(patient.isOnART()),
                        patient.getArtStartDate() != null ? patient.getArtStartDate().toString() : "",
                        patient.getCountryCode());

            } else if (user instanceof Admin) {
                result = _handleBashCommands(SCRIPT, "register",
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

    public UserRole verifyLoginCredentials(String email, String password) {
        try {
            String result = _handleBashCommands(SCRIPT, "login", email, password);
            if (result.startsWith("SUCCESS:")) {

                String roleStr = result.split(": ")[2].strip(); // Extract role
                return UserRole.valueOf(roleStr.toUpperCase());
            }
            else {
                logger.warning("Failed to verify login credentials: " + result);
                return null;
            }
        } catch (Exception e) {
            //Unable to login
            logger.log(Level.SEVERE, "Error during login", e);
            return null;
        }
    }

    /**
     * @return Returns from bash command
     */
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
            logger.severe("Bash command execution failed" + output.toString());
        }

        return output.toString().trim();
    }
}