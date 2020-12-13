package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.order.RushOrder;
import utils.Configs;

/**
 * This class controls the flow of place rush order use case in AIMS project
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
 */


public class PlaceRushOrderController extends PlaceOrderController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());

    /**
     * This method checks the availability of product when user click placeRushOrder
     * button
     *
     * @throws SQLException
     */
    public void placeRushOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new RushOrder based on the Cart
     *
     * @return RushOrder
     * @throws SQLException
     */
    public RushOrder createRushOrder() {
        RushOrder rushOrder = new RushOrder();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            if(cartMedia.getMedia().isSupportRushOrder()) {
                OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
                rushOrder.getlstOrderMedia().add(orderMedia);
            }
        }
        return rushOrder;
    }


    public Order createNormalOrder() {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            if(!cartMedia.getMedia().isSupportRushOrder()) {
                OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
                order.getlstOrderMedia().add(orderMedia);
            }
        }
        return order;
    }


    /**
     * This method calculates the shipping fees of RushOrder
     * fee = fee + 10 * number of items for rush order
     *
     * @param rushOrder
     * @return shippingFee
     */
    public int calculateShippingFee(RushOrder rushOrder) {
        Random rand = new Random();
        int fees = (int) (((rand.nextFloat() * 10) / 100) * rushOrder.getAmount()) + rushOrder.getlstOrderMedia().size() * 10000;
        LOGGER.info("Order Amount: " + rushOrder.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }

    /**
     * This method check if deliveryTime is valid
     *
     * @param time
     * @return boolean
     */

    public boolean validateRushTime(String time) {
        if (time.isEmpty()) return true;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
            LocalDateTime now = LocalDateTime.now();
            boolean isAfter = dateTime.isAfter(now);
            boolean validHour = false;
            if (dateTime.getHour() <= 18 && dateTime.getHour() >= 7) {
                validHour = true;
            }
            return isAfter && validHour;
        } catch (NullPointerException e) {
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * This method check if current Cart media has Media that supports RushOrder
     *
     * @return boolean
     */

    public boolean validateRushCartMedia() {
        try {
            return (Cart.getCart().getListSupportCartMedia().size() > 0);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * This method check if province in deliveryInfo is valid
     *
     * @param province
     * @return boolean
     */
    public boolean validateProvince(String province) {
        if (province != null) {
            return (province.equals(Configs.PROVINCES[0]));
        }
        return false;
    }

    @Override
    public boolean processDeliveryInfo(HashMap info) throws IOException, InterruptedException {
        LOGGER.info("Process Rush Order Delivery Info");
        LOGGER.info(info.toString());
        return validateDeliveryInfo(info) && validateRushDeliveryInfo(info);
    }

    public boolean validateRushDeliveryInfo(HashMap<String, String> info) {
        if (!validateProvince(info.get("province"))) {
            System.out.println("Your province is not supported rush order. Please try again!");
            return false;
        }
        if (!validateRushTime(info.get("deliveryTime"))) {
            System.out.println("Invalid delivery time. Please try again!");
            return false;
        }
        return true;
    }

}
