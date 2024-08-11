package core;

public class AppConfig {
    public static String getSurvivalRateScript() {
        return System.getenv("SURVIVAL_RATE_SCRIPT");
    }

    public static String getUserManagerScript() {
        return System.getenv("USER_MANAGER_SCRIPT");
    }

    public static String getAuthScript() {
        return System.getenv("AUTH_SCRIPT");
    }

    public static String getRegisterScript() {
        return System.getenv("REGISTER_SCRIPT");
    }

    public static String getUserStorePath() {
        return System.getenv("USER_STORE_PATH");
    }

    public static String getLifeExpectancyPath() {
        return System.getenv("LIFE_EXPECTANCY_PATH");
    }

    public static int getPinLength() {
        String pinLength = System.getenv("PIN_LENGTH");
        return pinLength != null ? Integer.parseInt(pinLength) : 4;
    }
}
