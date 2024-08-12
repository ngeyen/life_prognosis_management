package accounts.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import core.AppConfig;
import core.BashConnect;
import utils.enums.UserRole;

public class AuthService {
    private static final String authScript = AppConfig.getAuthScript();
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    public static UserRole verifyLoginCredentials(String email, String password) {
        try {
            String result = BashConnect.run(authScript, "login", email, password);
            if (result.startsWith("SUCCESS:")) {

                String roleStr = result.split(": ")[2].strip(); // Extract role
                return UserRole.valueOf(roleStr.toUpperCase());
            } else {
                System.out.println("\n\nUnable to Login");
                logger.log(Level.WARNING, "Looks like your credentials are incorrect");
                return null;
            }
        } catch (Exception e) {
            // Unable to login
            logger.log(Level.SEVERE, "Error during login", e);
            return null;
        }
    }

}
