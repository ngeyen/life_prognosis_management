package statistics.services;

import UserManager.models.Patient;
import UserManager.models.User;

public class SurvivalRate {
     private final Patient patient;

    SurvivalRate(User patient) {
        this.patient = (Patient) patient;
    }

   private String calculateSurvivalRate(){
        if(!patient.isHIVPositive()){
            // Country's life expectancy - age
        }
       else {
            if (!patient.isOnART()) {
                // Part 1
                // If a patient is not on ART, the survival rate is 5 years
                // 5 - (Current date - date of diagnosis)

            } else if (patient.isOnART() && patient.getDiagnosisDate().getYear() == patient.getArtStartDate().getYear()) {
                // (Country's life expectancy - Current age ) * 0.9
            } else {
                // For each year of delay, the patient loses 10% for each year of the difference
                // diff =  (dateOfART - diagnoseDate)
                // (country's life expectancy - current age) * 90 ^ difference + 1
            }
        }

      return "Survival Rate";
   }


    @Override
    public String toString() {
        return super.toString();
    }
}
