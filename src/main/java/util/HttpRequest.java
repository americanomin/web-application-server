package util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String path;
    private String method;
    private HashMap<String, String> header = new HashMap<>();
    private Map<String, String> params;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String[] RequestLine = br.readLine().split(" ");
        method = RequestLine[0];
        String[] UrlToken =  RequestLine[1].split("\\?");
        path = UrlToken[0];
        params = HttpRequestUtils.parseQueryString(UrlToken[1]);

        String line = br.readLine();
        while (line != null && !line.equals("")) {
            header.put(HttpRequestUtils.parseHeader(line).getKey(), HttpRequestUtils.parseHeader(line).getValue());
            line = br.readLine();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPrameter(String key) {
        return params.get(key);
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String key) {
        return header.get(key);
    }
}
