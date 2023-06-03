package cn.ac.gabriel.gabcat.servlet;

import java.util.List;
import java.util.Map;

/**
 * Servlet Request Interface
 */
public interface GabRequest {
    /**
     * Get the request method (GET POST PUT DELETE ...)
     * @return the request method
     */
    String getMethod();

    /**
     * Get the request uri including parameters
     * @return the request uri
     */
    String getUri();

    /**
     * Get the request path without parameters
     * @return the request path
     */
    String getPath();

    /**
     * Get the request parameters
     * @return request parameters
     */
    Map<String, List<String>> getParameters();

    /**
     * Get the values of request parameters by name
     * @param name parameter name
     * @return value of the parameter
     */
    List<String> getParameters(String name);

    /**
     * Get the first value of request parameter by name
     *
     * @param name parameter name
     * @return the first value of the parameter
     */
    default String getParameter(String name) {
        List<String> parameters = getParameters(name);
        if (parameters == null || parameters.isEmpty()) {
            return null;
        }
        return parameters.get(0);
    }
}
