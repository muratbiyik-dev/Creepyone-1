package com.pureblacksoft.creepyone.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pureblacksoft.creepyone.activity.ArticleActivity;
import com.pureblacksoft.creepyone.data.Article;
import com.pureblacksoft.creepyone.loader.PrefLoader;
import com.pureblacksoft.creepyone.R;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder>
{
    public static Article selectedArticle;

    private Context mContext;
    private ArrayList<Article> mArticleList;
    private ArrayList<Article> filteredArticleList;
    private LayoutInflater inflater;

    public ArticleAdapter(Context context, ArrayList<Article> articleList)
    {
        super();

        this.mContext = context;
        this.mArticleList = articleList;
        this.filteredArticleList = articleList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Preference Control
        PrefLoader prefLoader = new PrefLoader();
        prefLoader.prefControl(mContext);
        //Preference Control/

        //ArticleCard Connections
        View view = inflater.inflate(R.layout.card_article, parent, false);
        return new ViewHolder(view);
        //ArticleCard Connections/
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        selectedArticle = filteredArticleList.get(position);
        holder.setData(selectedArticle, position);
    }

    @Override
    public int getItemCount()
    {
        return filteredArticleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Integer articleId;
        TextView tvTitleAC;
        TextView tvIntroAC;
        TextView tvReadingTimeAC;

        ViewHolder(View itemView)
        {
            super(itemView);

            tvTitleAC = itemView.findViewById(R.id.titleAC);
            tvIntroAC = itemView.findViewById(R.id.introAC);
            tvReadingTimeAC = itemView.findViewById(R.id.readingTimeAC);

            //ArticleActivity Connections
            itemView.setOnClickListener((View v) ->
            {
                selectedArticle = filteredArticleList.get(getAdapterPosition());

                Intent intent = new Intent(mContext, ArticleActivity.class);
                mContext.startActivity(intent);
            });
            //ArticleActivity Connections/
        }

        void setData(Article selectedArticle, int position)
        {
            //Set Article Data
            this.articleId = selectedArticle.getId();
            this.tvTitleAC.setText(selectedArticle.getTitle());
            this.tvIntroAC.setText(selectedArticle.getContent());
            this.tvReadingTimeAC.setText(selectedArticle.getReadingTimeStr());
            //Set Article Data/

            //Title Color Change
            if (ArticleActivity.readedIdList.contains(articleId))
            {
                tvTitleAC.setTextColor(ContextCompat.getColor(mContext, R.color.bright_red_light));
            }
            else
            {
                tvTitleAC.setTextColor(ContextCompat.getColor(mContext, R.color.bright_red));
            }
            //Title Color Change/
        }
    }

    //SearchView Filter
    public Filter getTitleFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                ArrayList<Article> filteredList = new ArrayList<>();
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty())
                {
                    filteredList.clear();
                }
                else
                {
                    for (Article article : mArticleList)
                    {
                        if (article.getTitle().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filteredList.add(article);
                        }
                    }
                }
                filteredArticleList = filteredList;

                FilterResults results = new FilterResults();
                results.values = filteredArticleList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                filteredArticleList = (ArrayList<Article>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    //SearchView Filter/
}

//PureBlack Software / Murat BIYIK