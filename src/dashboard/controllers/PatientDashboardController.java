package dashboard.controllers;

import java.util.Scanner;

import accounts.controllers.ProfileController;
import core.Docs;
import datacompute.services.SurvivalRate;
import utils.interractions.Reset;

public class PatientDashboardController {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showDashboard(String email) {
        boolean isLoggedIn = true;
        Reset.clearConsole();
        while (isLoggedIn) {
            System.out.println("\n\nWelcome, " + email);
            System.out.println(SurvivalRate.calculateSurvivalRate(email));
            System.out.println("===============================");
            System.out.println("\nSelect an option to proceed: ");
            System.out.println("1. View Profile");
            System.out.println("2. Update my Profile");
            System.out.println("3. Download ICS Schedule");
            System.out.println("\n4. Help \t 0. Logout");
            System.out.println("\n===============================");
            System.out.print("\nYour choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    ProfileController.viewPatientDetails(email);
                    break;
                case 2:
                    ProfileController.editPatientProfile(email, "Update Profile");
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
