package app.entities;


public abstract class User {
    private int id;
    private String firstName;
    private String lastName;

    private String email;
    private String password;
    private String role;
    private double balance;


    public User(int id, String firstName, String lastName, String email, String password, String role, double balance) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

        this.password = password;
        this.role = role;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName(){return lastName;}


    public String getPassword() {
        return password;
    }

    public String getEmail(){return email;}


    public String getRole() {
        return role;
    }

    public double getBalance() {
        return balance;
    }

    public double setBalance(double newBalance) {
        balance = newBalance;
        return balance;
    }

    public void removeFromBalance(double valueToRemove) {
        balance -= valueToRemove;
    }


    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", firstName=" + firstName + '\''
                + ", lastName=" + lastName + '\''
                + ", email@email.com=" + email + '\''
                + ", password='" + password + '\''
                + ", rank='" + role + "\''"
                + '}';
    }

}


