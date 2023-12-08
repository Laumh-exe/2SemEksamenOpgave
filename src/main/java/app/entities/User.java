package app.entities;

public abstract class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String role;
    protected double balance;

    /**
     * this consturctor if there is need a simple constructor if somethin is neede from the database in the frontend
     */
    public User(){}

    public User(int id, String firstName, String lastName, String email, String password, String rank, double balance) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = rank;
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
