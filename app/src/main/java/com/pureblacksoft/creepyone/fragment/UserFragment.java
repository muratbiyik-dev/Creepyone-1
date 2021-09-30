package com.pureblacksoft.creepyone.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.activity.ListActivity;
import com.pureblacksoft.creepyone.activity.PrefActivity;

public class UserFragment extends Fragment
{
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        mContext = requireContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        //Button Connections
        ImageButton ibPreferencesUF = view.findViewById(R.id.ibPreferencesUF);
        ibPreferencesUF.setOnClickListener((View v) ->
        {
            Intent intent = new Intent(mContext, PrefActivity.class);
            startActivity(intent);
        });
        //Button Connections/

        //Row Connections
        LinearLayout llFavoriteRowUF = view.findViewById(R.id.llFavoriteRowUF);
        llFavoriteRowUF.setOnClickListener((View v) ->
        {
            ListActivity.selectedList = 2;
            Intent intent = new Intent(mContext, ListActivity.class);
            startActivity(intent);
        });
        //Row Connections/

        return view;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        mContext = null;
    }
}
