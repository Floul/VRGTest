package com.example.android.vrgtest.MainActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.vrgtest.Database.Article;
import com.example.android.vrgtest.Repository;

import java.util.List;


public class MainViewModel extends AndroidViewModel {

    private Repository repository;
    private LiveData<List<Article>> mostEmailedArticles;
    private LiveData<List<Article>> mostSharedArticles;
    private LiveData<List<Article>> mostViewedArticles;
    private LiveData<List<Article>> favoriteArticles;
    private LiveData<Article> selectedArticle;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        mostEmailedArticles = repository.getMostEmailedArticles();
        mostSharedArticles = repository.getMostSharedArticles();
        mostViewedArticles = repository.getMostViewedArticles();
        favoriteArticles = repository.getFavoriteArticles();

    }

    void callApiAndStoreData(int category) {
        repository.callApiAndStoreData(category);
    }

    void insert(Article article) {
        repository.insert(article);
    }

    LiveData<List<Article>> getMostEmailedArticles() {
        return mostEmailedArticles;
    }
    LiveData<List<Article>> getMostSharedArticles() {
        return mostSharedArticles;

    }
    LiveData<List<Article>> getMostViewedArticles() {
        return mostViewedArticles;
    }


    LiveData<Article> getSelectedArticle(String url) {
        selectedArticle = repository.getSelectedArticle(url);
        return selectedArticle;
    }

    public LiveData<List<Article>> getFavoriteArticles() {
        return favoriteArticles;
    }

    public void updateSelectedArticle(String url) {
        repository.updateSelectedArticle(url);
    }

    public void addToFavorites(String url) {
        repository.addToFavorites(url);
    }
    public void removeFromFavorites(String url) {
        repository.removeFromFavorites(url);
    }


}
