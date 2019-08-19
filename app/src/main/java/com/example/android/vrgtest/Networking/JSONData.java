package com.example.android.vrgtest.Networking;

import com.google.gson.annotations.SerializedName;

public class JSONData {

    @SerializedName("results")
    private ArticleData[] results;

    public ArticleData[] getResults() {
        return results;
    }

    public class ArticleData {

        private String title;

        @SerializedName("abstract")
        private String subTitle;

//        @SerializedName("url")
//        private String imageURL;

        @SerializedName("url")
        private String pageURL;

        @SerializedName("published_date")
        private String publishedDate;


        public String getTitle() {
            return title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public String getPageURL() {
            return pageURL;
        }

        public String getImageURL() {
            return "";
        }

        public String getPublishedDate() {
            return publishedDate;
        }
    }


}
