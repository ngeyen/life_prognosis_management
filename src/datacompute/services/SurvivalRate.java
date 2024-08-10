package datacompute.services;

import utils.user.SessionUtils;

import java.time.LocalDate;
import java.time.Period;

import accounts.models.Patient;

public class SurvivalRate {

    public static String calculateSurvivalRate(String email) {
        Patient patient = SessionUtils.getPatientByEmail(email);
        if (patient == null) {
            return "Patient not found";
        }

        try {
            // Get life expectancy from the Bash script
            String lifeExpectancyResult = SessionUtils.getLifeExpectancy(email);
            if (!lifeExpectancyResult.startsWith("SUCCESS")) {
                return "Error retrieving life expectancy: " + lifeExpectancyResult;
            }

            // Extract the numeric value from the life expectancy result
            String lifeExpectancyStr = lifeExpectancyResult.replaceAll("[^0-9.]", "");
            double le = Double.parseDouble(lifeExpectancyStr);
            int lifeExpectancy = (int) Math.ceil(le);

            int currentAge = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();

            if (!patient.isHIVPositive()) {
                // Country's life expectancy - current age
                return String.format("You have %d years remaining", lifeExpectancy - currentAge);
            } else {
                if (!patient.isOnART()) {
                    // If a patient is not on ART, the survival rate is 5 years minus time since
                    // diagnosis
                    int yearsSinceDiagnosis = Period.between(patient.getDiagnosisDate(), LocalDate.now()).getYears();
                    return String.format("You have %d years remaining", 5 - yearsSinceDiagnosis);
                } else if (patient.getDiagnosisDate().getYear() == patient.getArtStartDate().getYear()) {
                    // (Country's life expectancy - current age) * 0.9
                    return String.format("You have %d years remaining",
                            (int) Math.ceil((lifeExpectancy - currentAge) * 0.9));
                } else {
                    // For each year of delay, the patient loses 10% for each year of the difference
                    int delay = patient.getArtStartDate().getYear() - patient.getDiagnosisDate().getYear();
                    return String.format("You have %d years remaining",
                            (int) Math.ceil((lifeExpectancy - currentAge) * Math.pow(0.9, delay)));
                }
            }

        } catch (Exception e) {
            return "An error occurred while calculating survival rate: " + e.getMessage();
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
