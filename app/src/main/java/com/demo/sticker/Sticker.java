package com.demo.sticker;

public class Sticker {
    private final String name;
    private final int imageResource;
    private final String imageUrl;
    private final String keywords[];


    public Sticker(String name, int imageResource, String imageUrl, String keywords[]) {
        this.name = name;
        this.imageResource = imageResource;
        this.imageUrl = imageUrl;
        this.keywords = keywords;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String [] getKeywords() { return keywords; }
}