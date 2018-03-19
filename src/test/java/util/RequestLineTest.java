package util;

import http.RequestLine;
import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class RequestLineTest {
    @Test
    public void cerate_method(){
        RequestLine line = new RequestLine("GET /index.html HTTP/1.1");

        assertEquals("GET", line.getMethod());
        assertEquals("/index.html", line.getPath());

        line = new RequestLine("POST /index.html HTTP/1.1");
        assertEquals("/index.html", line.getPath());
    }

    @Test
    public void create_path_and_params(){
        RequestLine line = new RequestLine("GET /user/create?userid=javajigi&password=pass HTTP/1.1");

        assertEquals("GET", line.getMethod());
        assertEquals("/user/create", line.getPath());

        Map<String, String> params = line.getParams();
        assertEquals(2, params.size());
    }
}
