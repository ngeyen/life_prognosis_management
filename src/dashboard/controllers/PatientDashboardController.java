package dashboard.controllers;

import java.util.Scanner;

import accounts.controllers.ProfileController;
import core.Docs;
import datacompute.services.SurvivalRate;

public class PatientDashboardController {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showDashboard(String email) {
        boolean isLoggedIn = true;

        while (isLoggedIn) {
            System.out.println("\n\nWelcome, " + email);
            System.out.println(SurvivalRate.calculateSurvivalRate(email));
            System.out.println("===============================");
            System.out.println("\nSelect an option to proceed: ");
            System.out.println("1. View Profile");
            System.out.println("2. Update my Profile");
            System.out.println("3. Download ICS Schedule");
            System.out.println("4. Help");
            System.out.println("0. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    ProfileController.viewPatientDetails(email);
                    break;
                case 2:
                    ProfileController.editPatientProfile(email);
                    break;
                case 3:
                    ProfileController.downloadDeathScheduleICS(email);
                    break;
                case 4:
                    Docs.showHelp(utils.enums.UserRole.PATIENT);
                    break;
                case 0:
                    System.out.println("Logging out...");
                    isLoggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
