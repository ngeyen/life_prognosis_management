package accounts.services;

import utils.enums.UserRole;

import java.util.logging.Logger;

import core.AppConfig;
import core.BashConnect;

import java.util.logging.Level;

public class UserManagementService {

    private static final String SCRIPT = AppConfig.getUserMangerScript();
    private static final Logger logger = Logger.getLogger(UserManagementService.class.getName());

    public UserRole verifyLoginCredentials(String email, String password) {
        try {
            String result = BashConnect.run(SCRIPT, "login", email, password);
            if (result.startsWith("SUCCESS:")) {

                String roleStr = result.split(": ")[2].strip(); // Extract role
                return UserRole.valueOf(roleStr.toUpperCase());
            } else {
                logger.warning("Failed to verify login credentials: " + result);
                return null;
            }
        } catch (Exception e) {
            // Unable to login
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
                    countryCode.isEmpty() ? "keep_current" : countryCode);
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