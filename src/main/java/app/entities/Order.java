package app.entities;

import java.util.Date;

public class Order {
    private int id;
    private int customerId;
    private int salespersonId;
    private Date date;
    private OrderStatus status;
    private Carport carport;
    private Receipt receipt;
    private double price;

    public Order(int id, int customerId, int salespersonId, Date date, OrderStatus status, double price, double carportLength, double carportWidth, double shedLength, double shedWidth){
        this.id = id;
        this.customerId = customerId;
        this.salespersonId = salespersonId;
        this.date = date;
        this.status = status;
        carport = new Carport(carportLength,carportWidth);
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Carport getCarport() {
        return carport;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public double getPrice() {
        return price;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getSalespersonId() {
        return salespersonId;
    }

    @Override
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof Order)) {
            return false;
        }
        Order other = (Order)arg0;
        if(other.getId() != id) return false;
        if(other.getDate().compareTo(date) != 0) return false;

        return true;
    }


}
