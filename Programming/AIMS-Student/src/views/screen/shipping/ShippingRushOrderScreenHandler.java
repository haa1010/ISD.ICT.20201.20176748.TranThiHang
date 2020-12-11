package views.screen.shipping;

import common.exception.InvalidDeliveryInfoException;
import controller.PlaceOrderController;
import controller.PlaceRushOrderController;
import entity.order.Order;
import entity.order.RushOrder;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class ShippingRushOrderScreenHandler extends ShippingScreenHandler{

    @FXML
    private TextField deliveryTime;

    private RushOrder rushOrder;

    public ShippingRushOrderScreenHandler(Stage stage, String screenPath,RushOrder rushOrder) throws IOException {
        super(stage, screenPath, rushOrder);

    }

    public PlaceRushOrderController getBController() {
        return (PlaceRushOrderController) super.getBController();
    }


    @FXML
    void submitRushDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

        HashMap messages = addInfoToMessage();
        boolean validateInfoResult = false;

        try {
            // process and validate delivery info
            validateInfoResult = getBController().processDeliveryInfo(messages);
        } catch (InvalidDeliveryInfoException e) {
            throw new InvalidDeliveryInfoException(e.getMessage());
        }

        if (validateInfoResult) {
            calculateShippingFee(messages);
            createInvoice();
        }
    }
}
