package app.model.entities;
public record Item(int id, double price_pr_unit, double length, String unit, String description, int quantity, String function) {
    public Item(int id, double price_pr_unit, double length, String unit, String description, String function){
        this(id, price_pr_unit, length, unit, description, 0, function) ;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj == this){
            return true;
        }
        if(!(obj instanceof Item)){
            return false;
        }
        Item item = (Item)obj;
        if(id == item.id() && price_pr_unit == item.price_pr_unit && length == item.length && unit == item.unit && description == item.description && quantity == item.quantity && function == item.function) {
            return true;
        }
        return false;
    }
}