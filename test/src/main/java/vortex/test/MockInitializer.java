package vortex.test;


import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
public class MockInitializer implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
    	Mock.start();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    	Mock.stop();
    }
}
