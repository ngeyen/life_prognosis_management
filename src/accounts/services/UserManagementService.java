package accounts.services;

import java.util.logging.Logger;

import core.AppConfig;
import core.BashConnect;

import java.util.logging.Level;

public class UserManagementService {

    private static final String userManagerScript = AppConfig.getUserManagerScript();
    private static final Logger logger = Logger.getLogger(UserManagementService.class.getName());

    public String editUserProfile(String email, String firstName, String lastName,
            String dateOfBirth, Boolean isHivPositive,
            String diagnosisDate, Boolean isOnART,
            String artStartDate, String countryCode) {
        try {
            return BashConnect.run(
                    userManagerScript, "update", email,
                    firstName.isEmpty() ? "keep_current" : firstName,
                    lastName.isEmpty() ? "keep_current" : lastName,
                    dateOfBirth.isEmpty() ? "keep_current" : dateOfBirth,
                    isHivPositive == null ? "keep_current" : String.valueOf(isHivPositive),
                    diagnosisDate == null ? "keep_current" : diagnosisDate,
                    isOnART == null ? "keep_current" : String.valueOf(isOnART),
                    artStartDate == null ? "keep_current" : artStartDate,
                    countryCode.isEmpty() ? "keep_current" : countryCode);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error editing patient profile", e);
            throw new RuntimeException("Failed to edit patient profile", e);
        }
    }

    public String getUserDetail(String email) {
        try {
            return BashConnect.run(userManagerScript, "view", email);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error viewing user - ", e);
            throw new RuntimeException("Failed to view user", e);
        }

    }

}