package statistics.services;

import UserManager.models.Patient;
import UserManager.models.User;

public class SurvivalRate {
     private final Patient patient;

    SurvivalRate(User patient) {
        this.patient = (Patient) patient;
    }

   private String calculateSurvivalRate(){
        if(patient.isOnART()){
            patient.getDiagnosisDate();
        }
        return "Survival";
   }


    @Override
    public String toString() {
        return super.toString();
    }
}
