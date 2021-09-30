package com.pureblacksoft.creepyone.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.activity.MainActivity;
import com.pureblacksoft.creepyone.adapter.PortraitAdapter;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.util.DownloadCallback;
import com.pureblacksoft.creepyone.veriable.GalleryVeriable;

import java.util.Objects;

public class GalleryFragment extends Fragment
{
    private Context mContext;
    private MainActivity mActivity;
    private View mView;
    private RecyclerView recyclerViewGF;
    private LinearLayoutManager linearLayoutManager;
    private PortraitAdapter portraitAdapter;

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
        mView = inflater.inflate(R.layout.fragment_gallery, container, false);

        //RecyclerViewGF
        recyclerViewGF = mView.findViewById(R.id.recyclerViewGF);
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewGF.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext, linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(mContext.getDrawable(R.drawable.shape_divider_story)));
        recyclerViewGF.addItemDecoration(dividerItemDecoration);
        //RecyclerViewGF/

        //Get Portraits
        setPortraitAdapter();
        //Get Portraits/

        //Download Control
        mActivity.setDownloadCallback(new DownloadCallback()
        {
            @Override
            public void onDataChange()
            {
                //Save State
                if (portraitAdapter.getItemCount() != 0)
                {
                    GalleryVeriable.state = linearLayoutManager.onSaveInstanceState();

                    GalleryVeriable.stateSaved = true;
                }
                //Save State/

                setPortraitAdapter();
            }

            @Override
            public void outDataChange() {}
        });
        //Download Control/

        //Bottom Navigation
        mActivity.bottomNavMA.setOnNavigationItemReselectedListener(item ->
        {
            if (item.getItemId() == R.id.itmGallery)
            {
                linearLayoutManager.smoothScrollToPosition(recyclerViewGF, null, 0);
            }
        });
        //Bottom Navigation/

        return mView;
    }

    private void setPortraitAdapter()
    {
        LogHelper.LogD("Download", "{setPortraitAdapter GF} -> Running.");

        portraitAdapter = new PortraitAdapter(mContext, DataLoader.portraitDB.getPortraitList());
        recyclerViewGF.setAdapter(portraitAdapter);

        //Restore State
        if (GalleryVeriable.stateSaved)
        {
            linearLayoutManager.onRestoreInstanceState(GalleryVeriable.state);

            GalleryVeriable.stateSaved = false;
        }
        //Restore State/

        emptyAdapterControl();
    }

    private void emptyAdapterControl()
    {
        LinearLayout llNoDataGF = mView.findViewById(R.id.llNoDataGF);
        if (portraitAdapter.getItemCount() == 0)
        {
            llNoDataGF.setVisibility(View.VISIBLE);
        }
        else
        {
            if (llNoDataGF.getVisibility() == View.VISIBLE)
            {
                llNoDataGF.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        //Update Adapter
        if (portraitAdapter != null)
        {
            portraitAdapter.notifyDataSetChanged();
        }
        //Update Adapter/
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        //Save State
        if (portraitAdapter.getItemCount() != 0)
        {
            GalleryVeriable.state = linearLayoutManager.onSaveInstanceState();

            GalleryVeriable.stateSaved = true;
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
