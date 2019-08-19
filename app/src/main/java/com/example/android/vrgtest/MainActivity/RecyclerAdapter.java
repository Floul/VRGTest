package com.example.android.vrgtest.MainActivity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.vrgtest.Database.Article;
import com.example.android.vrgtest.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ArticleViewHolder> {

    private List<Article> allArticles = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    void setItems(List<Article> articles) {
        allArticles.clear();
        allArticles.addAll(articles);
        notifyDataSetChanged();
    }

    String getItemUrl(int position){
        return  allArticles.get(position).getPageUrl();
    }

    public String getItemBody (int position){
        return  allArticles.get(position).getBody();
    }
    public int getIsFavorite (int position){
        return  allArticles.get(position).isFavorite();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ArticleViewHolder(itemView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int i) {
        articleViewHolder.titleTextView.setText(allArticles.get(i).getTitle());
        articleViewHolder.subTitleTextView.setText(allArticles.get(i).getSubTitle());
    }

    @Override
    public int getItemCount() {
        return allArticles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView subTitleTextView;

        public ArticleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_tv);
            subTitleTextView = itemView.findViewById(R.id.sub_title_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        int position = getAdapterPosition();
                        if (position!=RecyclerView.NO_POSITION);
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
