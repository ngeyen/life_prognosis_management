package utils.validators;


public class PasswordValidator implements Validator {
    private String errorMessage;

    @Override
    public boolean validate(String password) {
        if (password == null || password.isEmpty()) {
            errorMessage = "Password cannot be empty.";
            return false;
        }
        return true;
    }

    public  String getPassword() {
        while (true) {
            System.out.print("Enter password: ");
            String password = new String(System.console().readPassword());
            if (!validate(password)) {
                System.out.println(getErrorMessage());
                continue;
            }
            System.out.print("Confirm password: ");
            String confirmPassword = new String(System.console().readPassword());
            if (password.equals(confirmPassword)) {
                return password;
            } else {
                System.out.println("Passwords do not match. Please try again.");
            }
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
