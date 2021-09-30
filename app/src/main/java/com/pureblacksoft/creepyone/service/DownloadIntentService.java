package com.pureblacksoft.creepyone.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pureblacksoft.creepyone.R;
import com.pureblacksoft.creepyone.data.Article;
import com.pureblacksoft.creepyone.helper.LogHelper;
import com.pureblacksoft.creepyone.loader.DataLoader;
import com.pureblacksoft.creepyone.helper.TinyDB;
import com.pureblacksoft.creepyone.loader.PrefLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadIntentService extends IntentService
{
    public static final String BROADCAST_ACTION = "com.pureblacksoft.creepyone.BROADCAST";
    public static final String EXTENDED_DATA_STATUS = "com.pureblacksoft.creepyone.STATUS";

    private int artCount;
    private int jaLength;

    private ArrayList<Integer> artIdList;
    private ArrayList<String> artTitleList;
    private ArrayList<String> artContentList;
    private ArrayList<String> artAuthorList;
    private ArrayList<byte[]> artImageList;
    private ArrayList<Integer> artPopularityList;
    private ArrayList<Float> artReadingTimeList;
    private ArrayList<String> artReadingTimeStrList;

    private ArrayList<Integer> portIdList;
    private ArrayList<String> portTitleList;
    private ArrayList<byte[]> portImageList;
    private ArrayList<Integer> portIndexList;

    public DownloadIntentService()
    {
        super("DownloadIntentService");

        LogHelper.LogD("Download", "{DownloadIntentService} -> Created.");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String artCountStr = intent.getStringExtra("artCountStr");
        String jaLengthStr = intent.getStringExtra("jaLengthStr");
        assert artCountStr != null;
        artCount = Integer.parseInt(artCountStr);
        assert jaLengthStr != null;
        jaLength = Integer.parseInt(jaLengthStr);

        getData();

        serviceFinished();
    }

    private void getData()
    {
        int appLocaleCode = PrefLoader.appLocaleCode;

        artIdList = new ArrayList<>();
        artTitleList = new ArrayList<>();
        artContentList = new ArrayList<>();
        artAuthorList = new ArrayList<>();
        artImageList = new ArrayList<>();
        artPopularityList = new ArrayList<>();
        artReadingTimeList = new ArrayList<>();
        artReadingTimeStrList = new ArrayList<>();

        portIdList = new ArrayList<>();
        portTitleList = new ArrayList<>();
        portImageList = new ArrayList<>();
        portIndexList = new ArrayList<>();

        int id;
        String title;
        String content;
        String author;
        int popularity;
        float readingTime;
        String readingTimeStr;
        int imageExist;
        URL imageURL;
        HttpURLConnection connection;
        InputStream inputStream;
        Bitmap image;
        ByteArrayOutputStream outputStream;
        byte[] imageBytes;

        try
        {
            if (appLocaleCode == 2) //Turkish
            {
                for (int i = artCount; i < jaLength; i++)
                {
                    JSONObject jsonObject = DataLoader.jsonArray.getJSONObject(i);

                    id = Integer.parseInt(jsonObject.getString("id"));
                    artIdList.add(id);
                    title = jsonObject.getString("title_tr");
                    artTitleList.add(title);
                    content = jsonObject.getString("content_tr");
                    artContentList.add(content);
                    author = jsonObject.getString("author_tr");
                    artAuthorList.add(author);
                    popularity = Integer.parseInt(jsonObject.getString("popularity"));
                    artPopularityList.add(popularity);
                    readingTime = Article.calcReadingTime(content);
                    artReadingTimeList.add(readingTime);
                    readingTimeStr = Article.findReadingTimeStr(this, readingTime, appLocaleCode);
                    artReadingTimeStrList.add(readingTimeStr);

                    //Image
                    imageExist = Integer.parseInt(jsonObject.getString("image_exist"));
                    if (imageExist == 1)
                    {
                        try
                        {
                            imageURL = new URL(this.getString(R.string.IMAGE_URL) + id);
                            connection = (HttpURLConnection) imageURL.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            inputStream = connection.getInputStream();
                            image = BitmapFactory.decodeStream(inputStream);

                            //Convert Bitmap to byte[]
                            outputStream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            imageBytes = outputStream.toByteArray();
                            image.recycle();
                            //Convert Bitmap to byte[]/

                            artImageList.add(imageBytes);

                            portIdList.add(id);
                            portTitleList.add(title);
                            portImageList.add(imageBytes);
                            portIndexList.add(i);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        artImageList.add(null);
                    }
                    //Image/
                }
            }
            else //English
            {
                for (int i = artCount; i < jaLength; i++)
                {
                    JSONObject jsonObject = DataLoader.jsonArray.getJSONObject(i);

                    id = Integer.parseInt(jsonObject.getString("id"));
                    artIdList.add(id);
                    title = jsonObject.getString("title_en");
                    artTitleList.add(title);
                    content = jsonObject.getString("content_en");
                    artContentList.add(content);
                    author = jsonObject.getString("author_en");
                    artAuthorList.add(author);
                    popularity = Integer.parseInt(jsonObject.getString("popularity"));
                    artPopularityList.add(popularity);
                    readingTime = Article.calcReadingTime(content);
                    artReadingTimeList.add(readingTime);
                    readingTimeStr = Article.findReadingTimeStr(this, readingTime, appLocaleCode);
                    artReadingTimeStrList.add(readingTimeStr);

                    //Image
                    imageExist = Integer.parseInt(jsonObject.getString("image_exist"));
                    if (imageExist == 1)
                    {
                        try
                        {
                            imageURL = new URL(this.getString(R.string.IMAGE_URL) + id);
                            connection = (HttpURLConnection) imageURL.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            inputStream = connection.getInputStream();
                            image = BitmapFactory.decodeStream(inputStream);

                            //Convert Bitmap to byte[]
                            outputStream = new ByteArrayOutputStream();
                            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            imageBytes = outputStream.toByteArray();
                            image.recycle();
                            //Convert Bitmap to byte[]/

                            artImageList.add(imageBytes);

                            portIdList.add(id);
                            portTitleList.add(title);
                            portImageList.add(imageBytes);
                            portIndexList.add(i);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        artImageList.add(null);
                    }
                    //Image/
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        addData(appLocaleCode);

        if (appLocaleCode != PrefLoader.appLocaleCode)
        {
            DataLoader.resetData = true;

            getData();
        }
    }

    private void addData(int appLocaleCode)
    {
        int passiveArtCount = 0;

        int artIdListSize = artIdList.size();
        if (artIdListSize > 0 && artIdListSize == artImageList.size())
        {
            LogHelper.LogD("Download", "{Add Data to Database} -> Succeed.");

            if (DataLoader.resetData)
            {
                LogHelper.LogD("Download", "(DataLoader.resetData) -> Delete database.");

                DataLoader.articleDB.deleteTable();
                DataLoader.portraitDB.deleteTable();

                DataLoader.resetData = false;
            }

            //articleDB
            for (int i = 0; i < artIdListSize; i++)
            {
                if (artPopularityList.get(i) >= 0) //Passive Article Control
                {
                    LogHelper.LogD("Download", "[Article " + artIdList.get(i).toString() + "] -> Added to database");

                    DataLoader.articleDB.addArticle(artIdList.get(i), artTitleList.get(i), artContentList.get(i), artAuthorList.get(i), artImageList.get(i), artPopularityList.get(i), artReadingTimeList.get(i), artReadingTimeStrList.get(i));
                }
                else
                {
                    LogHelper.LogD("Download", "[Passive Article " + artIdList.get(i).toString() + "] -> Added to database");

                    passiveArtCount++;
                }
            }
            DataLoader.articleDB.close();
            //articleDB/

            int portIdListSize = portIdList.size();
            if (portIdListSize > 0)
            {
                //portraitDB
                for (int i = 0; i < portIdListSize; i++)
                {
                    LogHelper.LogD("Download", "[Portrait " + portIdList.get(i).toString() + "] -> Added to database");

                    DataLoader.portraitDB.addPortrait(portIdList.get(i), portTitleList.get(i), portImageList.get(i), portIndexList.get(i));
                }
                DataLoader.portraitDB.close();
                //portraitDB/
            }

            //New Story Control
            if (DataLoader.newStoryCount == passiveArtCount)
            {
                DataLoader.newStories = false;
                DataLoader.newStoryCount = 0;
            }
            //New Story Control/

            saveData(appLocaleCode);
        }
        else
        {
            LogHelper.LogD("Download", "{Add Data to Database} -> Failed.");

            serviceFinished();
        }
    }

    private void saveData(int appLocaleCode)
    {
        LogHelper.LogD("Download", "{saveData} -> Running.");

        TinyDB tinyDB = new TinyDB(getApplicationContext());

        DataLoader.artCount = jaLength;

        if (appLocaleCode == 2)
        {
            tinyDB.putInt("artCountTR", jaLength);
            tinyDB.putListArticle("articleListTR", DataLoader.articleDB.getArticleList());
            tinyDB.putListPortrait("portraitListTR", DataLoader.portraitDB.getPortraitList());
        }
        else
        {
            tinyDB.putInt("artCountEN", jaLength);
            tinyDB.putListArticle("articleListEN", DataLoader.articleDB.getArticleList());
            tinyDB.putListPortrait("portraitListEN", DataLoader.portraitDB.getPortraitList());
        }
    }

    private void serviceFinished()
    {
        LogHelper.LogD("Download", "{serviceFinished} -> Running.");

        String status = "Service Finished";
        Intent localIntent = new Intent(BROADCAST_ACTION).putExtra(EXTENDED_DATA_STATUS, status);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        stopSelf();
    }
}

//PureBlack Software / Murat BIYIK
