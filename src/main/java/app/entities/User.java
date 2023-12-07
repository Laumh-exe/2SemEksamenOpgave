package app.entities;

public abstract class User {
    private int id;
    private String firstName;
    private String lastName;
    private String password;
    private String role;
    private double balance;

    public User(int id, String firstName, String lastName, String password, String rank, double balance){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public double getBalance() {
        return balance;
    }
    public double setBalance(double newBalance){
        balance = newBalance;
        return balance;
    }
    public void removeFromBalance(double valueToRemove){
        balance -=valueToRemove;
    }

    @Override
    public String toString(){
        return "User{"
                + "id=" + id
                + ", name=" + firstName + '\''
                + ", password='" + password + '\''
                + ", rank='" + role + "\''"
                + '}';
    }
}
