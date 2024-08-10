
import java.util.Scanner;

import accounts.controllers.AuthController;
import accounts.controllers.RegistrationController;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Life prognosis management System");

        while (true) {

            System.out.println("1. Register with UUID");
            System.out.println("2. Login");

            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {

                case 1:
                    RegistrationController.completePatientRegistration();
                    break;
                case 2:
                    AuthController.login();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

}
