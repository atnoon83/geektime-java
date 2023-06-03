package cn.ac.gabriel.gabcat.http;

import cn.ac.gabriel.gabcat.servlet.GabRequest;
import cn.ac.gabriel.gabcat.servlet.GabResponse;
import cn.ac.gabriel.gabcat.servlet.GabServlet;

/**
 * Default Gab Servlet is used to handle the request that no servlet matches
 */
public class DefaultGabServlet extends GabServlet {
    /**
     * Process GET request, return 404 as default
     * @param request request
     * @param response response
     * @throws Exception exception when processing
     */
    @Override
    public void doGet(GabRequest request, GabResponse response) throws Exception {
        String uri = request.getUri();
        String name = uri.substring(0, uri.indexOf("?"));
        response.write("404 - no this servlet : " + name);
    }

    /**
     * Process POST request, same as doGet
     * @param request request
     * @param response response
     * @throws Exception exception when processing
     */
    @Override
    public void doPost(GabRequest request, GabResponse response) throws Exception {
        doGet(request, response);
    }
}
