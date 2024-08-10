package UserManager.models;

public abstract class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
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
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract UserRole getRole();

    @Override
    public String toString() {
        return "First Name:" + getFirstName() + "\n"
                + "Last Name:" + getLastName() + "\n"
                + "Email:" + getEmail() + "\n";

    }
}