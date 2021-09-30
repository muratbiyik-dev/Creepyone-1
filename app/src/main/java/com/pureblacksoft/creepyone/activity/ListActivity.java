package com.pureblacksoft.creepyone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.adapter.ArticleAdapter;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.loader.PrefLoader;

import java.util.Objects;

public class ListActivity extends AppCompatActivity
{
    public static int selectedList;

    private RecyclerView recyclerViewLA;
    private LinearLayoutManager linearLayoutManager;
    private ArticleAdapter articleAdapter;
    private Parcelable state;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Preference Control
        PrefLoader prefLoader = new PrefLoader();
        prefLoader.prefControl(this);
        //Preference Control/

        setContentView(R.layout.activity_list);

        //Toolbar
        Toolbar toolbarLA = findViewById(R.id.toolbarLA);
        setSupportActionBar(toolbarLA);

        //Back Button
        ImageButton ibBackLA = findViewById(R.id.ibBackLA);
        ibBackLA.setOnClickListener((View v) -> finish());
        //Back Button/

        //Title
        TextView tvToolbarTitleLA = findViewById(R.id.toolbarTitleLA);
        TextView tvNoDataLA = findViewById(R.id.tvNoDataLA);
        switch (selectedList)
        {
            case 1:
                tvToolbarTitleLA.setText(R.string.Pop_Stories_Title);
                tvNoDataLA.setText(R.string.No_Pop_Story);
                break;

            case 2:
                tvToolbarTitleLA.setText(R.string.Fav_Stories_Title);
                tvNoDataLA.setText(R.string.No_Fav_Story);
                break;

            case 3:
                tvToolbarTitleLA.setText(R.string.Short_Stories_Title);
                tvNoDataLA.setText(R.string.No_Short_Story);
                break;

            case 4:
                tvToolbarTitleLA.setText(R.string.Medium_Stories_Title);
                tvNoDataLA.setText(R.string.No_Medium_Story);
                break;

            case 5:
                tvToolbarTitleLA.setText(R.string.Long_Stories_Title);
                tvNoDataLA.setText(R.string.No_Long_Story);
                break;
        }
        //Title/
        //Toolbar/

        //RecyclerView Settings
        recyclerViewLA = findViewById(R.id.recyclerViewLA);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewLA.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(getDrawable(R.drawable.shape_divider_story)));
        recyclerViewLA.addItemDecoration(dividerItemDecoration);
        //RecyclerView Settings/
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        //Get Articles
        setArticleAdapter();
        emptyAdapterControl();
        //Get Articles/

        //Restore State
        linearLayoutManager.onRestoreInstanceState(state);
        //Restore State/
    }

    private void setArticleAdapter()
    {
        LogHelper.LogD("Download", "{setArticleAdapter LA} -> Running.");

        switch (selectedList)
        {
            case 1:
                articleAdapter = new ArticleAdapter(this, DataLoader.articleDB.getPopArticleList());
                break;

            case 2:
                articleAdapter = new ArticleAdapter(this, DataLoader.articleDB.getFavArticleList(this));
                break;

            case 3:
                articleAdapter = new ArticleAdapter(this, DataLoader.articleDB.getShortArticleList());
                break;

            case 4:
                articleAdapter = new ArticleAdapter(this, DataLoader.articleDB.getMediumArticleList());
                break;

            case 5:
                articleAdapter = new ArticleAdapter(this, DataLoader.articleDB.getLongArticleList());
                break;
        }
        recyclerViewLA.setAdapter(articleAdapter);
    }

    private void emptyAdapterControl()
    {
        LinearLayout llNoDataLA = findViewById(R.id.llNoDataLA);
        if (articleAdapter.getItemCount() == 0)
        {
            llNoDataLA.setVisibility(View.VISIBLE);
        }
        else
        {
            if (llNoDataLA.getVisibility() == View.VISIBLE)
            {
                llNoDataLA.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        //Save State
        state = linearLayoutManager.onSaveInstanceState();
        //Save State/
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }
}

//PureBlack Software / Murat BIYIK
