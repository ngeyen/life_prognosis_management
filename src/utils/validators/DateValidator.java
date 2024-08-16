package utils.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateValidator implements Validator {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String errorMessage;

    @Override
    public boolean validate(String dateStr) {
        try {
            LocalDate.parse(dateStr, DATE_FORMAT);
            return true;
        } catch (DateTimeParseException e) {
            errorMessage = "Invalid date format. Please use yyyy-MM-dd (e.g., 2000-01-23).";
            return false;
        }
    }

    public boolean validateDateOrder(LocalDate dateOfBirth, LocalDate diagnosisDate, LocalDate artStartDate) {
        // if (dateOfBirth.isAfter(LocalDate.now()) || diagnosisDate !=null && diagnosisDate.isAfter(LocalDate.now()) || diagnosisDate!=null&& artStartDate.isAfter(LocalDate.now())) {
        //     return false;
        // }

        if (dateOfBirth != null && diagnosisDate != null && dateOfBirth.isAfter(diagnosisDate)) {
            errorMessage = "Invalid date. diagnosis cannot be before Birth date.";
            return false;
        }
        if (diagnosisDate != null && artStartDate != null && artStartDate.isBefore(diagnosisDate)) {
            errorMessage = "ART start date cannot be before the diagnosis date.";
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
