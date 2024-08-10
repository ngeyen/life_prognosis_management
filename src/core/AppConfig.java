package core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getSurvialRateScript() {
        return getProperty("sr.script");
    }

    public static String getUserMangerScript() {
        return getProperty("usr.script");
    }

    public static String getUserStorePath() {
        return getProperty("usr.store");
    }

    public static String lifeExpectancyPath() {
        return getProperty("le.path");
    }

    public static int getPinLength() {
        return Integer.parseInt("pin.length");
    }
}