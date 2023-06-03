package cn.ac.gabriel.gabcat.http;

import cn.ac.gabriel.gabcat.server.GabcatServer;
import cn.ac.gabriel.gabcat.servlet.GabRequest;
import cn.ac.gabriel.gabcat.servlet.GabResponse;
import cn.ac.gabriel.gabcat.servlet.GabServlet;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class ResourceGabServlet extends GabServlet {
    @Override
    public void doGet(GabRequest request, GabResponse response) throws Exception {
        String path = request.getPath().substring(1);
        URL resource = GabcatServer.class.getClassLoader().getResource(path);
        File file = new File(resource.toURI());
        if (file.exists() && file.isFile()) {
            // read file content
            try (FileReader reader = new FileReader(file)) {
                char[] buffer = new char[1024];
                int len;
                StringBuffer sb = new StringBuffer();
                while ((len = reader.read(buffer)) != -1) {
                    String content = new String(buffer, 0, len);
                    sb.append(content);
                }
                response.write(sb.toString());
            }
        } else {
            String name = request.getParameter("name");
            response.write("404 - resource not found: " + name);
        }
    }

    @Override
    public void doPost(GabRequest request, GabResponse response) throws Exception {

    }
}
