package utils.validators;

public interface Validator {
    boolean validate(String value);

    String getErrorMessage();
}
