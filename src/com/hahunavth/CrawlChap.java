package com.hahunavth;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class CrawlChap {

    Help help = new Help();
    ArrayList<Chapter> listChap = new ArrayList<>();
    String url;

    public CrawlChap(String url)    {
        this.url    = url;
    }

    public void GetListChap() throws IOException {
        listChap = getAllChapInPage(url);
        printListChap();
    }

    public void SaveChap(String direct) throws IOException {
        String chapNumber = help.inputString();
        Chapter chap = getChapByChapNumber(chapNumber);
        System.out.println("Chap : "+ chap.getChapter());
        saveFile(chap, direct);
    }

    public Chapter getChapByChapNumber(String chapNumber) {
        for (int i = 0; i < listChap.size(); i++) {
            if (listChap.get(i).getChapter().equalsIgnoreCase(chapNumber)) {
                System.out.println("|||Get chap" + chapNumber + "|||");
                return listChap.get(i);
            }
        }
        System.out.println("No chap has name : " + chapNumber);
        return null;
    }

    private void printListChap() {
        System.out.println("List Chaps : ");
        for (int i = 0; i < listChap.size(); i++) {
            System.out.println("Chap : |" + listChap.get(i).getChapter() + "|");
        }
    }

    public ArrayList<Chapter> getAllChapInPage(String urls) throws IOException {
        ArrayList<Chapter> list_chap = new ArrayList<>();
        Document document = Jsoup.connect(urls).get();
        Elements elms = document.getElementsByClass("chap-item");
        for (int i = 0; i < elms.size(); i++) {
            Elements elm_row = elms.get(i).getElementsByTag("a");
            for (int j = 0; j < elm_row.size(); j++) {
                String linkChap = elm_row.get(j).attr("href");
                String linkChap2 = "https://saytruyen.com/" + linkChap;
                list_chap.add(new Chapter(linkChap2));
            }
        }
        return list_chap;
    }

    public ArrayList<String> listImgOnPage(String pageURL) throws IOException {
        Document document = Jsoup.connect(pageURL).get();
        ArrayList<String> list_img = new ArrayList<>();
        Elements elms = document.getElementById("lst_content").select("img");

        for (int i = 0; i < elms.size(); i++) {
            String url = elms.get(i).attr("src");
            if (url.equals("")) {
                continue;
            }
            list_img.add(url);
        }
        System.out.println(document.getElementsByClass("lst_content").select("img"));
        return list_img;
    }

    private void saveImg(String src_image, String name, String dir) throws IOException{
        Image image = null;
       try {
            URL url = new URL(src_image);
           System.out.println(src_image);
            image = ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("savedddddddd");
    }


    private void saveFile(Chapter chap, String dir) throws IOException{
        try {
            System.out.println(chap.getUrl());
            ArrayList<String> list_img = listImgOnPage(chap.getUrl());
            System.out.println(list_img.size());
            for (int i = 0; i < list_img.size(); i++) {
                String name = i + "jpg";
                saveImg(list_img.get(i), name, dir);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error", "Error to save file !", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "Dowload sucessfull chap " + chap.getChapter());
    }

}
