package dashboard.controllers;

import java.util.Scanner;

import accounts.controllers.RegistrationController;
import core.Docs;
import utils.enums.ExportType;
import utils.interractions.Reset;
import utils.user.SessionUtils;

public class AdminDashboardController {
    private static final Scanner scanner = new Scanner(System.in);

    public static void showDashboard() {
        boolean isLoggedIn = true;
Reset.clearConsole();
        while (isLoggedIn) {
            System.out.println("Admin Menu.");
            System.out.println("\nSelect an option to proceed: ");
            System.out.println("1. Create new patient");
            System.out.println("2. Add an Admin");
            System.out.println("3. Download Patient Info");
            System.out.println("4. Download Patient Analytics");
            System.out.println("\n5. Help \t 0. Logout\n");
            System.out.println("=========================");

            System.out.print("\nYour choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    RegistrationController.initiateUserRegistration();
                    break;
                case 2:
                    RegistrationController.createAdmin();
                    break;
                case 3:
                    SessionUtils.downloadCSV(ExportType.PATIENT_INFO);
                    break;
                case 4:
                    SessionUtils.downloadCSV(ExportType.PATIENT_STATS);
                    break;
                case 5:
                    Docs.showHelp(utils.enums.UserRole.ADMIN);
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
