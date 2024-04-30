package vortex.test;

import org.junit.jupiter.api.Test;

/**
 * MockTest
 */
public class MockTest {

    @Test
    void testInit() throws Exception{
        Mock.getInstance();
    }

    @Test
    void testStop() throws Exception{
        Mock.stop();

    }

    
}
