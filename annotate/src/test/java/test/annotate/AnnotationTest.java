package test.annotate;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import vortex.annotate.constants.HttpMethod;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.AnnotationManager;
import vortex.annotate.manager.Storage;
import vortex.annotate.method.parameter.RequestBody;
import vortex.annotate.method.parameter.RequestParam;
import vortex.properties.kinds.Server;
class AnnotationTest {

	private static final String context_path = Server.CONTEXT_PATH.value().equals("/")?"":(String) Server.CONTEXT_PATH.value();
	private static final HttpMethod DELETE = HttpMethod.DELETE;
	private static final HttpMethod POST = HttpMethod.POST;
	private static final HttpMethod GET = HttpMethod.GET;
	private static final HttpMethod PUT = HttpMethod.PUT;
	private static Storage storage;
	private static AnnotationManager manager;

	@BeforeAll
	static void init() throws Exception {
		manager = AnnotationManager.getInstance();
		storage = Storage.getInstance();
	}

	@Test
	void testGet() {
		HttpMethod[] method = storage.checkType("/users/logged");
		assertEquals(GET, method[0]);
		method = storage.checkType("/tests/status");
		assertEquals(GET, method[0]);
	}

	@Test
	void testPost() {
		HttpMethod[] method = storage.checkType("/tests/analyze");
		assertEquals(POST, method[0]);
		method = storage.checkType("/users/login");
		assertEquals(POST, method[0]);
	}

	@Test
	void testPut() {
		HttpMethod[] method = storage.checkType("/tests/execute");

		assertEquals(PUT, method[0]);
		method = storage.checkType("/users/register");

		assertEquals(PUT, method[0]);
	}

	@Test
	void testDelete() {
		HttpMethod method[] = storage.checkType("/tests/cleanup");
		assertEquals(DELETE, method[0]);
		method = storage.checkType("/users/erease");
		assertEquals(DELETE, method[0]);
	}

	@Test
	void testBody() throws UriException {

		Method method = storage.getMethod(PUT,
				context_path + "/tests/execute");
		Annotation[][] annotations = method.getParameterAnnotations();
		assertEquals(RequestBody.class.getName(),
				annotations[0][0].annotationType().getName());
	}
	@Test
	void testParam() throws UriException {
		Method method = storage.getMethod(GET,
				context_path+ "/users/logged");
		Annotation[][] annotations = method.getParameterAnnotations();
		assertEquals(RequestParam.class.getName(),
				annotations[0][0].annotationType().getName());
	}

	@Test
	void testParams() throws UriException {

		Method method = storage.getMethod(GET, context_path + "/tests/status");
		Annotation[][] annotations = method.getParameterAnnotations();
		assertEquals(RequestParam.class.getName(),
				annotations[0][0].annotationType().getName());
		assertEquals(RequestParam.class.getName(),
				annotations[1][0].annotationType().getName());

	}

	@Test
	void testAll() throws UriException {
		Method method = storage.getMethod(DELETE,context_path +  "/users/erease");
		Annotation[][] annotations = method.getParameterAnnotations();
		assertEquals(RequestParam.class.getName(),
				annotations[0][0].annotationType().getName());
		assertEquals(RequestBody.class.getName(),
				annotations[1][0].annotationType().getName());
		assertEquals(RequestParam.class.getName(),
				annotations[2][0].annotationType().getName());
	}
}
