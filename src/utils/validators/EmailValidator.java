package utils.validators;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class EmailValidator implements Validator {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean validate(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public String getErrorMessage() {
        return "Please provide a valid email e.g.(email@domain.com)\n";
    }

    // Method to get a valid email from the user
    public String getValidEmail(String prompt) {
        String email;
        while (true) {
            System.out.print(prompt);
            email = scanner.nextLine().trim();
            if (email.isEmpty()) {
                System.err.println("Email cannot be blank. Please enter a valid email.");
                continue;
            }
            if (validate(email)) {
                return email;
            } else {
                System.err.println(getErrorMessage());
            }
        }
    }
}
