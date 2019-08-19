package com.example.android.vrgtest;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Update;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.example.android.vrgtest.Database.Article;
import com.example.android.vrgtest.Database.ArticleDao;
import com.example.android.vrgtest.Database.ArticleDatabase;
import com.example.android.vrgtest.Networking.JSONData;
import com.example.android.vrgtest.Networking.WebApi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Repository {

    private ArticleDao articleDao;
    Application application;
    LiveData<List<Article>> mostEmailedArticles;
    LiveData<List<Article>> mostSharedArticles;
    LiveData<List<Article>> mostViewedArticles;
    LiveData<List<Article>> favoriteArticles;


    public Repository(Application application) {
        this.application = application;
        ArticleDatabase database = ArticleDatabase.getDatabase(application);
        articleDao = database.articleDao();
        mostEmailedArticles = articleDao.getMostEmailedArticles();
        mostSharedArticles = articleDao.getMostSharedArticles();
        mostViewedArticles = articleDao.getMostViewedArticles();
        favoriteArticles = articleDao.getFavoriteArticles();
    }

    public void callApiAndStoreData(final int category) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WebApi webApi = retrofit.create(WebApi.class);
        Call<JSONData> call;
        switch (category){
            case (Article.MOST_EMAILED_CATEGORY):
                call = webApi.getMostEmailedArticles();
                break;

                   case (Article.MOST_SHARED_CATEGORY):
                call = webApi.getMostSharedArticles();
                break;

                   case (Article.MOST_VIEWED_CATEGORY):
                call = webApi.getMostViewedArticles();
                break;
                default:call=null;

        }
        call.enqueue(new Callback<JSONData>() {
            @Override
            public void onResponse(Call<JSONData> call, Response<JSONData> response) {

                JSONData responseData = response.body();
                final JSONData.ArticleData[] results = responseData.getResults();

                new Thread() {
                    @Override
                    public void run() {
                        for (JSONData.ArticleData articleData : results
                        ) {
                            insert(new Article(category,
                                    articleData.getTitle(),
                                    articleData.getSubTitle(),
                                    null,
                                    articleData.getImageURL(),
                                    articleData.getPageURL(),
                                    articleData.getPublishedDate()));
                        }
                    }
                }.start();


            }

            @Override
            public void onFailure(Call<JSONData> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "API connection failure. " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void insert(final Article article) {
        articleDao.insertArticle(article);
    }

    public LiveData<List<Article>> getMostEmailedArticles() {
        return mostEmailedArticles;
    }

    public LiveData<List<Article>> getMostSharedArticles() {
        return mostSharedArticles;
    }

    public LiveData<List<Article>> getMostViewedArticles() {
        return mostViewedArticles;
    }

    public LiveData<Article> getSelectedArticle(String url) {
        LiveData<Article> selectedArticle = null;

        try {
            selectedArticle = new GetSelectedArticle(articleDao).execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return selectedArticle;
    }

    static class GetSelectedArticle extends AsyncTask<String, Void, LiveData<Article>> {

        private ArticleDao mDao;

        GetSelectedArticle(ArticleDao dao) {
            mDao = dao;
        }


        @Override
        protected LiveData<Article> doInBackground(String... strings) {
            return mDao.getArticle(strings[0]);
        }
    }

    public void updateSelectedArticle(String url) {
        String body = "";
        AsyncParsing asyncParsing = new AsyncParsing(url);
        try {
            body = asyncParsing.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (body != null) {
            new UpdateSelectedArticle(articleDao, url, body).execute();
        }
    }

    static class UpdateSelectedArticle extends AsyncTask<Void, Void, Void> {

        private ArticleDao mDao;
        private String mUrl;
        private String mBody;

        UpdateSelectedArticle(ArticleDao dao, String url, String body) {
            mDao = dao;
            mUrl = url;
            mBody = body;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mDao.updateArticle(mUrl, mBody);
            return null;
        }
    }

    static class AsyncParsing extends AsyncTask<String, Void, String> {

        String url;

        AsyncParsing(String url) {
            this.url = url;
        }

        @Override
        protected String doInBackground(String... strings) {
            Document document = null;
            try {
                document = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            StringBuilder stringBuilder = new StringBuilder();
            Elements h1elements = document.getElementsByTag("p");
            for (int i = 5; i < h1elements.size() - 4; i++) {
                Element h1element = h1elements.get(i);
                stringBuilder.append(h1element.text());
                stringBuilder.append(System.getProperty("line.separator"));
                stringBuilder.append(System.getProperty("line.separator"));
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void addToFavorites(String url) {
        new AddToFavorites(articleDao).execute(url);
    }

    static class AddToFavorites extends AsyncTask<String, Void, Void> {
        ArticleDao mDao;

        AddToFavorites(ArticleDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mDao.addToFavorites(strings[0]);
            return null;
        }
    }


    public void removeFromFavorites(String url) {
        new RemoveFromFavorites(articleDao).execute(url);
    }

    static class RemoveFromFavorites extends AsyncTask<String, Void, Void> {
        ArticleDao mDao;

        RemoveFromFavorites(ArticleDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            mDao.removeFromFavorites(strings[0]);
            return null;
        }
    }

    public LiveData<List<Article>> getFavoriteArticles (){
        return favoriteArticles;
    }

}

