import UserManager.models.Patient;
import UserManager.services.UserService;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {

    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Life prognosis management System");

        while (true) {
            System.out.println("1. Admin - Initiate Patient Registration");
            System.out.println("2. Patient - Complete Registration");
            System.out.println("3. User - Login");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    initiatePatientRegistration();
                    break;
                case 2:
                    completePatientRegistration();
                    break;
                case 3:
                    login();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initiatePatientRegistration() {
        System.out.print("Enter patient email: ");
        String email = scanner.nextLine();
        String uuid = userService.initializePatientRegistration(email);
        if (uuid != null) {
            System.out.println("Registration initiated. Share this UUID with the patient: " + uuid);
        } else {
            System.out.println("Failed to initiate registration. Please try again.");
        }
    }

    private static void completePatientRegistration() {
        System.out.print("Enter patient UUID: ");
        String uuid = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        System.out.print("Are you HIV positive? (true/false): ");
        boolean isHIVPositive = Boolean.parseBoolean(scanner.nextLine());

        LocalDate diagnosisDate = null;
        boolean isOnART = false;
        LocalDate artStartDate = null;

        if (isHIVPositive) {
            System.out.print("Enter diagnosis date (YYYY-MM-DD): ");
            diagnosisDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Are you on ART drugs? (true/false): ");
            isOnART = Boolean.parseBoolean(scanner.nextLine());
            if (isOnART) {
                System.out.print("Enter ART start date (YYYY-MM-DD): ");
                artStartDate = LocalDate.parse(scanner.nextLine());
            }
        }

        System.out.print("Enter country code (ISO): ");
        String countryCode = scanner.nextLine();

        Patient patient = new Patient(firstName, lastName, email, password, dateOfBirth, isHIVPositive, diagnosisDate, isOnART, artStartDate, countryCode);
        boolean success = userService.createUser(patient);

        if (success) {
            System.out.println("Patient registration completed successfully.");
        } else {
            System.out.println("Failed to complete registration. Please try again.");
        }
    }

    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean success = userService.verifyLoginCredentials(email, password, null);
        if (success) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }
}
