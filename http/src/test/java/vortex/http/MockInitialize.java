package vortex.http;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import vortex.test.test.Mock;

public class MockInitialize implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
    	Mock.getInstance();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    	Mock.stop();
    }
}
