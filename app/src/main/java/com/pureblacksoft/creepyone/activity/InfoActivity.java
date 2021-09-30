package com.pureblacksoft.creepyone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.loader.PrefLoader;

public class InfoActivity extends AppCompatActivity
{
    public static int selectedInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Preference Control
        PrefLoader prefLoader = new PrefLoader();
        prefLoader.prefControl(this);
        //Preference Control/

        setContentView(R.layout.activity_info);

        //Toolbar
        Toolbar toolbarIA = findViewById(R.id.toolbarIA);
        setSupportActionBar(toolbarIA);

        //Back Button
        ImageButton ibBackIA = findViewById(R.id.ibBackIA);
        ibBackIA.setOnClickListener((View v) -> finish());
        //Back Button/
        //Toolbar/

        //Title & Data
        TextView tvToolbarTitleIA = findViewById(R.id.toolbarTitleIA);
        TextView tvInfoIA = findViewById(R.id.infoIA);
        switch (selectedInfo)
        {
            case 1:
                tvToolbarTitleIA.setText(R.string.Terms_Title);
                tvInfoIA.setText(R.string.Terms_Info);
                break;

            case 2:
                tvToolbarTitleIA.setText(R.string.About_Title);
                tvInfoIA.setText(R.string.About_Info);
                tvInfoIA.setMovementMethod(LinkMovementMethod.getInstance());
                break;
        }
        //Title & Data/
    }
}

//PureBlack Software / Murat BIYIK