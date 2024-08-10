package accounts.models;

import java.time.LocalDate;

import utils.enums.UserRole;

public class Patient extends User {
    private LocalDate dateOfBirth;
    private boolean isHIVPositive;
    private LocalDate diagnosisDate;
    private boolean isOnART;
    private LocalDate artStartDate;
    private String countryCode;

    public Patient(String firstName, String lastName, String email, String pin,
            LocalDate dateOfBirth, boolean isHIVPositive, LocalDate diagnosisDate,
            boolean isOnART, LocalDate artStartDate, String countryCode) {
        super(firstName, lastName, email, pin);
        this.dateOfBirth = dateOfBirth;
        this.isHIVPositive = isHIVPositive;
        this.diagnosisDate = diagnosisDate;
        this.isOnART = isOnART;
        this.artStartDate = artStartDate;
        this.countryCode = countryCode;
    }

    @Override
    public UserRole getRole() {
        return UserRole.PATIENT;
    }

    // Getters and setters for Patient-specific fields
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isHIVPositive() {
        return isHIVPositive;
    }

    public void setHIVPositive(boolean HIVPositive) {
        isHIVPositive = HIVPositive;
    }

    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public boolean isOnART() {
        return isOnART;
    }

    public void setOnART(boolean onART) {
        isOnART = onART;
    }

    public LocalDate getArtStartDate() {
        return artStartDate;
    }

    public void setArtStartDate(LocalDate artStartDate) {
        this.artStartDate = artStartDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
