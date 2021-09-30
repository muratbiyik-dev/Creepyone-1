package com.pureblacksoft.creepyone.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.adapter.ArticleAdapter;
import com.pureblacksoft.creepyone.helper.TinyDB;
import com.pureblacksoft.creepyone.loader.PrefLoader;

import java.util.ArrayList;

public class ArticleActivity extends AppCompatActivity
{
    public static ArrayList<Integer> favoriteIdList = new ArrayList<>();
    public static ArrayList<Integer> readedIdList = new ArrayList<>();

    private boolean favorite;
    private boolean readed;
    private Integer selectedArticleId;
    private CheckBox cbFavoriteAA;
    private CheckBox cbReadedAA;
    private TinyDB tinyDB;

    private Toast favoriteToast;
    private Toast readedToast;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Preference Control
        PrefLoader prefLoader = new PrefLoader();
        prefLoader.prefControl(this);
        //Preference Control/

        setContentView(R.layout.activity_article);

        //Toasts
        favoriteToast = Toast.makeText(this, R.string.Favorite_Toast, Toast.LENGTH_SHORT);
        favoriteToast.setGravity(Gravity.BOTTOM, 0, 160);
        readedToast = Toast.makeText(this, R.string.Readed_Toast, Toast.LENGTH_SHORT);
        readedToast.setGravity(Gravity.BOTTOM, 0, 160);
        //Toasts/

        tinyDB = new TinyDB(getApplicationContext());

        //Toolbar
        Toolbar toolbarAA = findViewById(R.id.toolbarAA);
        setSupportActionBar(toolbarAA);

        //Back Button
        ImageButton ibBackAP = findViewById(R.id.ibBackAA);
        ibBackAP.setOnClickListener((View v) -> finish());
        //Back Button/
        //Toolbar/

        //Get Data
        selectedArticleId = ArticleAdapter.selectedArticle.getId();

        TextView tvTitleAA = findViewById(R.id.titleAA);
        TextView tvContentAA = findViewById(R.id.contentAA);
        TextView tvAuthorAA = findViewById(R.id.authorAA);

        tvTitleAA.setText(ArticleAdapter.selectedArticle.getTitle());
        tvContentAA.setText(ArticleAdapter.selectedArticle.getContent());
        tvAuthorAA.setText(ArticleAdapter.selectedArticle.getAuthor());

        //Get Image
        ImageView ivImageAA = findViewById(R.id.imageAA);

        byte[] imageBytes = ArticleAdapter.selectedArticle.getImage();
        if (imageBytes != null)
        {
            Bitmap image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            ivImageAA.setImageBitmap(Bitmap.createScaledBitmap(image, image.getWidth(), image.getHeight(), false));
            ivImageAA.setVisibility(View.VISIBLE);
        }
        else
        {
            //No Image
            ivImageAA.setVisibility(View.GONE);
            //No Image/
        }
        //Get Image/
        //Get Data/

        //CheckBoxes
        //Read favoriteIdList & readedIdList
        favoriteIdList = tinyDB.getListInt("favoriteIdList");
        readedIdList = tinyDB.getListInt("readedIdList");
        //Read favoriteIdList & readedIdList/

        //Favorite CheckBox
        cbFavoriteAA = findViewById(R.id.cbFavoriteAA);

        if (favoriteIdList.contains(selectedArticleId))
        {
            favorite = true;

            cbFavoriteAA.setChecked(true);
        }
        else
        {
            favorite = false;

            cbFavoriteAA.setChecked(false);
        }

        cbFavoriteAA.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
        {
            if (cbFavoriteAA.isChecked())
            {
                favorite = true;

                favoriteToast.show();
            }
            else
            {
                favorite = false;
            }
        });
        //Favorite CheckBox/

        //Readed CheckBox
        cbReadedAA = findViewById(R.id.cbReadedAA);

        if (readedIdList.contains(selectedArticleId))
        {
            readed = true;

            cbReadedAA.setChecked(true);
            tvTitleAA.setTextColor(getColor(R.color.bright_red_light));
        }
        else
        {
            readed = false;

            cbReadedAA.setChecked(false);
            tvTitleAA.setTextColor(getColor(R.color.bright_red));
        }

        cbReadedAA.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) ->
        {
            if (cbReadedAA.isChecked())
            {
                readed = true;

                readedToast.show();
            }
            else
            {
                readed = false;
            }
        });
        //Readed CheckBox/
        //CheckBoxes/
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        //Favorite CheckBox Control
        if (favorite && !favoriteIdList.contains(selectedArticleId))
        {
            favoriteIdList.add(selectedArticleId);
        }
        else if (!favorite)
        {
            favoriteIdList.remove(selectedArticleId);
        }
        //Favorite CheckBox Control/

        //Readed CheckBox Control
        if (readed && !readedIdList.contains(selectedArticleId))
        {
            readedIdList.add(selectedArticleId);
        }
        else if (!readed)
        {
            readedIdList.remove(selectedArticleId);
        }
        //Readed CheckBox Control/

        //Write favoriteIdList & readedIdList
        tinyDB.putListInt("favoriteIdList", favoriteIdList);
        tinyDB.putListInt("readedIdList", readedIdList);
        //Write favoriteIdList & readedIdList/
    }
}

//PureBlack Software / Murat BIYIK