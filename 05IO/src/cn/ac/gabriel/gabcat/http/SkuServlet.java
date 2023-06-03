package cn.ac.gabriel.gabcat.http;

import cn.ac.gabriel.gabcat.servlet.GabRequest;
import cn.ac.gabriel.gabcat.servlet.GabResponse;
import cn.ac.gabriel.gabcat.servlet.GabServlet;

public class SkuServlet extends GabServlet {
    @Override
    public void doGet(GabRequest request, GabResponse response) throws Exception {
        String uri = request.getUri();
        String path = request.getPath();
        String method = request.getMethod();
        String name = request.getParameter("name");
        String content = "uri = " + uri + "\n" +
                "path = " + path + "\n" +
                "method = " + method + "\n" +
                "param = " + name;
        response.write(content);
    }

    @Override
    public void doPost(GabRequest request, GabResponse response) throws Exception {
        doGet(request, response);
    }
}
