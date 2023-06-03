package cn.ac.gabriel.gabcat.servlet;

/**
 * Servlet Response Interface
 */
public interface GabResponse {
    /**
     * Write content to response
     * @param content content to write
     * @throws Exception exception when writing
     */
    void write(String content) throws Exception;
}
