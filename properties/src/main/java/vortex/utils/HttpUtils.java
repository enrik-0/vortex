package vortex.utils;

import java.util.List;

public class HttpUtils {
    private HttpUtils() {
	
    }
    public static List<String> setContentHeader(List<String> contentHeaders, Object body, Object contentHeader) {
	if (contentHeader == null && (body != null)) {
	    if (Asserttions.isPrimitive(body)) {
		contentHeaders.add("text/plain");
	    } else {
		contentHeaders.add("application/json");
	    }
	}
	return contentHeaders;
    }
}
