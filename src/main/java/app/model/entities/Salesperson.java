package app.model.entities;

public class Salesperson extends User{

    public Salesperson(int id, String firstName, String lastName, String email, String password, String role) {
        super(id, firstName, lastName, email, password, role);


    }

    public Salesperson(int id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
