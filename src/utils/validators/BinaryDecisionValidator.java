package utils.validators;


public class BinaryDecisionValidator implements Validator {
    private String errorMessage;
    private InputValidator inputValidator = new InputValidator();
    @Override
    public boolean validate(String input) {
        if (input.equals("1") || input.equals("0")) {
            return true;
        } else {
            errorMessage = "Invalid choice. Please choose 1 for Yes or 0 for No.";
            return false;
        }
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean getBinaryDecision(String prompt) {
        while (true) {
            String input = inputValidator.getNonEmptyInput(prompt);
            if (validate(input)) {
                return input.equals("1");
            } else {
                System.err.println(getErrorMessage());
            }
        }
    }
    
  
}
