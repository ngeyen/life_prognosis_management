
import java.util.Scanner;

import accounts.controllers.AuthController;
import accounts.controllers.PatientRegistrationController;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {

        while (true) {

            System.out.println("\nLife Prognosis Management System");

            System.out.println("\nChoose an option to begin:");
            System.out.println("1. Register with UUID");
            System.out.println("2. Login\n");
            System.out.println("0. Exit");
            System.out.println("===============================");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {

                case 1:
                    PatientRegistrationController.completePatientRegistration();
                    break;
                case 2:
                    AuthController.login();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
