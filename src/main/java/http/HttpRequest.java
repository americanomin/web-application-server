package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private String method;
    private String path;
    private HashMap<String, String> headers = new HashMap<>();
    private Map<String, String> params;

    public HttpRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            if(line == null){
                return;
            }

            processRequestLine(line);

            line = br.readLine();

            while (line != null && !line.equals("")) {
                log.debug("headers : {}", line);
                String[] tokens = line.split(":");

                headers.put(tokens[0].trim(), tokens[1].trim());
                line = br.readLine();
            }

            if(method.equals("POST")){
                String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                params = HttpRequestUtils.parseQueryString(body);
            }
        }catch (IOException io){
            log.error(io.getMessage());
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
        return headers.get(key);
    }

    private int getContentLength(String line) {
        String[] headerTokens = line.split(":");
        return Integer.parseInt(headerTokens[1].trim());
    }

    public void processRequestLine(String requestLine) {
        log.debug("request line : {}", requestLine);
        String[] tokens = requestLine.split(" ");
        method = tokens[0];

        if("POST".equals(method)){
            path = tokens[1];
            return;
        }

        int index = tokens[1].indexOf("?");

        if(index == -1){
            path = tokens[1];
        }else{
            path = tokens[1].substring(0, index);
            params = HttpRequestUtils.parseQueryString(tokens[1].substring(index+1));
        }
     }
}
