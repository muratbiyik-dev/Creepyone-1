package com.pureblacksoft.creepyone.loader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pureblacksoft.creepyone.BuildConfig;
import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.data.Article;
import com.pureblacksoft.creepyone.data.Portrait;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.helper.ArticleDB;
import com.pureblacksoft.creepyone.helper.PortraitDB;
import com.pureblacksoft.creepyone.helper.TinyDB;
import com.pureblacksoft.creepyone.service.DownloadIntentService;
import com.pureblacksoft.creepyone.util.ServerCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class DataLoader
{
    public static boolean downloading;
    public static boolean dataChanged;
    public static boolean newStories;
    public static boolean resetData;
    public static int artCount;
    public static int newStoryCount;
    public static int conFailCount;
    public static ArticleDB articleDB;
    public static PortraitDB portraitDB;
    public static JSONArray jsonArray;

    private static boolean versionChanged;

    private TinyDB tinyDB;

    public void versionControl(Context context)
    {
        if (!downloading)
        {
            LogHelper.LogD("Download", "{versionControl} -> Running.");

            int versionCode = BuildConfig.VERSION_CODE;

            //Read oldVersionCode
            tinyDB = new TinyDB(context.getApplicationContext());
            int oldVersionCode = tinyDB.getInt("oldVersionCode");
            //Read oldVersionCode/

            versionChanged = oldVersionCode != 0 && versionCode != oldVersionCode;

            //Write oldVersionCode
            tinyDB.putInt("oldVersionCode", BuildConfig.VERSION_CODE);
            //Write oldVersionCode/
        }
    }

    public void setDatabase(Context context)
    {
        //Set articleDB & portraitDB
        articleDB = new ArticleDB(context);
        articleDB.onUpgrade(articleDB.getWritableDatabase(), 1, 2);

        portraitDB = new PortraitDB(context);
        portraitDB.onUpgrade(portraitDB.getWritableDatabase(), 1, 2);
        //Set articleDB & portraitDB/

        ArrayList<Article> articleList;
        ArrayList<Portrait> portraitList;
        tinyDB = new TinyDB(context.getApplicationContext());
        if (versionChanged)
        {
            LogHelper.LogD("Download", "(versionChanged) -> Delete old articles.");

            articleList = new ArrayList<>();
            portraitList = new ArrayList<>();

            //Write artCount & articleList & portraitList
            tinyDB.putInt("artCountEN", 0);
            tinyDB.putListArticle("articleListEN", articleList);
            tinyDB.putListPortrait("portraitListEN", portraitList);
            tinyDB.putInt("artCountTR", 0);
            tinyDB.putListArticle("articleListTR", articleList);
            tinyDB.putListPortrait("portraitListTR", portraitList);
            //Write artCount & articleList & portraitList/

            versionChanged = false;
        }
        else
        {
            //Read artCount & articleList & portraitList
            if (PrefLoader.appLocaleCode == 2)
            {
                artCount = tinyDB.getInt("artCountTR");
                articleList = tinyDB.getListArticle("articleListTR");
                portraitList = tinyDB.getListPortrait("portraitListTR");
            }
            else
            {
                artCount = tinyDB.getInt("artCountEN");
                articleList = tinyDB.getListArticle("articleListEN");
                portraitList = tinyDB.getListPortrait("portraitListEN");
            }
            //Read artCount & articleList & portraitList/

            articleDB.addArticleList(articleList);
            portraitDB.addPortraitList(portraitList);
        }
    }

    public void downloadDataJSON(Context context, final ServerCallback callback)
    {
        if (!downloading)
        {
            LogHelper.LogD("Download", "{downloadDataJSON} -> Running.");

            downloading = true;

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, context.getString(R.string.DATA_URL), null,
                    response ->
                    {
                        try {
                            jsonArray = response.getJSONArray("results");

                            int jaLength = jsonArray.length();
                            if (jaLength > artCount)
                            {
                                LogHelper.LogD("Download", "(jaLength > artCount) -> Update data.");

                                dataChanged = true;
                                if (artCount != 0)
                                {
                                    newStories = true;
                                    newStoryCount = jaLength - artCount;
                                }
                                else
                                {
                                    newStories = false;
                                    newStoryCount = 0;
                                }

                                startDownload(context, artCount, jaLength);
                            }
                            else if (jaLength == artCount)
                            {
                                LogHelper.LogD("Download", "(jaLength == artCount) -> Keep data.");

                                dataChanged = false;
                                newStories = false;
                                newStoryCount = 0;
                            }
                            else
                            {
                                LogHelper.LogD("Download", "(jaLength < artCount) -> Reset data.");

                                dataChanged = true;
                                resetData = true;
                                newStories = false;
                                newStoryCount = jaLength - artCount;

                                startDownload(context, 0, jaLength);
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        callback.onSuccess(response);
                    },
                    error ->
                    {
                        Log.e("Download", "(Connection Failed) -> " + error.toString());

                        conFailCount++;

                        callback.onFailure(error);
                    });
            requestQueue.add(jor);
        }
    }

    private void startDownload(Context context, int artCount, int jaLength)
    {
        LogHelper.LogD("Download", "{startDownload} -> Running.");

        Intent downloadIntent = new Intent(context, DownloadIntentService.class);
        downloadIntent.putExtra("artCountStr", Integer.toString(artCount));
        downloadIntent.putExtra("jaLengthStr", Integer.toString(jaLength));
        context.startService(downloadIntent);
    }
}

//PureBlack Software / Murat BIYIK
