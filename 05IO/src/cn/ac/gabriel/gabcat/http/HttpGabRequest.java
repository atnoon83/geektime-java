package cn.ac.gabriel.gabcat.http;

import cn.ac.gabriel.gabcat.servlet.GabRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * Wrapper of Http Request
 */
public class HttpGabRequest implements GabRequest {
    /**
     * Http Request
     */
    HttpRequest request;

    /**
     * Constructor
     * @param request Http Request
     */
    public HttpGabRequest(HttpRequest request) {
        this.request = request;
    }

    /**
     * Get the request method (GET POST PUT DELETE ...)
     * @return the request method
     */
    @Override
    public String getMethod() {
        return request.method().name();
    }

    /**
     * Get the request uri including parameters
     * @return the request uri
     */
    @Override
    public String getUri() {
        return request.uri().toString();
    }

    /**
     * Get the request path without parameters
     * @return the request path
     */
    @Override
    public String getPath() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.path();
    }

    /**
     * Get the request parameters
     * @return request parameters
     */
    @Override
    public Map<String, List<String>> getParameters() {
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return decoder.parameters();
    }

    /**
     * Get the values of request parameters by name
     * @param name parameter name
     * @return value of the parameter
     */
    @Override
    public List<String> getParameters(String name) {
        return getParameters().get(name);
    }
}
