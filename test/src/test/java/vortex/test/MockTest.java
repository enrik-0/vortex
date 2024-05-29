package vortex.test;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

/**
 * MockTest
 */
public class MockTest {

    @Test @Order(1)
    void testInit() throws Exception{
        Mock.getInstance();
    }

    @Test @Order(2)
    void testStop() throws Exception{
        Mock.stop();

    }

    
}
