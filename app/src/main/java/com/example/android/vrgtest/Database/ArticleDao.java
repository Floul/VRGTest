package com.example.android.vrgtest.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertArticle(Article article);

    @Query("SELECT * FROM articles_table WHERE category =" + Article.MOST_EMAILED_CATEGORY)
    LiveData<List<Article>> getMostEmailedArticles();

    @Query("SELECT * FROM articles_table WHERE category =" + Article.MOST_SHARED_CATEGORY)
    LiveData<List<Article>> getMostSharedArticles();

    @Query("SELECT * FROM articles_table WHERE category =" + Article.MOST_VIEWED_CATEGORY)
    LiveData<List<Article>> getMostViewedArticles();

    @Query("SELECT * FROM ARTICLES_TABLE WHERE pageUrl = :url")
    LiveData<Article> getArticle(String url);

    @Query("UPDATE articles_table SET body=:body WHERE pageUrl = :url")
    void updateArticle(String url, String body);

    @Query("UPDATE articles_table SET isFavorite=" + Article.FAVORITE + " WHERE pageUrl = :url")
    void addToFavorites(String url);

    @Query("UPDATE articles_table SET isFavorite=" + Article.NOT_FAVORITE + " WHERE pageUrl = :url")
    void removeFromFavorites(String url);

    @Query("SELECT * FROM articles_table WHERE isFavorite="+ Article.FAVORITE)
    LiveData<List<Article>> getFavoriteArticles();
}
