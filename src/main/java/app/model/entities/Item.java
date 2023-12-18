package app.model.entities;

public record Item(int id, double price_pr_unit, double length, String unit, String description, int quantity, String carport_part) {
    public Item(int id, double price_pr_unit, double length, String unit, String description, String carport_part){
        this(id, price_pr_unit, length, unit, description, 0, carport_part) ;
    }


}
