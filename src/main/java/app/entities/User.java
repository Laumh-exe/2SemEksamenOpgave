package app.entities;

public class User {
    private int id;
    private String name;
    private String password;
    private String rank;
    private double balance;

    public User(int id, String name, String password, String rank, double balance){
        this.id = id;
        this.name = name;
        this.password = password;
        this.rank = rank;
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

    public String getRank() {
        return rank;
    }

    public double getBalance() {
        return balance;
    }
    public double setBalance(double newBalance){
        balance = newBalance;
        return balance;
    }
}
