package com.hahunavth;

public class Chapter {
    private String url;
    private String chapter;

    public Chapter(String url)  {
        this.url    = url;
        chapter     = chapNumber(url);
    }

    public String getUrl()  {
        return url;
    }
    public String getChapter()  {
        return chapter;
    }

    private String chapNumber(String url)   {
        int idx = 0;
        String tmp = "";
        for(int i = 0; i < url.length(); i++)   {
            if(Character.isDigit(url.charAt(i)))    {
                idx = i;
                break;
            }
        }
        tmp = url.substring(idx);
        for(int i = tmp.length() - 1; i > 0; i--)   {
            if(!Character.isDigit(tmp.charAt(i)))    {
                idx = i;
            }
        }
        return tmp.substring(0, idx);
    }

}
