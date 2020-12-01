package entity.order;

import utils.Configs;

import java.time.LocalDateTime;
/**
 * This class is an entity for Rush Order
 * 
 * @author Tran Thi Hang
 * @version 1.0
 * <p>
 * created_at: 30/11/2020
 * <p>
 * project_name: An Internet Media Store (AIMS)
 * <p>
 * teacher_name: Dr. Nguyen Thi Thu Trang
 * <p>
 * class_name: TT.CNTT ICT 02 K62
 * <p>
 * helpers: Teaching Assistants 
 * 
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
