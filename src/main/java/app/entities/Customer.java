package app.entities;

public class Customer extends User{
    public Customer(int id, String name, String password, String rank, double balance) {
        super(id, name, password, rank, balance);
    }
}
