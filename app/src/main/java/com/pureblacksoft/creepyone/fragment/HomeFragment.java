package com.pureblacksoft.creepyone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.activity.MainActivity;
import com.pureblacksoft.creepyone.adapter.ArticleAdapter;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.helper.TinyDB;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.util.DownloadCallback;
import com.pureblacksoft.creepyone.veriable.HomeVeriable;
import com.pureblacksoft.creepyone.veriable.MainVeriable;

import java.util.Objects;

public class HomeFragment extends Fragment
{
    private Context mContext;
    private MainActivity mActivity;
    private View mView;
    private RecyclerView recyclerViewHF;
    private LinearLayoutManager linearLayoutManager;
    private ArticleAdapter articleAdapter;
    private TinyDB tinyDB;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        mContext = requireContext();

        mActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        tinyDB = new TinyDB(mContext.getApplicationContext());

        //RecyclerViewHF
        recyclerViewHF = mView.findViewById(R.id.recyclerViewHF);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewHF.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(mContext.getDrawable(R.drawable.shape_divider_story)));
        recyclerViewHF.addItemDecoration(dividerItemDecoration);
        //RecyclerViewHF/

        //Get Articles
        if (!MainVeriable.downloadingAnimFinished)
        {
            startDownloadingAnim(true);
        }
        else
        {
            startDownloadingAnim(false);
        }
        setArticleAdapter();
        //Get Articles/

        //Download Control
        mActivity.setDownloadCallback(new DownloadCallback()
        {
            @Override
            public void onDataChange()
            {
                //Save State
                if (articleAdapter.getItemCount() != 0)
                {
                    HomeVeriable.state = linearLayoutManager.onSaveInstanceState();

                    HomeVeriable.stateSaved = true;
                }
                //Save State/

                setArticleAdapter();
                startDownloadingAnim(false);
            }

            @Override
            public void outDataChange()
            {
                emptyAdapterControl();
                startDownloadingAnim(false);
            }
        });
        //Download Control/

        //Bottom Navigation
        mActivity.bottomNavMA.setOnNavigationItemReselectedListener(item ->
        {
            if (item.getItemId() == R.id.itmHome)
            {
                linearLayoutManager.smoothScrollToPosition(recyclerViewHF, null, 0);
                startDownloadingAnim(true);
                mActivity.downloadData();
            }
        });
        //Bottom Navigation/

        return mView;
    }

    private void startDownloadingAnim(Boolean choice)
    {
        LinearLayout llDownloadingHF = mView.findViewById(R.id.llDownloadingHF);
        LinearLayout llFirstDownloadingHF = mView.findViewById(R.id.llFirstDownloadingHF);

        if (choice)
        {
            if (DataLoader.artCount == 0)
            {
                llFirstDownloadingHF.setVisibility(View.VISIBLE);
            }
            else
            {
                llDownloadingHF.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            if (llDownloadingHF.getVisibility() == View.VISIBLE)
            {
                llDownloadingHF.setVisibility(View.GONE);
            }

            if (llFirstDownloadingHF.getVisibility() == View.VISIBLE)
            {
                llFirstDownloadingHF.setVisibility(View.GONE);
            }
        }
    }

    private void setArticleAdapter()
    {
        LogHelper.LogD("Download", "{setArticleAdapter HF} -> Running.");

        articleAdapter = new ArticleAdapter(mContext, DataLoader.articleDB.getArticleList());
        recyclerViewHF.setAdapter(articleAdapter);

        //Read scrolly
        linearLayoutManager.scrollToPosition(tinyDB.getInt("scrolly") + DataLoader.newStoryCount);
        //Read scrolly/

        //Restore State
        if (HomeVeriable.stateSaved)
        {
            linearLayoutManager.onRestoreInstanceState(HomeVeriable.state);

            HomeVeriable.stateSaved = false;
        }
        //Restore State/

        emptyAdapterControl();
    }

    private void emptyAdapterControl()
    {
        if (MainVeriable.downloadingAnimFinished)
        {
            LinearLayout llNoDataHF = mView.findViewById(R.id.llNoDataHF);
            LinearLayout llTryAgainHF = mView.findViewById(R.id.llTryAgainHF);

            if (articleAdapter.getItemCount() == 0)
            {
                llNoDataHF.setVisibility(View.VISIBLE);

                if (DataLoader.conFailCount >= 5)
                {
                    TextView tvNoDataHF = mView.findViewById(R.id.tvNoDataHF);
                    tvNoDataHF.setText(R.string.Server_Originated);
                }

                llTryAgainHF.setOnClickListener((View v) ->
                {
                    DataLoader.resetData = true;

                    llNoDataHF.setVisibility(View.GONE);
                    startDownloadingAnim(true);
                    mActivity.downloadData();
                });
            }
            else
            {
                if (llNoDataHF.getVisibility() == View.VISIBLE)
                {
                    llNoDataHF.setVisibility(View.GONE);
                }
            }
        }
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
    public void onPause()
    {
        super.onPause();

        if (articleAdapter.getItemCount() != 0)
        {
            //Write scrolly
            tinyDB.putInt("scrolly", linearLayoutManager.findFirstVisibleItemPosition());
            //Write scrolly/
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        //Save State
        if (articleAdapter.getItemCount() != 0)
        {
            HomeVeriable.state = linearLayoutManager.onSaveInstanceState();

            HomeVeriable.stateSaved = true;
        }
        //Save State/

        mActivity.bottomNavMA.setOnNavigationItemReselectedListener(null);
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