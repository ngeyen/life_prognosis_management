import UserManager.models.Admin;
import UserManager.models.Patient;
import UserManager.models.UserRole;
import UserManager.services.UserService;
import helpers.DataExport;
import helpers.ExportType;
import helpers.Help;
import helpers.UserUtils;
import statistics.services.SurvivalRate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {
    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to the Life prognosis management System");

        while (true) {

            System.out.println("1. Register");
            System.out.println("2. Login");


            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {

                case 1:
                    completePatientRegistration();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void initiateUserRegistration() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        String uuid = userService.initializeRegistration(email);
        if (uuid != null) {
            System.out.println("Registration initiated. Share this UUID with the user: " + uuid);
        } else {
            System.out.println("Failed to initiate registration. Please try again.");
        }
    }
    private  static  void createAdmin(){
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Admin admin = new Admin(firstName, lastName, email, password);
        boolean success = userService.createUser(admin);
        if (success) {
            System.out.println("Admin added created.");
        }else {
            System.out.println("Failed to create Admin. Please try again.");
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
    private static void viewPatientDetails(String email) {

        try {
            String result = userService.getUserDetail(email);

            if (result.startsWith("SUCCESS")) {
                String[] details = result.replace("SUCCESS: ", "").split(",");

                // Display patient details excluding the password
                System.out.println("Patient Details:");
                System.out.println("First Name: " + details[2]);
                System.out.println("Last Name: " + details[3]);
                System.out.println("Role: " + details[4]);
                System.out.println("Date of Birth: " + details[5]);
                System.out.println("HIV Positive: " + details[6]);
                System.out.println("Diagnosis Date: " + details[7]);
                System.out.println("On ART: " + details[8]);
                System.out.println("ART Start Date: " + details[9]);
                System.out.println("Country Code: " + details[10]);
            } else {
                System.out.println("Patient not found.");

            }
        } catch (Exception e) {
            System.err.println("An error occurred while retrieving patient details: " + e.getMessage());
        }
    }
    private static void downloadCSV(ExportType type){
        if(type == ExportType.PATIENT_INFO) {
            DataExport dataExport = new DataExport();
            dataExport.exportPatientData("patient_data.csv");
        }
    }
    private static void editPatientProfile(String email) {

        System.out.println("Enter the patient's first name (leave blank to keep current):");
        String firstName = scanner.nextLine();
        
        System.out.println("Enter the patient's last name (leave blank to keep current):");
        String lastName = scanner.nextLine();
        
        System.out.println("Enter the patient's date of birth (yyyy-mm-dd) (leave blank to keep current):");
        String dateOfBirth = scanner.nextLine();
        
        System.out.println("Is the patient HIV positive? (true/false) (leave blank to keep current):");
        String isHIVPositiveStr = scanner.nextLine();
        Boolean isHIVPositive = isHIVPositiveStr.isEmpty() ? null : Boolean.parseBoolean(isHIVPositiveStr);
        
        System.out.println("Enter the patient's diagnosis date (yyyy-mm-dd) (leave blank if not applicable or to keep current):");
        String diagnosisDate = scanner.nextLine();
        
        System.out.println("Is the patient on ART? (true/false) (leave blank to keep current):");
        String isOnARTStr = scanner.nextLine();
        Boolean isOnART = isOnARTStr.isEmpty() ? null : Boolean.parseBoolean(isOnARTStr);
        
        System.out.println("Enter the patient's ART start date (yyyy-mm-dd) (leave blank if not applicable or to keep current):");
        String artStartDate = scanner.nextLine();
        
        System.out.println("Enter the patient's country ISO code (leave blank to keep current):");
        String countryCode = scanner.nextLine();

        try {
            String result = userService.editUserProfile(
                email, firstName, lastName, dateOfBirth, 
                isHIVPositive, diagnosisDate, isOnART, 
                artStartDate, countryCode
            );

            if (result.startsWith("SUCCESS")) {
                System.out.println("Patient profile updated successfully.");
                System.out.println(SurvivalRate.calculateSurvivalRate(email));
            } else {
                System.out.println("Failed to update patient profile: " + result);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while updating patient profile: " + e.getMessage());
        }
    }
    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        UserRole role = userService.verifyLoginCredentials(email, password);
        if (role != null) {
            System.out.println("Login successful.");
            System.out.println(role + "\n========================");

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
                            initiateUserRegistration();
                            break;
                        case 2:
                            createAdmin();
                            break;
                        case 3:
                            downloadCSV(ExportType.PATIENT_INFO);
                            break;
                        case 4:
                            downloadCSV(ExportType.PATIENT_STATS);
                            break;
                        case 5:
                            Help.showHelp();  // Assuming there's a help method.
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
                    System.out.println("\n===============================");
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
                            viewPatientDetails(email);
                            break;
                        case 2:
                            editPatientProfile(email);
                            break;
                        case 3:
                            UserUtils.downloadDeathScheduleICS(email);
                            break;
                        case 4:
                            Help.showHelp();  // Assuming there's a help method.
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
