package accounts.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import accounts.models.Admin;
import accounts.models.Patient;
import accounts.models.User;
import core.AppConfig;
import core.BashConnect;

public class RegistrationService {
    private static final String registerScript = AppConfig.getRegisterScript();
    private static final Logger logger = Logger.getLogger(RegistrationService.class.getName());

    public String initializeRegistration(String email) {
        try {

            String result = BashConnect.run(registerScript, "initialize", email);
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
                result = BashConnect.run(registerScript, "register",
                        patient.getFirstName(), patient.getLastName(), patient.getEmail(), patient.getPassword(),
                        patient.getRole().name().toLowerCase(), patient.getDateOfBirth().toString(),
                        String.valueOf(patient.isHivPositive()),
                        patient.getDiagnosisDate() != null ? patient.getDiagnosisDate().toString() : "",
                        String.valueOf(patient.isOnArt()),
                        patient.getArtStartDate() != null ? patient.getArtStartDate().toString() : "",
                        patient.getCountryCode());

            } else if (user instanceof Admin) {
                result = BashConnect.run(registerScript, "register",
                        user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(),
                        user.getRole().name().toLowerCase());
            } else {
                throw new IllegalArgumentException("Unsupported user type");
            }
            System.err.println("Registration Result"+result);
            return result.startsWith("SUCCESS");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during user registration", e);
            return false;
        }
    }

}
