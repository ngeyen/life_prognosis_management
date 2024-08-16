package datacompute.services;


import core.AppConfig;
import core.BashConnect;

import java.io.IOException;
import java.util.List;

import accounts.models.Patient;



public class StatisticsService {

    private static String getAllPatientAsString() {
        try {
            return BashConnect.run(AppConfig.getUserManagerScript(), "all_users");
         
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

   public List<Patient> getPatientAsList() {
        String allPatients = getAllPatientAsString();
        if (allPatients != null) {
            return Patient.getAllPatients(allPatients);
        }
        else {
            return null;
    }
   }
}
