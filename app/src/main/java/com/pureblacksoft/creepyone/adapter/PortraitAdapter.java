package com.pureblacksoft.creepyone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.activity.ArticleActivity;
import com.pureblacksoft.creepyone.data.Article;
import com.pureblacksoft.creepyone.data.Portrait;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.loader.PrefLoader;

import java.util.ArrayList;

public class PortraitAdapter extends RecyclerView.Adapter<PortraitAdapter.ViewHolder>
{
    public static Portrait selectedPortrait;

    private Context mContext;
    private ArrayList<Portrait> mPortraitList;
    private LayoutInflater inflater;

    public PortraitAdapter(Context context, ArrayList<Portrait> portraitList)
    {
        super();

        this.mContext = context;
        this.mPortraitList = portraitList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //Preference Control
        PrefLoader prefLoader = new PrefLoader();
        prefLoader.prefControl(mContext);
        //Preference Control/

        //PortraitCard Connections
        View view = inflater.inflate(R.layout.card_portrait, parent, false);
        return new ViewHolder(view);
        //PortraitCard Connections/
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        selectedPortrait = mPortraitList.get(position);
        holder.setData(selectedPortrait, position);
    }

    @Override
    public int getItemCount()
    {
        return mPortraitList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        Integer portraitId;
        TextView tvTitlePC;
        ImageView ivImagePC;

        ViewHolder(View itemView)
        {
            super(itemView);

            tvTitlePC = itemView.findViewById(R.id.titlePC);
            ivImagePC = itemView.findViewById(R.id.imagePC);

            //ArticleActivity Connections
            itemView.setOnClickListener((View v) ->
            {
                selectedPortrait = mPortraitList.get(getAdapterPosition());
                ArrayList<Article> articleList = DataLoader.articleDB.getArticleList();
                ArticleAdapter.selectedArticle = articleList.get(DataLoader.artCount - selectedPortrait.getIndex() - 1);

                Intent intent = new Intent(mContext, ArticleActivity.class);
                mContext.startActivity(intent);
            });
            //ArticleActivity Connections/
        }

        void setData(Portrait selectedPortrait, int position)
        {
            //Set Portrait Data
            this.portraitId = selectedPortrait.getId();
            this.tvTitlePC.setText(selectedPortrait.getTitle());
            byte[] imageBytes = selectedPortrait.getImage();
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            this.ivImagePC.setImageBitmap(Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), false));
            //Set Portrait Data/

            //Title Color Change
            if (ArticleActivity.readedIdList.contains(portraitId))
            {
                tvTitlePC.setTextColor(ContextCompat.getColor(mContext, R.color.bright_red_light));
            }
            else
            {
                tvTitlePC.setTextColor(ContextCompat.getColor(mContext, R.color.bright_red));
            }
            //Title Color Change/
        }
    }
}

//PureBlack Software / Murat BIYIK