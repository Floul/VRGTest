package com.example.android.vrgtest.MainActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.vrgtest.Database.Article;
import com.example.android.vrgtest.R;

import java.util.List;

public class FavoriteNewsActivity extends AppCompatActivity {

    private MainViewModel viewModel;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_news);

        recyclerView = findViewById(R.id.favorite_news_rv);
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String clickeditemUrl = adapter.getItemUrl(position);
                String clickeditemBody = adapter.getItemBody(position);
                int clickeditemIsFavorite = adapter.getIsFavorite(position);
                openSelectedNews(clickeditemUrl, clickeditemBody, clickeditemIsFavorite);
            }
        });


        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavoriteArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                adapter.setItems(articles);
            }
        });
    }

    private void openSelectedNews(String url, String body, int isFavorite) {
        Intent openSelectedPageIntent = new Intent(this, SingleNewsActivity.class);
        openSelectedPageIntent.putExtra("url", url);
        openSelectedPageIntent.putExtra("body", body);
        openSelectedPageIntent.putExtra("isFavorite", isFavorite);

        startActivity(openSelectedPageIntent);
    }


}
