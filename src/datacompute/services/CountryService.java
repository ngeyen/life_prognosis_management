package datacompute.services;

import java.io.IOException;
import java.util.Scanner;

import core.AppConfig;
import core.BashConnect;
import utils.validators.InputValidator;



public class CountryService {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputValidator nonEmptyValidator = new InputValidator();

    public static String getCountryCode() {
        String countryCode = null;
        String country;

        // Get country name from user with non-empty validation
        do {
            country = nonEmptyValidator.getNonEmptyInput("Enter country name: ");
        } while (!nonEmptyValidator.validate(country));

        try {
            // Call the bash script using BashConnect
            String result = BashConnect.run(AppConfig.getCountryQueryScript(), country);

            if (result.isEmpty()) {
                System.out.println("No matching countries found. Please try again.");
            } else {
                System.out.println("\nSuggestions:");
                System.out.println(result);

                System.out.print("Choose the ISO code from the suggestions or press 99 to cancel: ");
                String choice = scanner.nextLine().trim();

                if (choice.equals("99")) {
                    System.out.println("Cancelling registration and returning to the main menu...");
                    return null;
                } else if (result.contains(choice)) {
                    countryCode = choice;
                } else {
                    System.out.println("Invalid choice. Please select a valid ISO code from the suggestions.");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error executing bash script: " + e.getMessage());
        }

        return countryCode;
    }
}
