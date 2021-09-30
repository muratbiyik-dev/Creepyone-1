package com.pureblacksoft.creepyone.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.activity.ArticleActivity;
import com.pureblacksoft.creepyone.activity.ListActivity;
import com.pureblacksoft.creepyone.activity.MainActivity;
import com.pureblacksoft.creepyone.adapter.ArticleAdapter;
import com.pureblacksoft.creepyone.data.Article;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.util.DownloadCallback;
import com.pureblacksoft.creepyone.veriable.SearchVeriable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SearchFragment extends Fragment
{
    private Context mContext;
    private MainActivity mActivity;
    private RecyclerView recyclerViewSF;
    private ArticleAdapter articleAdapter;
    private SearchView searchViewSF;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        mContext = requireContext();

        mActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //RecyclerViewSF
        recyclerViewSF = view.findViewById(R.id.recyclerViewSF);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewSF.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(mContext.getDrawable(R.drawable.shape_divider_story)));
        recyclerViewSF.addItemDecoration(dividerItemDecoration);
        //RecyclerViewSF/

        //Get Articles
        setArticleAdapter();
        //Get Articles/

        //Download Control
        mActivity.setDownloadCallback(new DownloadCallback()
        {
            @Override
            public void onDataChange()
            {
                setArticleAdapter();
            }

            @Override
            public void outDataChange() {}
        });
        //Download Control/

        //Search
        LinearLayout llNoResultSF = view.findViewById(R.id.llNoResultSF);
        NestedScrollView scrollViewSF = view.findViewById(R.id.scrollViewSF);

        searchViewSF = view.findViewById(R.id.searchViewSF);
        searchViewSF.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextChange(String newText)
            {
                articleAdapter.getTitleFilter().filter(newText);

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (articleAdapter.getItemCount() == 0)
                {
                    SearchVeriable.noResultVisible = true;

                    llNoResultSF.setVisibility(View.VISIBLE);
                }

                searchViewSF.clearFocus();

                return true;
            }
        });

        searchViewSF.setOnQueryTextFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
            {
                if (llNoResultSF.getVisibility() == View.VISIBLE)
                {
                    SearchVeriable.noResultVisible = false;

                    llNoResultSF.setVisibility(View.GONE);
                }

                if (scrollViewSF.getVisibility() == View.VISIBLE)
                {
                    SearchVeriable.scrollViewGone = true;

                    scrollViewSF.setVisibility(View.GONE);
                }
            }
            else
            {
                if (articleAdapter.getItemCount() == 0 && llNoResultSF.getVisibility() == View.GONE && scrollViewSF.getVisibility() == View.GONE)
                {
                    SearchVeriable.scrollViewGone = false;

                    scrollViewSF.setVisibility(View.VISIBLE);
                }
            }
        });

        if (SearchVeriable.query != null && !SearchVeriable.query.isEmpty())
        {
            if (SearchVeriable.noResultVisible)
            {
                llNoResultSF.setVisibility(View.VISIBLE);
            }

            if (SearchVeriable.scrollViewGone)
            {
                scrollViewSF.setVisibility(View.GONE);
            }
        }
        //Search/

        //Row Connections
        LinearLayout llPopularRowSF = view.findViewById(R.id.llPopularRowSF);
        llPopularRowSF.setOnClickListener((View v) ->
        {
            ListActivity.selectedList = 1;
            Intent intent = new Intent(mContext, ListActivity.class);
            startActivity(intent);
        });

        LinearLayout llRandomRowSF = view.findViewById(R.id.llRandomRowSF);
        llRandomRowSF.setOnClickListener((View v) ->
        {
            ArrayList<Article> articleList = DataLoader.articleDB.getArticleList();
            int articleListSize = articleList.size();
            if (articleListSize == 0)
            {
                Toast noStoryToast = Toast.makeText(mContext, R.string.No_Story_Toast, Toast.LENGTH_LONG);
                noStoryToast.setGravity(Gravity.BOTTOM, 0, 160);
                noStoryToast.show();
            }
            else
            {
                ArticleAdapter.selectedArticle = articleList.get(new Random().nextInt(articleListSize));
                Intent intent = new Intent(mContext, ArticleActivity.class);
                startActivity(intent);
            }
        });
        //Row Connections/

        return view;
    }

    private void setArticleAdapter()
    {
        LogHelper.LogD("Download", "{setArticleAdapter SF} -> Running.");

        articleAdapter = new ArticleAdapter(mContext, DataLoader.articleDB.getArticleList());
        recyclerViewSF.setAdapter(articleAdapter);
        articleAdapter.getTitleFilter().filter("");
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //Update Adapter
        if (articleAdapter != null)
        {
            articleAdapter.notifyDataSetChanged();
        }
        //Update Adapter/
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        SearchVeriable.query = searchViewSF.getQuery().toString();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        mActivity = null;

        mContext = null;
    }
}

//PureBlack Software / Murat BIYIK