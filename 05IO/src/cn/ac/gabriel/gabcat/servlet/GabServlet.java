package cn.ac.gabriel.gabcat.servlet;

/**
 * Servlet Interface
 */
public abstract class GabServlet {
    /**
     * Process GET request
     * @param request request
     * @param response response
     * @throws Exception exception when processing
     */
    public abstract void doGet(GabRequest request, GabResponse response) throws Exception;

    /**
     * Process POST request
     * @param request request
     * @param response response
     * @throws Exception exception when processing
     */
    public abstract void doPost(GabRequest request, GabResponse response) throws Exception;
}
