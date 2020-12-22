package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import entity.order.RushOrder;

/**
 * This class controls the flow of place order use case in AIMS project
 * 
 * @author Tran Thi Hang, nguyenlm
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

public class PlaceOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the availability of product when user click PlaceOrder
     * button
     *
     * @throws SQLException
     */
    public void placeOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     *
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(), cartMedia.getQuantity(), cartMedia.getPrice());
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     *
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order, RushOrder rushOrder) {
        return new Invoice(order, rushOrder);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public String processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        return validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public String validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        if(!validateName(info.get("name"))) {
            return "Your name is invalid. Please try again!";
        }
        if(!validatePhoneNumber(info.get("phone"))) {
            return "Phone number is invalid. Please try again!";
        }
        if(info.get("province") == null || info.get("province").isEmpty()) {
            return "Please choose your province!";
        }
        if(!validateAddress(info.get("address"))) {
            return "Address is invalid. Please try again!";
        }
        return "";
    }
    
	/**
	 * This method validates phone number on deliveryInfo
	 * 
	 * @param phoneNumber
	 * @return boolean
	 */
    public boolean validatePhoneNumber(String phoneNumber) {
        try {
            if (phoneNumber.length() != 10 || !phoneNumber.startsWith("0")) {
                return false;
            }
            Integer.parseInt(phoneNumber);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * This method validates name on deliveryInfo
     * 
     * @param name
     * @return boolean
     */
    public boolean validateName(String name) {
        try {
            return ((!name.equals("")) && (name.matches("^[a-zA-Z\\s]*$")));
        } catch (NullPointerException e) {
            return false;
        }
    }


    /**
     * This method validates address on deliveryInfo
     *
     * @param address
     * @return boolean
     */
    
    public boolean validateAddress(String address) {
        try {
            return ((!address.equals("")) && (address.matches("^[.0-9a-zA-Z\\s,]+$")));
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * This method calculates the shipping fees of order
     *
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order) {
        Random rand = new Random();
        int fees = (int) (((rand.nextFloat() * 10) / 100) * order.getAmount());
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }

    public List getListNormalCartMedia(){
        return Cart.getCart().getListMedia();
    }
}
