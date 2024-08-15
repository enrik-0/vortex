package vortex.http.exchange;

import java.util.List;
import java.util.Map;

import vortex.http.status.HttpStatus;

/**
 * Contract of a response
 */
public
interface Response{

    /**
     * @return {@linkplain HttpStatus}
     */
    HttpStatus getStatus();

    /**
     * @param state {@link HttpStatus} to set
     */
    Response setStatus(HttpStatus state);

    /**
     * @return Map(String, List ( String))
     */
    Map<String, List<String>> getHeaders();

    Response setHeader(String name, List<String> value);

    Response setHeader(String name, String value);

    /**
     * @return Object or null
     */
    Object getBody();
}
