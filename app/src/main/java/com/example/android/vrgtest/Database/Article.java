package com.example.android.vrgtest.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;

@Entity(tableName = "articles_table")
public class Article {

    public static final int MOST_EMAILED_CATEGORY = 1;
    public static final int MOST_SHARED_CATEGORY = 2;
    public static final int MOST_VIEWED_CATEGORY = 3;
    public static final int FAVORITE = 1;
    public static final int NOT_FAVORITE = 0;


    private int category;

    @NonNull
    @PrimaryKey
    private String title;

    private String subTitle;

    private String body;

    private String imageUrl;

    private String pageUrl;

    private String publishedDate;

    private int isFavorite;

    public Article(int category, @NonNull String title, String subTitle, @Nullable String body, String imageUrl, String pageUrl, String publishedDate) {
        this.category = category;
        this.title = title;
        this.subTitle = subTitle;
        this.body = body;
        this.imageUrl = imageUrl;
        this.pageUrl = pageUrl;
        this.publishedDate = publishedDate;
        this.isFavorite=0;
    }


    public int getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getBody() {
        return body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public int isFavorite() {
        return isFavorite;
    }

    public void setBody(String body) {
        this.body = body;
    }
}


