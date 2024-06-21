package vortex.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import kik.framework.vortex.databasemanager.exception.RelationTypeException;
import vortex.annotate.constants.HttpMethod;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.http.exchange.Response;

import vortex.http.status.HttpStatus;
import vortex.properties.kinds.Server;

import vortex.test.exception.AmbiguousMethodException;
import vortex.utils.Asserttions;
import vortex.utils.MappingUtils;
import vortex.utils.Regex;
import vortex.utils.StringUtils;

class StructureTest {

    private static final String LOCALHOST = "http://localhost:" + Server.PORT.value();

    @BeforeAll
    static void init() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException, SQLException, RelationTypeException {
	Mock.getInstance();
    }

    @AfterAll
    static void finish() {
	Mock.stop();
    }

    @Test
    void testCreateWith2Methods() {
	
	Executable executable = () ->  new RequestBuilder().get("http://buenastardes.com").post("nomater");
	assertThrows(AmbiguousMethodException.class, executable);
    }
    @Test
    void testCreate() throws AmbiguousMethodException {
	RequestBuilder builder = new RequestBuilder().get("http://buenastardes.com");
	assertEquals(HttpMethod.GET, builder.getRequestMethod());
	assertEquals("http://buenastardes.com", builder.getRequestUri());

	builder = new RequestBuilder().addHeader("buenas", "adios");
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));
	builder.getRequestHeaders().put("buenas", "buenas");
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));

	builder = new RequestBuilder().addHeader("buenas", "adios").post("http://buenastardes.com");
	assertEquals(HttpMethod.POST, builder.getRequestMethod());
	assertEquals("http://buenastardes.com", builder.getRequestUri());
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));
	builder.getRequestHeaders().put("buenas", "buenas");
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));

	builder = new RequestBuilder().addHeader("buenas", "adios").put("http://buenastardes.com");
	assertEquals(HttpMethod.PUT, builder.getRequestMethod());
	assertEquals("http://buenastardes.com", builder.getRequestUri());
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));
	builder.getRequestHeaders().put("buenas", "buenas");
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));

	builder = new RequestBuilder().addHeader("buenas", "adios").delete("http://buenastardes.com").setTimeout(10000);
	assertEquals(HttpMethod.DELETE, builder.getRequestMethod());
	assertEquals("http://buenastardes.com", builder.getRequestUri());
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));
	builder.getRequestHeaders().put("buenas", "buenas");
	assertEquals("adios", builder.getRequestHeaders().get("buenas"));
	assertEquals(10000, builder.getTimeout());

	builder = new RequestBuilder();
	assertNull(builder.getRequestMethod());
	assertNull(builder.getRequestUri());
	assertNotNull(builder.getRequestHeaders());

	Map<String, String> headers = new HashMap<>();
	headers.put("buenas", "2");
	builder = new RequestBuilder(headers);
	assertEquals("2", builder.getRequestHeaders().get("buenas"));

    }

    @Test
    void badRequest() throws IOException, AmbiguousMethodException {
	Response response = new RequestBuilder().get(LOCALHOST + "/dontwork").perform();
	assertEquals(HttpStatus.NOT_FOUND, response.getStatus());
    }

    @ParameterizedTest
    @CsvSource({ "-789", "1023", "-2048", "3072", "-4096", "5120", "-6144", "7168", "-8192", "9216", "-10240", "11264",
	    "-12288", "13312", "-14336", "15360", "-16384", "17408", "-18432", "19456", "-20480", "21504", "-22528",
	    "23552", "-24576", "25600", "-26624", "27648", "-28672", "29696", "-30720", "31744", "-32768", "33792",
	    "-34816", "35840", "-36864", "37888", "-38912", "-4096", "4608" })
    void testPerformNonFloatingNumber(long number) throws IOException, AmbiguousMethodException {
	Response response = new RequestBuilder().get(LOCALHOST + "/test/number?number=" + number).perform();
	numeric(number, response);
	response = new RequestBuilder().get(LOCALHOST + "/test/ResponseNumber?number=" + number).perform();
	numeric(number, response);
    }

    private void numeric(long number, Response response) {
	switch (numberType(number)) {
	case "byte":
	    assertEquals((byte) number, (byte) response.getBody());
	    break;
	case "integer":
	    assertEquals((int) number, (int) response.getBody());
	    break;

	case "long":
	    assertEquals(number, response.getBody());
	    break;
	}
    }

    @ParameterizedTest
    @CsvSource({ "-1.2", "0", "0.0", "-12.345", "45.678", "-89.012", "123.456", "-789.012", "345.678", "-901.234",
	    "567.890", "-1234.567", "890.123", "-2345.6712318", "9012.345", "-3456.789", "7890.121231233", "-4567.890",
	    "2345.67123138", "-6789.011232", "1234.561231237", "-8901.234", "4567.890", "-12345.678", "9012.345",
	    "-7890.123", "3456.789", "-5678.901", "1234.5678", "-8901.2345", "6789.0123", "-2345.6789", "4567.8901",
	    "-7890.1234", "9012.3456", "-3456.7890", "7890.1234", "-5678.9012", "1234.5678", "-8901.2345", "6789.0123",
	    "-2345.6789", "4567.8901", "-7890.1234", "9012.3456", "-3456.7890", "7890.1234", "-5678.9012", "1234.5678",
	    "-8901.2345", "6789.0123", "-2345.6789", "4567.8901", "-7890.1234", "9012.3456", "-3456.7890", "7890.1234",
	    "-5678.9012", "1234.5678", "-8901.2345", "6789.0123", "-2345.6789", "4567.8901", "-7890.1234", "9012.3456",
	    "-3456.7890", "7890.1234", "-5678.9012", "1234.5678", "-8901.2345", "6789.0123", "-2345.6789", "4567.8901",
	    "-7890.1234", "9012.3456", "-3456.7890", "7890.1234", "-5678.9012", "1234.5678", "-8901.2345", "6789.0123",
	    "-2345.6789", "4567.8901", "-7890.1234", "9012.3456", "-3456.7890", "7890.1234", "-5678.9012", "1234.5678",
	    "-8901.2345", "6789.0123", "-2345.6789", "4567.8901", "-7890.1234", "9012.3456", "-3456.7890", "7890.1234",
	    "-5678.9012", "1234.5678", "-8901.2345", "6789.0123" })
    void testPerformFloatingNumbers(double number) throws IOException, AmbiguousMethodException {
	Response response = new RequestBuilder().get(LOCALHOST + "/test/floating?number=" + number).perform();
	Object w = MappingUtils.mapToPrimitive(response.getBody(), "" + number);
	assertEquals(number, w);
	response = new RequestBuilder().get(LOCALHOST + "/test/ResponseNumberFloat?number=" + number).perform();
	assertEquals(number, ((Double) response.getBody()).doubleValue());
    }

    @ParameterizedTest
    @CsvSource({ "true", "false", "no boolean" })
    void testPerformBoolean(boolean decision) throws IOException, AmbiguousMethodException {

	Response response = new RequestBuilder().get(LOCALHOST + "/test/decision?decision=" + decision).perform();
	assertEquals(decision, response.getBody());
	response = new RequestBuilder().get(LOCALHOST + "/test/ResponseDecision?decision=" + decision).perform();
	assertEquals(decision, response.getBody());
    }

    @ParameterizedTest
    @CsvSource({ "TestString123!", "RandomEndpoint987", "ApiTesting_$", "SampleData456", "EndpointTester789",
	    "StringValidation321", "AlphaNumeric_!@", "RequestCheck567", "DebuggingString456", "ApiTest123_$",
	    "String50Test!", "Endpoint123CSV", "DataTesting456", "RandomString_123", "ApiEndpoint789", "TestString_$",
	    "Sample123Data", "Validation321!", "AlphaNumeric@$", "CheckRequest567", "DebugString_456", "ApiTest_123_$",
	    "AnotherTestString!", "CSVEndpoint123", "TestingData_456", "StringRandom123", "EndpointApi789",
	    "StringTest_$", "DataSample123", "Validation!321", "NumericAlpha@", "RequestCheck_567", "Debug456String",
	    "ApiTest123_!$", "MoreTestString!", "CSV123Endpoint", "456TestingData", "RandomString123_",
	    "789ApiEndpoint", "StringTest_$!", "Sample123Data!", "Validation!321@", "NumericAlpha@$",
	    "CheckRequest_567!", "Debug456String!", "ApiTest123_!$", "MoreTestString!@"

    })
    void testPerformString(String string) throws IOException, AmbiguousMethodException {

	Response response = new RequestBuilder().get(LOCALHOST + "/test/string?string=" + string).perform();
	assertEquals(string, response.getBody());
	response = new RequestBuilder().get(LOCALHOST + "/test/ResponseString?string=" + string).perform();
	assertEquals(string, response.getBody());
    }

    private String numberType(long number) {
	String type = "";
	type = Asserttions.inRange(number, Long.MAX_VALUE, Long.MIN_VALUE) ? "long" : type;
	type = Asserttions.inRange(number, Integer.MAX_VALUE, Integer.MIN_VALUE) ? "integer" : type;
	type = Asserttions.inRange(number, Byte.MAX_VALUE, Byte.MIN_VALUE) ? "byte" : type;

	return type;
    }
}
