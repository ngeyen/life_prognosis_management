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

        while (true) {
            // Get country name or code from the user with non-empty validation
            do {
                country = nonEmptyValidator.getNonEmptyInput("Enter country name or ISO code: ");
            } while (!nonEmptyValidator.validate(country));

            try {
                // Call the bash script using BashConnect
                String result = BashConnect.run(AppConfig.getCountryQueryScript(), country);

                if (result.isEmpty()) {
                    System.out.println("No matching countries found. Please try again.");
                } else {
                    String[] suggestions = result.split("\n");
                    System.out.println("\nSuggestions:");
                    for (int i = 0; i < suggestions.length; i++) {
                        System.out.println((i + 1) + ". " + suggestions[i]);
                    }

                    while (true) {
                        System.out.print("Choose a number from the suggestions or press 99 to cancel: ");
                        String choice = scanner.nextLine().trim();

                        if (choice.equals("99")) {
                            System.out.println("Cancelling selection. Please enter the country name or code again.");
                            break; // Break the inner loop to allow re-entry of country name or code
                        } else {
                            try {
                                int selectedIndex = Integer.parseInt(choice) - 1;
                                if (selectedIndex >= 0 && selectedIndex < suggestions.length) {
                                    // suggestion
                                    String [] selectedCountryData = suggestions[selectedIndex].split("ISO Code:");
                                    countryCode = selectedCountryData[1].trim(); // The Alpha-2 code is in the 4th column
                                    return countryCode; // Valid selection, return the countryCode
                                } else {
                                    System.out.println(
                                            "Invalid choice. Please select a valid number from the suggestions.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(
                                        "Invalid input. Please enter a number corresponding to the suggestions.");
                            }
                        }
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.err.println("Error executing bash script: " + e.getMessage());
            }
        }
    }
}
