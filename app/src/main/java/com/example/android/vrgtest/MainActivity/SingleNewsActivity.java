package com.example.android.vrgtest.MainActivity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.vrgtest.Database.Article;
import com.example.android.vrgtest.MainActivity.MainViewModel;
import com.example.android.vrgtest.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class SingleNewsActivity extends AppCompatActivity {


    TextView titleTextView;
    TextView testtextView;
    MainViewModel viewModel;
    String pageUrl;
    String pageBody;
    private int isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news);


        titleTextView = findViewById(R.id.single_news_title_tv);
        testtextView = findViewById(R.id.body_text_view);


        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pageUrl = extras.getString("url");
            pageBody = extras.getString("body");
            isFavorite = extras.getInt("isFavorite");
        }

        if (pageBody == null || pageBody.equals("")) {
            viewModel.updateSelectedArticle(pageUrl);
        }

        testtextView.setText(pageUrl);

        viewModel.getSelectedArticle(pageUrl).observe(this, new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                titleTextView.setText(article.getTitle());
                testtextView.setText(article.getBody());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.single_news_menu, menu);
        MenuItem save = menu.findItem(R.id.save);
        if (isFavorite==1){
        save.setChecked(true);
        save.setIcon(R.drawable.ic_favorite_white_24dp);}
        else
            save.setChecked(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                if (!item.isChecked()) {
                    item.setIcon(R.drawable.ic_favorite_white_24dp);
                    viewModel.addToFavorites(pageUrl);
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_white_24dp);
                    viewModel.removeFromFavorites(pageUrl);

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
