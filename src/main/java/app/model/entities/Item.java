package app.model.entities;

public record Item(int id, double price_pr_unit, double length, String unit, String description) {
    public Item(double price_pr_unit, double length, String unit, String description){
        this(-1, price_pr_unit, length, unit, description);
    }
}
