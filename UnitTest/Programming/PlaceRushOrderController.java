package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.order.RushOrder;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 *
 * @author hangtt
 */
public class PlaceRushOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click placeRushOrder
     * button
     *
     * @throws SQLException
     */
    public void placeRushOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     *
     * @return Order
     * @throws SQLException
     */
    public RushOrder createRushOrder() throws SQLException {
        RushOrder rushOrder = new RushOrder();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
            rushOrder.getlstOrderMedia().add(orderMedia);
        }
        return rushOrder;
    }

    /**
     * This method calculates the shipping fees of order
     *
     * @param rushOrder
     * @return shippingFee
     */
    public int calculateShippingFee(RushOrder rushOrder) {
        Random rand = new Random();
        // fee + 10* number of items for rush order
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

        // TODO: your work
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
            LocalDateTime now = LocalDateTime.now();
            boolean isAfter = dateTime.isAfter(now);
            boolean validHour = false;
            if (dateTime.getHour() <= 18 && dateTime.getHour() >= 7) {
                validHour = true;
            }
            return isAfter && validHour;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }

    public boolean validateRushCartMedia() {
        try {
            return Cart.getCart().getListSupportCartMedia().size() > 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public boolean validateProvince(String address) {
        // TODO: your work
        if(address!=null){
            address = address.toLowerCase();
            return ((!address.equals("")) && (address.matches("^[.0-9a-zA-Z\\s,]+$"))) && address.contains("hanoi");
        }
        return false;
    }

}
