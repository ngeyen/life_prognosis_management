package utils.validators;

import java.util.Scanner;

public class InputValidator implements Validator {
    private String errorMessage;
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            errorMessage = "This field cannot be empty. Please provide a value.";
            return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    // Method to get non-empty input from the user
    public String getNonEmptyInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!validate(input)) { // Call the validate method to check the input
                System.out.println(getErrorMessage());
            }
        } while (!validate(input));
        return input;
    }

    public int getValidatedIntegerInput(String prompt) {
        int input = -1;
        boolean valid = false;

        while (!valid) {
            System.out.print(prompt);
            String userInput = scanner.nextLine().trim();

            try {
                input = Integer.parseInt(userInput);
                valid = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return input;
    }
}
