package app.entities;

public class Customer extends User{

    public Customer(int id, String firstName, String lastName, String email, String password, String role, double balance) {
        super(id, firstName, lastName, email, password, role, balance);
    }

}
