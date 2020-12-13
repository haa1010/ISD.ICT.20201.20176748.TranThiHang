package entity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import utils.Configs;

public class Order {

    protected int shippingFees;


    protected List lstOrderMedia;
    protected HashMap<String, String> deliveryInfo;

    public Order() {
        this.lstOrderMedia = new ArrayList<>();
    }

    public Order(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public List getLstOrderMedia() {
        return lstOrderMedia;
    }

    public void setLstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void addOrderMedia(OrderMedia om) {
        this.lstOrderMedia.add(om);
    }

    public void removeOrderMedia(OrderMedia om) {
        this.lstOrderMedia.remove(om);
    }

    public List getlstOrderMedia() {
        return this.lstOrderMedia;
    }

    public void setlstOrderMedia(List lstOrderMedia) {
        this.lstOrderMedia = lstOrderMedia;
    }

    public void setShippingFees(int shippingFees) {
        this.shippingFees = shippingFees;
    }

    public int getShippingFees() {
        return shippingFees;
    }

    public HashMap getDeliveryInfo() {
        return deliveryInfo;
    }

    public void setDeliveryInfo(HashMap deliveryInfo) {
        this.deliveryInfo = deliveryInfo;
    }

    public int getAmount() {
        double amount = 0;

        for (Object object : lstOrderMedia) {
            if( object instanceof OrderMedia) {
                OrderMedia om = (OrderMedia) object;
                amount += om.getPrice();
            }
        }
        return (int) (amount + (Configs.PERCENT_VAT / 100) * amount);
    }

}
