import static org.junit.jupiter.api.Assertions.assertEquals;

import controller.PlaceRushOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import controller.PlaceOrderController;


public class ValidateProvinceTest {
    private PlaceRushOrderController placeRushOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeRushOrderController = new PlaceRushOrderController();
    }


    @ParameterizedTest
    @CsvSource({
            "Hanoi,true",
            "so 15 hai ba trung hanoi,true",
            "so 15 hai ba trung hai phong,false",
            ",false"
    })
    public void test(String address, boolean expected) {
        boolean isValid = placeRushOrderController.validateProvince(address);
        assertEquals(expected,isValid);
    }


    @Test
    public void test2() {
        boolean isValid = placeRushOrderController.validateProvince("so 15, hai ba trung");
        assertEquals(false,isValid);
    }
}
