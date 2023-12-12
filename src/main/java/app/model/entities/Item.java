package app.model.entities;

public record Item(int id, double price_pr_unit, double length, String unit, String description, int quantity) {
    /*public Item(int id, double price_pr_unit, double length, String unit, String description, int quantity){
        this(id, price_pr_unit, length, unit, description, quantity);
    }

     */
}
