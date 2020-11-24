import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import controller.PlaceOrderController;


public class ValidateAddressTest {
    private PlaceOrderController placeOrderController;

    @BeforeEach
    void setUp() throws Exception {
        placeOrderController = new PlaceOrderController();
    }


    @ParameterizedTest
    @CsvSource({
            "hanoi,true",
            "so 15 hai ba trung,true",
            "$# Hanoi,false",
            ",false"
    })
    public void test(String address, boolean expected) {
        boolean isValid = placeOrderController.validateAddress(address);
        assertEquals(expected,isValid);
    }


    @Test
    public void test2() {
        boolean isValid = placeOrderController.validateAddress("so 15, hai ba trung");
        assertEquals(true,isValid);
    }
}
