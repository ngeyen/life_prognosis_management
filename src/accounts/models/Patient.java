package accounts.models;

import java.time.LocalDate;
import java.util.UUID;

import utils.enums.UserRole;

public class Patient extends User {
    private String uuid;
    private LocalDate dateOfBirth;
    private boolean isHivPositive;
    private LocalDate diagnosisDate;
    private boolean isOnArt;
    private LocalDate artStartDate;
    private String countryCode;

    public Patient(String firstName, String lastName, String email, String password,
            LocalDate dateOfBirth, boolean isHivPositive, LocalDate diagnosisDate,
            boolean isOnArt, LocalDate artStartDate, String countryCode, String uuid) {
        super(firstName, lastName, email, password);
        this.dateOfBirth = dateOfBirth;
        this.isHivPositive = isHivPositive;
        this.diagnosisDate = diagnosisDate;
        this.isOnArt = isOnArt;
        this.artStartDate = artStartDate;
        this.countryCode = countryCode;
        this.uuid = uuid;
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

    public boolean isHivPositive() {
        return isHivPositive;
    }

    public void setHivPositive(boolean HIVPositive) {
        isHivPositive = HIVPositive;
    }

    public LocalDate getDiagnosisDate() {
        return diagnosisDate;
    }

    public void setDiagnosisDate(LocalDate diagnosisDate) {
        this.diagnosisDate = diagnosisDate;
    }

    public boolean isOnArt() {
        return isOnArt;
    }

    public void setOnArt(boolean onART) {
        isOnArt = onART;
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
    @Override
    public String toString() {
        String hivStatus = isHivPositive ? "true" : "false";
        String onArt = isOnArt ? "true" : "false";
        String dateOfBirthStr = dateOfBirth != null ? dateOfBirth.toString() : "";
        String diagnosisDateStr = diagnosisDate != null ? diagnosisDate.toString() : "";
        String artStartDateStr = artStartDate != null ? artStartDate.toString() : "";
        String uuidString = uuid==null || uuid.isBlank() ? UUID.randomUUID().toString() : uuid;


        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                getEmail(),
                uuidString,
                getFirstName(),
                getLastName(),
                getPassword(), 
                "patient", 
                dateOfBirthStr,
                hivStatus,
                diagnosisDateStr,
                onArt,
                artStartDateStr,
                countryCode);
    }
}
