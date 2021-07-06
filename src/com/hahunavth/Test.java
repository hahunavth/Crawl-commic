package com.hahunavth;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Test {
    public static void main(String[] args) {
        String url = "https://timtruyen.net/truyen-tranh/vo-luyen-dinh-phong";
        try{
            Document document = Jsoup.connect(url).get();
            Elements doc2 = document.getElementsByClass("chapter-item").select("a");
            List<String> chapter_link = doc2.eachAttr("href");
            for(String chapter : chapter_link) {
//            String chapter = chapter_link.get(0);
                getImg(chapter);
            }
//            System.out.println(chapter_link);
        }catch(IOException e) {
            System.out.println(e);
        }
    }

    public static void getImg(String chapter, Integer count) {
        if(count < 5) {
            try{
                System.out.println(chapter);
                Document document1 = Jsoup.connect(chapter).get();
                Elements es = document1.getElementsByClass("lazy");
                List<String> imgList = es.eachAttr("data-src");
                System.out.println(imgList);
            }catch (SocketTimeoutException e) {
                System.out.println(e);
                System.out.println("re crawl");
                getImg(chapter);
            }catch(IOException e) {
                System.out.println(e);
                System.out.println("re crawl");
                getImg(chapter);
            }
        }else{
            System.out.println("Try 5 times -> CACEL");
            count++;
            getImg(chapter, count);
        }
    }

    public static void getImg(String chapter) {
        getImg(chapter, 0);
    }
}

