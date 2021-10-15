package com;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 使用Java原生进行Http请求
 */
public class HttpUtils {
    /**
     * 获取Response
     * 以JSON字符串的形式
     *
     * @param url url
     * @return json
     */
    public static JSONObject getHttpResponse(String url) {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.connect();
            if (200 == con.getResponseCode()) {
                return getStreamToJson(con.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return new JSONObject();
    }

    private static JSONObject getStreamToJson(InputStream inputStream) {
        StringBuffer data = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONObject.parseObject(data.toString());
    }
}
