package app.entities;

public abstract class User {
    private int id;
    private String name;
    private String password;
    private String role;
    private double balance;

    public User(int id, String name, String password, String rank, double balance){
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = rank;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
                + ", name=" + name + '\''
                + ", password='" + password + '\''
                + ", rank='" + role + "\''"
                + '}';
    }
}
