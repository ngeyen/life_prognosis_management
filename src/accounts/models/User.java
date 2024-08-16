package accounts.models;

import utils.enums.UserRole;

public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String pin;

    public User(String firstName, String lastName, String email, String pin) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.pin = pin;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public abstract UserRole getRole();
}
