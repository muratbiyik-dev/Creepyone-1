package com.pureblacksoft.creepyone.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.activity.ListActivity;

public class ArchiveFragment extends Fragment
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
        View view = inflater.inflate(R.layout.fragment_archive, container, false);

        //Row Connections
        LinearLayout llShortRowAF = view.findViewById(R.id.llShortRowAF);
        llShortRowAF.setOnClickListener((View v) ->
        {
            ListActivity.selectedList = 3;
            Intent intent = new Intent(mContext, ListActivity.class);
            startActivity(intent);
        });

        LinearLayout llMediumRowAF = view.findViewById(R.id.llMediumRowAF);
        llMediumRowAF.setOnClickListener((View v) ->
        {
            ListActivity.selectedList = 4;
            Intent intent = new Intent(mContext, ListActivity.class);
            startActivity(intent);
        });

        LinearLayout llLongRowAF = view.findViewById(R.id.llLongRowAF);
        llLongRowAF.setOnClickListener((View v) ->
        {
            ListActivity.selectedList = 5;
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
