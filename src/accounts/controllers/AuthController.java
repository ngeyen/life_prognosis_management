package accounts.controllers;

import java.util.Scanner;

import accounts.services.UserManagementService;
import core.Docs;
import datacompute.services.SurvivalRate;
import utils.enums.ExportType;
import utils.enums.UserRole;
import utils.user.SessionUtils;

public class AuthController {
    private static final UserManagementService userService = new UserManagementService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        UserRole role = userService.verifyLoginCredentials(email, password);
        if (role != null) {
            System.out.println("Login successful.");
            System.out.println("========================");

            boolean isLoggedIn = true;

            while (isLoggedIn) {
                if (role == UserRole.ADMIN) {
                    System.out.println("Admin Menu.");
                    System.out.println("\nSelect an option to proceed: ");
                    System.out.println("1. Create new patient");
                    System.out.println("2. Add an Admin");
                    System.out.println("3. Download Patient Info");
                    System.out.println("4. Download Patient Analytics");
                    System.out.println("\n=========================");
                    System.out.println("5. Help \t 0. Logout");

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
                            Docs.showHelp(UserRole.ADMIN);
                            break;
                        case 0:
                            System.out.println("Logging out...");
                            isLoggedIn = false;
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } else {
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
                            Docs.showHelp(UserRole.PATIENT);
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
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

}
