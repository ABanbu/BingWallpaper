package com;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片url解析
 * 图片下载器
 */
public class ImageUtils {
    private final static String ORIGIN_URL = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
    private final static String BING_URL = "https://cn.bing.com";

    /**
     * 解析originUrl，获取图片url和name
     *
     * @param originUrl originUrl
     * @return list{name,url}
     */
    public static List<JSONObject> getImageResponse(String originUrl) {
        JSONObject response = HttpUtils.getHttpResponse(originUrl);
        System.out.printf(">>>>>OriginUrl Response: %s \n", response);
        //读取images
        JSONArray images = response.getJSONArray("images");
        JSONObject temp = null;
        List<JSONObject> result = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            //获取name 和 url
            String name = (image.getString("copyright").split("\\(©")[0].trim()) + ".jpg";
            //String name = LocalDate.now() + ".jpg";
            String url = BING_URL + image.getString("urlbase") + "_UHD.jpg";
            temp = new JSONObject();
            temp.put("name", name);
            temp.put("url", url);
            result.add(temp);
        }
        return result;
    }

    /**
     * 多图片下载器
     * limit 8
     */
    public void imagesDownloader() {
        List<JSONObject> list = getImageResponse(ORIGIN_URL + "0");
        for (JSONObject image : list) {
            String name = image.getString("name");
            String url = image.getString("url");
            downloader(name, url);
        }
    }

    /**
     * 单图片下载器
     * 当日图片
     */
    public void imageDownloader() {
        List<JSONObject> list = getImageResponse(ORIGIN_URL);
        for (JSONObject image : list) {
            String name = image.getString("name");
            String url = image.getString("url");
            downloader(name, url);
        }
    }

    /**
     * 下载器
     *
     * @param name 图片名称
     * @param url  图片url
     */
    private void downloader(String name, String url) {
        System.out.printf(">>>>>Image Downloader: name={%s}, url={%s} \n", name, url);
        HttpURLConnection con = null;
        InputStream content = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            con.connect();
            if (200 == con.getResponseCode()) {
                content = con.getInputStream();
            }
            if (null != content) {
                String pathname = "src/main/resources/img/" + name;
                File imageFile = new File(pathname);
                //创建流
                try (
                        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                        FileOutputStream fileOutStream = new FileOutputStream(imageFile)
                ) {
                    //创建一个Buffer字符串
                    byte[] buffer = new byte[1024];
                    //每次读取的字符串长度，如果为-1，代表全部读取完毕
                    int len = 0;
                    //使用一个输入流从buffer里把数据读取出来
                    while ((len = content.read(buffer)) != -1) {
                        //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                        outStream.write(buffer, 0, len);
                    }
                    //得到图片的二进制数据，以二进制封装得到数据，具有通用性
                    byte[] data = outStream.toByteArray();
                    //写入数据
                    fileOutStream.write(data);
                } finally {
                    content.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private String getPath(){
        return this.getClass().getClassLoader().getResource("/").getPath();
    }
}
