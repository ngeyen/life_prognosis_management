package UserManager.services;

import UserManager.models.User;
import UserManager.models.Patient;
import UserManager.models.Admin;
import UserManager.models.UserRole;
import helpers.BashConnect;


import java.util.logging.Logger;
import java.util.logging.Level;


public class UserService {
    //TODO
    // Write comments for each method

    private static final String SCRIPT = "./src/user_manager.sh";
    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public String initializeRegistration(String email) {
        try {

            String result = BashConnect.run(SCRIPT, "initialize", email);
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
                result = BashConnect.run(SCRIPT, "register",
                        patient.getFirstName(), patient.getLastName(), patient.getEmail(), patient.getPassword(),
                        patient.getRole().name().toLowerCase(), patient.getDateOfBirth().toString(),
                        String.valueOf(patient.isHIVPositive()),
                        patient.getDiagnosisDate() != null ? patient.getDiagnosisDate().toString() : "",
                        String.valueOf(patient.isOnART()),
                        patient.getArtStartDate() != null ? patient.getArtStartDate().toString() : "",
                        patient.getCountryCode());

            } else if (user instanceof Admin) {
                result = BashConnect.run(SCRIPT, "register",
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
            String result = BashConnect.run(SCRIPT, "login", email, password);
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

    public String editUserProfile(String email, String firstName, String lastName, 
                                     String dateOfBirth, Boolean isHIVPositive, 
                                     String diagnosisDate, Boolean isOnART, 
                                     String artStartDate, String countryCode) {
        try {
            return BashConnect.run(
                SCRIPT, "update", email,
                firstName.isEmpty() ? "keep_current" : firstName,
                lastName.isEmpty() ? "keep_current" : lastName,
                dateOfBirth.isEmpty() ? "keep_current" : dateOfBirth,
                isHIVPositive == null ? "keep_current" : String.valueOf(isHIVPositive),
                diagnosisDate.isEmpty() ? "keep_current" : diagnosisDate,
                isOnART == null ? "keep_current" : String.valueOf(isOnART),
                artStartDate.isEmpty() ? "keep_current" : artStartDate,
                countryCode.isEmpty() ? "keep_current" : countryCode
            );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing patient profile", e);
            throw new RuntimeException("Failed to edit patient profile", e);
        }
    }
    public String getUserDetail(String email) {
        try {
            return BashConnect.run(SCRIPT, "view", email);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error viewing user - ", e);
            throw new RuntimeException("Failed to view user", e);
        }
    }
}