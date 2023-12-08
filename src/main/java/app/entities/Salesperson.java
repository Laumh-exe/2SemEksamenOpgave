package app.entities;

public class Salesperson extends User{
    public Salesperson(int id, String firstName, String lastName, String email, String password, String rank, double balance) {
        super(id, firstName, lastName, email, password, rank, balance);
    }

    public Salesperson(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
