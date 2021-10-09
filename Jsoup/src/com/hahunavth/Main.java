package com.hahunavth;

import java.io.IOException;

public class Main{

    public static void main(String[] args) throws IOException {
        test1(args);
    }

    public static void test1(String[] args) throws IOException {
        Help helper = new Help();
        String direct = helper.getDirect();
        System.out.println(direct.length());
        String url = "https://saytruyen.net/truyen-toan-tri-doc-gia.html";
        CrawlChap crawler = new CrawlChap(url);
        while (true) {
            showMenu();
            int choice = helper.getIntInRange(0, 2);
            switch (choice) {
                case 1:
                    crawler.GetListChap();
                    break;
                case 2:
                    crawler.SaveChap(direct);
                    break;
                case 0:
                    return;
            }
        }

    }

    static void showMenu() {
        System.out.println("----- Crawler -----");
        System.out.println("1. Get list Chap.");
        System.out.println("2. Dowload chap");
        System.out.println("0. Exit");
    }

}
