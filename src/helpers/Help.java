package helpers;

public class Help {
        /*
        TODO:
                show help for the different menu options available to the user
         */
    public static void showHelp(){

            System.out.println("\nHelp Menu");
            System.out.println("=========================");
            System.out.println("1. Create new patient: Allows an admin to register a new patient by entering their details.");
            System.out.println("2. Add an Admin: Enables an admin to register another admin user.");
            System.out.println("3. Download Patient Info: Exports patient information to a CSV file.");
            System.out.println("4. Download Patient Analytics: Exports patient statistics and analytics to a CSV file.");
            System.out.println("5. Help: Displays this help menu.");
            System.out.println("0. Exit: Exits the application.");
            System.out.println("\nPatient Menu:");
            System.out.println("1. View Profile: Displays your profile details.");
            System.out.println("2. Update my Profile: Allows you to update your profile information.");
            System.out.println("3. Recalculate Survival Rate: Recalculates your survival rate based on the latest data.");
            System.out.println("5. Exit: Exits the application.");
            System.out.println("=========================");
            System.out.println("Please choose the corresponding number to proceed.");

    }
}
