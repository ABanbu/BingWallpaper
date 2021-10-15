package com;

import java.io.IOException;

public class Wallpaper {

    public static void main(String[] args) throws IOException {
        System.out.println("========开始下载今日Bing壁纸=======");
        new ImageUtils().imagesDownloader();
        //new ImageUtils().imageDownloader();
        System.out.println("===========下载壁纸结束==========");
    }
}
