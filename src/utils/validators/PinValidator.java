package utils.validators;

import core.AppConfig;

public class PinValidator implements Validator {

    private static final int PIN_LENGTH = AppConfig.getPinLength();

    @Override
    public boolean validate(String pin) {
        return pin != null && pin.matches("\\d{" + PIN_LENGTH + "}");
    }

    public boolean validateMatch(String pin, String confirmPin) {
        return pin.equals(confirmPin);
    }

    @Override
    public String getErrorMessage() {
        return "PIN must be exactly " + PIN_LENGTH + " digits long.";
    }

}