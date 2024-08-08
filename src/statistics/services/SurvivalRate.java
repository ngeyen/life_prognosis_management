package statistics.services;

import UserManager.models.Patient;
import UserManager.models.User;
import helpers.UserUtils;

import java.time.LocalDate;
import java.time.Period;

public class SurvivalRate {



    public static String calculateSurvivalRate(String email) {
        Patient patient = UserUtils.getPatientByEmail(email);
        if (patient == null) {
            return "Patient not found";
        }

        try {
            // Get life expectancy from the Bash script
            String lifeExpectancyResult = UserUtils.getLifeExpectancy(email);
            if (!lifeExpectancyResult.startsWith("SUCCESS")) {
                return "Error retrieving life expectancy";
            }

            double lifeExpectancy = Double.parseDouble(lifeExpectancyResult.replace("SUCCESS: ", ""));
            int currentAge = Period.between(patient.getDateOfBirth(), LocalDate.now()).getYears();

            if (!patient.isHIVPositive()) {
                // Country's life expectancy - current age
                return String.format("Survival Rate: %.2f years", lifeExpectancy - currentAge);
            } else {
                if (!patient.isOnART()) {
                    // If a patient is not on ART, the survival rate is 5 years minus time since diagnosis
                    int yearsSinceDiagnosis = Period.between(patient.getDiagnosisDate(), LocalDate.now()).getYears();
                    return String.format("Survival Rate: %.2f years", 5.0 - yearsSinceDiagnosis);
                } else if (patient.getDiagnosisDate().getYear() == patient.getArtStartDate().getYear()) {
                    // (Country's life expectancy - current age) * 0.9
                    return String.format("Survival Rate: %.2f years", (lifeExpectancy - currentAge) * 0.9);
                } else {
                    // For each year of delay, the patient loses 10% for each year of the difference
                    int delay = patient.getArtStartDate().getYear() - patient.getDiagnosisDate().getYear();
                    return String.format("Survival Rate: %.2f years",
                            (lifeExpectancy - currentAge) * Math.pow(0.9, delay));
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
