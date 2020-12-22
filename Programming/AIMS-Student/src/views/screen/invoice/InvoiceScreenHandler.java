package views.screen.invoice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import common.exception.ProcessInvoiceException;
import controller.PaymentController;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.payment.PaymentScreenHandler;

public class InvoiceScreenHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(InvoiceScreenHandler.class.getName());

    @FXML
    private Label pageTitle;

    @FXML
    private Label name;

    @FXML
    private Label phone;

    @FXML
    private Label province;

    @FXML
    private Label address;

    @FXML
    private Label instructions;

    @FXML
    private Label deliveryTimeLabel;
    @FXML
    private Label deliveryTime;

    @FXML
    private Label subtotal;

    @FXML
    private Label shippingFees;

    @FXML
    private Label total;

    @FXML
    private VBox vboxItems;

    @FXML
    private Button rushInfo;
    @FXML
    private Button normalInfo;
    @FXML
    private Label invoiceType;

    private Invoice invoice;


    public InvoiceScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
        super(stage, screenPath);
        this.invoice = invoice;

        rushInfo.setDisable(invoice.getRushOrder() == null);
        normalInfo.setDisable(invoice.getOrder() == null);

        if (invoice.getRushOrder() != null) {
            setRushInvoiceInfo();
        } else {
            setInvoiceInfo();
            invoiceType.setText("Normal Order Shipping Information");
        }
        rushInfo.setOnMouseClicked(e -> {
            invoiceType.setText("Rush Order Shipping Information");
            setRushInvoiceInfo();
        });
        normalInfo.setOnMouseClicked(e -> {
            invoiceType.setText("Normal Order Shipping Information");
            setInvoiceInfo();
        });

        // display total money
        setMoneyFormula();

    }

    private void setMoneyFormula(){
        int subTotal = 0, fees = 0;
        if (invoice.getOrder() != null) {
            subTotal += invoice.getOrder().getAmount();
            fees += invoice.getOrder().getShippingFees();
        }
        if (invoice.getRushOrder() != null) {
            subTotal += invoice.getRushOrder().getAmount();
            fees += invoice.getRushOrder().getShippingFees();
        }
        subtotal.setText(Utils.getCurrencyFormat(subTotal));
        shippingFees.setText(Utils.getCurrencyFormat(fees));

        int totalAmount = subTotal + fees;
        total.setText(Utils.getCurrencyFormat(totalAmount));
        invoice.setAmount(totalAmount);
    }

    private void setDeliveryInfo(HashMap<String, String> deliveryInfo, boolean isRush) {
        name.setText(deliveryInfo.get("name"));
        province.setText(deliveryInfo.get("province"));
        instructions.setText(deliveryInfo.get("instructions"));
        address.setText(deliveryInfo.get("address"));
        phone.setText(deliveryInfo.get("phone"));
        deliveryTime.setText(deliveryInfo.get("deliveryTime"));

        // delivery time is visible only for rush order
        deliveryTime.setVisible(isRush);
        deliveryTimeLabel.setVisible(isRush);
    }

    private void getOrderMediaToDisplay(Order order) {
        order.getlstOrderMedia().forEach(orderMedia -> {
            try {
                MediaInvoiceScreenHandler mis = new MediaInvoiceScreenHandler(Configs.INVOICE_MEDIA_SCREEN_PATH);
                mis.setOrderMedia((OrderMedia) orderMedia);
                vboxItems.getChildren().add(mis.getContent());
            } catch (IOException | SQLException e) {
                System.err.println("errors: " + e.getMessage());
                throw new ProcessInvoiceException(e.getMessage());
            }
        });
    }

    private void setRushInvoiceInfo() {

        if (invoice.getRushOrder() == null) return;
        HashMap<String, String> deliveryInfo = invoice.getRushOrder().getDeliveryInfo();

        setDeliveryInfo(deliveryInfo, true);
        vboxItems.getChildren().clear();

        getOrderMediaToDisplay(invoice.getRushOrder());

    }

    private void setInvoiceInfo() {
        if (invoice.getOrder() == null) return;
        HashMap<String, String> deliveryInfo = invoice.getOrder().getDeliveryInfo();

        setDeliveryInfo(deliveryInfo, false);
        vboxItems.getChildren().clear();

        getOrderMediaToDisplay(invoice.getOrder());

    }

    @FXML
    void confirmInvoice(MouseEvent event) throws IOException {
        BaseScreenHandler paymentScreen = new PaymentScreenHandler(this.stage, Configs.PAYMENT_SCREEN_PATH, invoice);
        paymentScreen.setBController(new PaymentController());
        paymentScreen.setPreviousScreen(this);
        paymentScreen.setHomeScreenHandler(homeScreenHandler);
        paymentScreen.setScreenTitle("Payment Screen");
        paymentScreen.show();
        LOGGER.info("Confirmed invoice");
    }

}
