package entity.invoice;

import entity.order.Order;
import entity.order.RushOrder;

public class Invoice {

    private Order order;

    public RushOrder getRushOrder() {
        return rushOrder;
    }

    public void setRushOrder(RushOrder rushOrder) {
        this.rushOrder = rushOrder;
    }

    private RushOrder rushOrder;

    public int getAmountRushOrder() {
        return amountRushOrder;
    }

    public void setAmountRushOrder(int amountRushOrder) {
        this.amountRushOrder = amountRushOrder;
    }

    private int amountRushOrder;
    private int amount;

    public Invoice(Order order, RushOrder rushOrder){
        this.order = order;
        this.rushOrder = rushOrder;
    }

    public Order getOrder() {
        return order;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void saveInvoice(){
        
    }
}
