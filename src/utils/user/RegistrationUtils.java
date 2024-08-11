package utils.user;

import java.util.logging.Level;
import java.util.logging.Logger;

import core.AppConfig;
import core.BashConnect;

public class RegistrationUtils {
    private static final Logger logger = Logger.getLogger(RegistrationUtils.class.getName());

    public static boolean isUUIDValid(String uuid) {
        try {
            String result = BashConnect.run(AppConfig.getUserManagerScript(), "search_uuid", uuid);
            return result.startsWith("SUCCESS");
        } catch (Exception e) {

            logger.log(Level.SEVERE, "UUID Couldn't be fount - ", e.getMessage());

        }
        return false;
    }
}
