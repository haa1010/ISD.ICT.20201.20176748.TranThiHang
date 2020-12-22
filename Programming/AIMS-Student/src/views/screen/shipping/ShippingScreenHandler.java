package views.screen.shipping;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import controller.PlaceOrderController;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.RushOrder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {

    @FXML
    protected Label screenTitle;

    @FXML
    protected TextField name;

    @FXML
    protected TextField phone;

    @FXML
    protected TextField address;

    @FXML
    protected TextField instructions;

    @FXML
    protected Label errorMessage;

    @FXML
    protected ComboBox<String> province;

    private Order order;

    private RushOrder rushOrder;

    public ShippingScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
        super(stage, screenPath);
        this.order = order;
        this.rushOrder = null;
    }
    public ShippingScreenHandler(Stage stage, String screenPath, Order order, RushOrder rushOrder) throws IOException {
        super(stage, screenPath);
        this.order = order;
        this.rushOrder = rushOrder;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
        this.province.getItems().addAll(Configs.PROVINCES);
    }

    protected HashMap addInfoToMessage () {
        HashMap messages = new HashMap<>();
        messages.put("name", name.getText());
        messages.put("phone", phone.getText());
        messages.put("address", address.getText());
        messages.put("instructions", instructions.getText());
        messages.put("province", province.getValue());
        return messages;
    }

    @FXML
    void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException {

        // add info to messages
        HashMap messages = addInfoToMessage();
        String validateInfoResult = "";
        errorMessage.setText("");

        try {
            // process and validate delivery info
            validateInfoResult = getBController().processDeliveryInfo(messages);
        } catch (InvalidDeliveryInfoException e) {
            throw new InvalidDeliveryInfoException(e.getMessage());
        }

        if (validateInfoResult.isEmpty()) {
            calculateShippingFee(messages);
            order.setDeliveryInfo(messages);
            createInvoiceScreen();
        }
        else {
            errorMessage.setText(validateInfoResult);
        }
    }

    public void calculateShippingFee ( HashMap messages) {
        // calculate shipping fees
        int shippingFees = getBController().calculateShippingFee(order);
        order.setShippingFees(shippingFees);
        order.setDeliveryInfo(messages);
    }

    public void createInvoiceScreen () throws IOException {

        // create invoice screen
        Invoice invoice = getBController().createInvoice(order, rushOrder);

        BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
        InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
        InvoiceScreenHandler.setScreenTitle("Invoice Screen");
        InvoiceScreenHandler.setBController(getBController());
        InvoiceScreenHandler.show();
    }

    public PlaceOrderController getBController() {
        return (PlaceOrderController) super.getBController();
    }

    public void notifyError() {
        // TODO: implement later on if we need
    }

}
