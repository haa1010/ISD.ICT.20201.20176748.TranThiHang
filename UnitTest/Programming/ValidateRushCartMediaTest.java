import static org.junit.jupiter.api.Assertions.assertEquals;

import controller.PlaceRushOrderController;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;


public class ValidateRushCartMediaTest {
    private PlaceRushOrderController placeRushOrderController;
    private Media media;

    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
        media = new Media();
    }

    @Test
    public void test1() {
        boolean isValid = placeRushOrderController.validateRushCartMedia();
        assertEquals(false, isValid);
    }

    @Test
    public void test2() throws SQLException {
        CartMedia cm1 = new CartMedia(media.getMediaById(1), Cart.getCart(), 2, 4000);
        Cart.getCart().addCartMedia(cm1);
        boolean isValid = placeRushOrderController.validateRushCartMedia();
        assertEquals(false, isValid);
    }

    @Test
    public void test3() throws SQLException {
        CartMedia cm1 = new CartMedia(media.getMediaById(1), Cart.getCart(), 2, 5000);
        CartMedia cm2 = new CartMedia(media.getMediaById(2), Cart.getCart(), 2, 5000);

        cm1.getMedia().setSupportRushOrder(true);
        Cart.getCart().addCartMedia(cm1);
        Cart.getCart().addCartMedia(cm2);

        boolean isValid = placeRushOrderController.validateRushCartMedia();
        assertEquals(true, isValid);
    }
}
