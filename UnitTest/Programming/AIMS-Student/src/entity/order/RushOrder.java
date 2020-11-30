package entity.order;

import utils.Configs;

import java.time.LocalDateTime;
/**
 * 
 * @author hangtt
 * @version 1.0
 *
 */
public class RushOrder extends Order {
    private LocalDateTime deliveryTime;

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
