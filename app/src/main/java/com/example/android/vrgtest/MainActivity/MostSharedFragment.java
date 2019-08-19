package com.example.android.vrgtest.MainActivity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vrgtest.Database.Article;
import com.example.android.vrgtest.R;

import java.util.List;

public class MostSharedFragment extends Fragment {

    public static final int CATEGORY = Article.MOST_SHARED_CATEGORY;
    MainViewModel viewModel;
    RecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_most_shared, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerAdapter = new RecyclerAdapter();
        recyclerView = view.findViewById(R.id.most_shared_rv);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String clickeditemUrl = recyclerAdapter.getItemUrl(position);
                String clickeditemBody = recyclerAdapter.getItemBody(position);
                int clickeditemIsFavorite = recyclerAdapter.getIsFavorite(position);
                openSelectedNews(clickeditemUrl, clickeditemBody, clickeditemIsFavorite);
            }
        });

        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        viewModel.callApiAndStoreData(CATEGORY);

        viewModel.getMostSharedArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null && articles.size() != 0)
                    recyclerAdapter.setItems(articles);
            }
        });


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.all_news_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.show_favorites:
                openFavoriteNews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openSelectedNews(String url, String body, int isFavorite) {
        Intent openSelectedPageIntent = new Intent(getContext(), SingleNewsActivity.class);
        openSelectedPageIntent.putExtra("url", url);
        openSelectedPageIntent.putExtra("body", body);
        openSelectedPageIntent.putExtra("isFavorite", isFavorite);

        startActivity(openSelectedPageIntent);
    }


    private void openFavoriteNews() {
        Intent openFavoriteNewsIntent = new Intent(getContext(), FavoriteNewsActivity.class);
        startActivity(openFavoriteNewsIntent);
    }
}
