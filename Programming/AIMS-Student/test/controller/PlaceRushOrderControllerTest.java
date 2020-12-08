package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;

/**
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
* */

public class PlaceRushOrderControllerTest {
	  private PlaceRushOrderController placeRushOrderController;
	  private Media media;
	    @BeforeEach
	    void setUp() throws Exception {
	        placeRushOrderController = new PlaceRushOrderController();
	        media = new Media();
	    }


	    @ParameterizedTest
	    @CsvSource({
	            "2007/12/03 19:15,false",
	            "2020/12/23 10:15,true",
	            "abc,false",
	            ",true"
	    })
	    public void testRushTime(String time, boolean expected) {
	        boolean isValid = placeRushOrderController.validateRushTime(time);
	        assertEquals(expected,isValid);
	    }
	    
	    @Test
	    public void testRushCartMedia1() {
	        boolean isValid = placeRushOrderController.validateRushCartMedia();
	        assertEquals(false, isValid);
	    }

	    @Test
	    public void testRushCartMedia2() throws SQLException {
	        CartMedia cm1 = new CartMedia(media.getMediaById(1), Cart.getCart(), 2, 4000);
	        Cart.getCart().addCartMedia(cm1);
	        boolean isValid = placeRushOrderController.validateRushCartMedia();
	        assertEquals(false, isValid);
	    }

	    @Test
	    public void testRushCartMedia3() throws SQLException {
	        CartMedia cm1 = new CartMedia(media.getMediaById(1), Cart.getCart(), 2, 5000);
	        CartMedia cm2 = new CartMedia(media.getMediaById(2), Cart.getCart(), 2, 5000);

	        cm1.getMedia().setSupportRushOrder(true);
	        Cart.getCart().addCartMedia(cm1);
	        Cart.getCart().addCartMedia(cm2);

	        boolean isValid = placeRushOrderController.validateRushCartMedia();
	        assertEquals(true, isValid);
	    }
	    
	    @ParameterizedTest
	    @CsvSource({
	            "Hanoi,true",
	            "so 15 hai ba trung hanoi,true",
	            "so 15 hai ba trung hai phong,false",
	            ",false"
	    })
	    public void testProvince(String address, boolean expected) {
	        boolean isValid = placeRushOrderController.validateProvince(address);
	        assertEquals(expected,isValid);
	    }


	    @Test
	    public void testProvince2() {
	        boolean isValid = placeRushOrderController.validateProvince("so 15, hai ba trung");
	        assertEquals(false,isValid);
	    }
}
