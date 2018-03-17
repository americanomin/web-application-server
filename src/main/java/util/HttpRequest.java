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

        int contentLength = 0;
        String line = br.readLine();
        while (line != null && !line.equals("")) {
            header.put(HttpRequestUtils.parseHeader(line).getKey(), HttpRequestUtils.parseHeader(line).getValue());
            if (line.contains("Content-Length")) {
                contentLength = getContentLength(line);
            }
            line = br.readLine();
        }

        if(method.equals("GET")){
            params = HttpRequestUtils.parseQueryString(UrlToken[1]);
        }else if(method.equals("POST")){
            String body = IOUtils.readData(br, contentLength);
            params = HttpRequestUtils.parseQueryString(body);
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

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }
}
