package vortex.properties.dataTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import vortex.utils.HttpUtils;

import java.util.ArrayList;
import java.util.List;

class HttpUtilsTest {

    @Test
    void testSetContentHeaderWithPrimitiveBody() {
        List<String> contentHeaders = new ArrayList<>();
        Object body = 123;
        Object contentHeader = null;

        List<String> result = HttpUtils.setContentHeader(contentHeaders, body, contentHeader);
        assertEquals(1, result.size());
        assertEquals("text/plain", result.get(0));
    }

    @Test
    void testSetContentHeaderWithNonPrimitiveBody() {
        List<String> contentHeaders = new ArrayList<>();
        Object body = new ArrayList<>();
        Object contentHeader = null;

        List<String> result = HttpUtils.setContentHeader(contentHeaders, body, contentHeader);
        assertEquals(1, result.size());
        assertEquals("application/json", result.get(0));
    }

    @Test
    void testSetContentHeaderWithNonNullContentHeader() {
        List<String> contentHeaders = new ArrayList<>();
        Object body = 123;
        Object contentHeader = "some header";

        List<String> result = HttpUtils.setContentHeader(contentHeaders, body, contentHeader);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSetContentHeaderWithNullBody() {
        List<String> contentHeaders = new ArrayList<>();
        Object body = null;
        Object contentHeader = null;

        List<String> result = HttpUtils.setContentHeader(contentHeaders, body, contentHeader);
        assertTrue(result.isEmpty());
    }
}
