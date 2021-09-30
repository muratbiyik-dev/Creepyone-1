package com.pureblacksoft.creepyone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.fragment.HomeFragment;
import com.pureblacksoft.creepyone.helper.TinyDB;
import com.pureblacksoft.creepyone.loader.PrefLoader;
import com.pureblacksoft.creepyone.veriable.MainVeriable;

import java.util.ArrayList;
import java.util.List;

public class PrefActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{
    private boolean firstStartS1;
    private boolean firstStartS2;
    private Spinner sAppLocalePA;
    private Spinner sFontScalePA;
    private PrefLoader prefLoader;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Preference Control
        prefLoader = new PrefLoader();
        prefLoader.prefControl(this);
        //Preference Control/

        setContentView(R.layout.activity_pref);

        tinyDB = new TinyDB(getApplicationContext());

        //Write oldAppLocaleCodePA & oldFontScaleCodePA
        tinyDB.putInt("oldAppLocaleCodePA", PrefLoader.appLocaleCode);
        tinyDB.putInt("oldFontScaleCodePA", PrefLoader.fontScaleCode);
        //Write oldAppLocaleCodePA & oldFontScaleCodePA/

        //Toolbar
        Toolbar toolbarPA = findViewById(R.id.toolbarPA);
        setSupportActionBar(toolbarPA);

        //Back Button
        ImageButton ibBackPA = findViewById(R.id.ibBackPA);
        ibBackPA.setOnClickListener((View v) -> startMainActivity());
        //Back Button/
        //Toolbar/

        //Spinner
        sAppLocalePA = findViewById(R.id.sAppLocalePA);
        firstStartS1 = true;
        sAppLocalePA.setOnItemSelectedListener(this);

        sFontScalePA = findViewById(R.id.sFontScalePA);
        firstStartS2 = true;
        sFontScalePA.setOnItemSelectedListener(this);

        //Spinner Elements
        List<String> appLocaleList = new ArrayList<>();
        appLocaleList.add(getString(R.string.App_Prefs_Language_EN));
        appLocaleList.add(getString(R.string.App_Prefs_Language_TR));

        List<String> fontScaleList = new ArrayList<>();
        fontScaleList.add(getString(R.string.App_Prefs_Font_Size_S));
        fontScaleList.add(getString(R.string.App_Prefs_Font_Size_M));
        fontScaleList.add(getString(R.string.App_Prefs_Font_Size_L));
        fontScaleList.add(getString(R.string.App_Prefs_Font_Size_XL));
        //Spinner Elements/

        //Spinner Adapter
        ArrayAdapter<String> appLocaleAdapter = new ArrayAdapter<>(this, R.layout.text_spinner, appLocaleList);
        sAppLocalePA.setAdapter(appLocaleAdapter);

        ArrayAdapter<String> fontScaleAdapter = new ArrayAdapter<>(this, R.layout.text_spinner, fontScaleList);
        sFontScalePA.setAdapter(fontScaleAdapter);
        //Spinner Adapter/

        //Spinner Selection
        //App Locale
        if (PrefLoader.appLocaleCode == 1)
        {
            sAppLocalePA.setSelection(0);
        }
        else if (PrefLoader.appLocaleCode == 2)
        {
            sAppLocalePA.setSelection(1);
        }
        //App Locale/

        //Font Scale
        switch (PrefLoader.fontScaleCode)
        {
            case 1:
                sFontScalePA.setSelection(0);
                break;

            case 2:
                sFontScalePA.setSelection(1);
                break;

            case 3:
                sFontScalePA.setSelection(2);
                break;

            case 4:
                sFontScalePA.setSelection(3);
                break;
        }
        //Font Scale/
        //Spinner Selection/
        //Spinner/

        //Row Connections
        LinearLayout llTermsRowPA = findViewById(R.id.llTermsRowPA);
        llTermsRowPA.setOnClickListener((View v) ->
        {
            InfoActivity.selectedInfo = 1;
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        });

        LinearLayout llAboutRowPA = findViewById(R.id.llAboutRowPA);
        llAboutRowPA.setOnClickListener((View v) ->
        {
            InfoActivity.selectedInfo = 2;
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        });
        //Row Connections/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getId() == sAppLocalePA.getId())
        {
            if (firstStartS1)
            {
                firstStartS1 = false;
            }
            else
            {
                if (sAppLocalePA.getSelectedItemId() == sAppLocalePA.getItemIdAtPosition(0))
                {
                    PrefLoader.appLocaleCode = 1;
                }
                else if (sAppLocalePA.getSelectedItemId() == sAppLocalePA.getItemIdAtPosition(1))
                {
                    PrefLoader.appLocaleCode = 2;
                }

                //Write appLocaleCode
                tinyDB.putInt("appLocaleCode", PrefLoader.appLocaleCode);
                //Write appLocaleCode/

                restartPrefActivity();
            }
        }

        if (parent.getId() == sFontScalePA.getId())
        {
            if (firstStartS2)
            {
                firstStartS2 = false;
            }
            else
            {
                if (sFontScalePA.getSelectedItemId() == sFontScalePA.getItemIdAtPosition(0))
                {
                    PrefLoader.fontScaleCode = 1;
                }
                else if (sFontScalePA.getSelectedItemId() == sFontScalePA.getItemIdAtPosition(1))
                {
                    PrefLoader.fontScaleCode = 2;
                }
                else if (sFontScalePA.getSelectedItemId() == sFontScalePA.getItemIdAtPosition(2))
                {
                    PrefLoader.fontScaleCode = 3;
                }
                else if (sFontScalePA.getSelectedItemId() == sFontScalePA.getItemIdAtPosition(3))
                {
                    PrefLoader.fontScaleCode = 4;
                }

                //Write fontScaleCode
                tinyDB.putInt("fontScaleCode", PrefLoader.fontScaleCode);
                //Write fontScaleCode/

                restartPrefActivity();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onBackPressed()
    {
        startMainActivity();
    }

    private void restartPrefActivity()
    {
        //Read oldAppLocaleCodePA & oldFontScaleCodePA
        int oldAppLocaleCodePA = tinyDB.getInt("oldAppLocaleCodePA");
        int oldFontScaleCodePA = tinyDB.getInt("oldFontScaleCodePA");
        //Read oldAppLocaleCodePA & oldFontScaleCodePA/

        prefLoader.prefChangeControl(this, oldAppLocaleCodePA, oldFontScaleCodePA);

        if (PrefLoader.prefChanged)
        {
            recreate();
        }
    }

    private void startMainActivity()
    {
        //Read oldAppLocaleCodeMA & oldFontScaleCodeMA
        int oldAppLocaleCodeMA = tinyDB.getInt("oldAppLocaleCodeMA");
        int oldFontScaleCodeMA = tinyDB.getInt("oldFontScaleCodeMA");
        //Read oldAppLocaleCodeMA & oldFontScaleCodeMA/

        prefLoader.prefChangeControl(this, oldAppLocaleCodeMA, oldFontScaleCodeMA);

        if (PrefLoader.prefChanged)
        {
            if (PrefLoader.prefChangedAL)
            {
                MainVeriable.downloadingAnimFinished = true;

                //Read artCountEN & artCountTR
                int artCountEN = tinyDB.getInt("artCountEN");
                int artCountTR = tinyDB.getInt("artCountTR");
                //Read artCountEN & artCountTR/

                if (artCountEN != 0 && artCountTR != 0)
                {
                    MainVeriable.localDatasDifferent = artCountEN != artCountTR;
                }
            }

            MainVeriable.selectedFragment = new HomeFragment();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }
        else
        {
            finish();
        }
    }
}

//PureBlack Software / Murat BIYIK