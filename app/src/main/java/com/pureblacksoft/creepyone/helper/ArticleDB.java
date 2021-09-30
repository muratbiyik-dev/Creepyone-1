package com.pureblacksoft.creepyone.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pureblacksoft.creepyone.activity.ArticleActivity;
import com.pureblacksoft.creepyone.data.Article;

import java.util.ArrayList;

public class ArticleDB extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ArticleDB";
    private static final String ARTICLE_TABLE = "Articles";
    private static final String ARTICLE_ID = "article_id";
    private static final String ARTICLE_TITLE = "article_title";
    private static final String ARTICLE_CONTENT = "article_content";
    private static final String ARTICLE_AUTHOR = "article_author";
    private static final String ARTICLE_IMAGE = "article_image";
    private static final String ARTICLE_POPULARITY = "article_popularity";
    private static final String ARTICLE_READING_TIME = "article_reading_time";
    private static final String ARTICLE_READING_TIME_STRING = "article_reading_time_string";
    private static final String ARTICLE_TABLE_SQL = "CREATE TABLE "
            + ARTICLE_TABLE + " ("
            + ARTICLE_ID + " INTEGER PRIMARY KEY UNIQUE, "
            + ARTICLE_TITLE + " TEXT, "
            + ARTICLE_CONTENT + " TEXT, "
            + ARTICLE_AUTHOR + " TEXT, "
            + ARTICLE_IMAGE + " BLOB, "
            + ARTICLE_POPULARITY + " INTEGER, "
            + ARTICLE_READING_TIME + " REAL, "
            + ARTICLE_READING_TIME_STRING + " TEXT )";

    public ArticleDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ARTICLE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ARTICLE_TABLE);
        this.onCreate(db);
    }

    public void addArticle(Integer id, String title, String content, String author, byte[] image, Integer popularity, Float readingTime, String readingTimeStr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ARTICLE_ID, id);
        values.put(ARTICLE_TITLE, title);
        values.put(ARTICLE_CONTENT, content);
        values.put(ARTICLE_AUTHOR, author);
        values.put(ARTICLE_IMAGE, image);
        values.put(ARTICLE_POPULARITY, popularity);
        values.put(ARTICLE_READING_TIME, readingTime);
        values.put(ARTICLE_READING_TIME_STRING, readingTimeStr);
        db.insert(ARTICLE_TABLE, null, values);
    }

    public void addArticleList(ArrayList<Article> articleList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int articleListSize = articleList.size();
        for (int i = 0; i < articleListSize; i++)
        {
            addArticle(articleList.get(i).getId(), articleList.get(i).getTitle(), articleList.get(i).getContent(), articleList.get(i).getAuthor(), articleList.get(i).getImage(), articleList.get(i).getPopularity(), articleList.get(i).getReadingTime(), articleList.get(i).getReadingTimeStr());
        }
    }

    public ArrayList<Article> getArticleList()
    {
        String query = "SELECT * FROM " + ARTICLE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Article> articleList = new ArrayList<>();
        Article article;
        if (cursor.moveToLast())
        {
            do {
                article = new Article();
                article.setId(cursor.getInt(0));
                article.setTitle(cursor.getString(1));
                article.setContent(cursor.getString(2));
                article.setAuthor(cursor.getString(3));
                article.setImage(cursor.getBlob(4));
                article.setPopularity(cursor.getInt(5));
                article.setReadingTime(cursor.getFloat(6));
                article.setReadingTimeStr(cursor.getString(7));
                articleList.add(article);
            }
            while (cursor.moveToPrevious());
        }
        cursor.close();

        return articleList;
    }

    public ArrayList<Article> getPopArticleList()
    {
        String query = "SELECT * FROM " + ARTICLE_TABLE + " WHERE " + ARTICLE_POPULARITY + " > 0 ORDER BY " + ARTICLE_POPULARITY + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Article> popArticleList = new ArrayList<>();
        cursorSetterFirst(cursor, popArticleList);

        return popArticleList;
    }

    public ArrayList<Article> getFavArticleList(Context context)
    {
        //Read favoriteIdList
        TinyDB tinyDB = new TinyDB(context);
        ArticleActivity.favoriteIdList = tinyDB.getListInt("favoriteIdList");
        //Read favoriteIdList

        int favIdListSize = ArticleActivity.favoriteIdList.size();
        StringBuilder favIdListQuery = new StringBuilder();
        for (int i = 0; i < favIdListSize; i++)
        {
            favIdListQuery.append(ArticleActivity.favoriteIdList.get(i).toString());

            if (i != favIdListSize - 1)
            {
                favIdListQuery.append(", ");
            }
        }

        String query = "SELECT * FROM " + ARTICLE_TABLE + " WHERE " + ARTICLE_ID + " IN (" + favIdListQuery + ") ORDER BY " + ARTICLE_TITLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Article> favArticleList = new ArrayList<>();
        cursorSetterFirst(cursor, favArticleList);

        return favArticleList;
    }

    public ArrayList<Article> getShortArticleList()
    {
        String query = "SELECT * FROM " + ARTICLE_TABLE + " WHERE " + ARTICLE_READING_TIME + " < 2 ORDER BY " + ARTICLE_READING_TIME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Article> shortArticleList = new ArrayList<>();
        cursorSetterFirst(cursor, shortArticleList);

        return shortArticleList;
    }

    public ArrayList<Article> getMediumArticleList()
    {
        String query = "SELECT * FROM " + ARTICLE_TABLE + " WHERE " + ARTICLE_READING_TIME + " BETWEEN 2 AND 10 ORDER BY " + ARTICLE_READING_TIME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Article> mediumArticleList = new ArrayList<>();
        cursorSetterFirst(cursor, mediumArticleList);

        return mediumArticleList;
    }

    public ArrayList<Article> getLongArticleList()
    {
        String query = "SELECT * FROM " + ARTICLE_TABLE + " WHERE " + ARTICLE_READING_TIME + " > 10 ORDER BY " + ARTICLE_READING_TIME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Article> longArticleList = new ArrayList<>();
        cursorSetterFirst(cursor, longArticleList);

        return longArticleList;
    }

    private void cursorSetterFirst(Cursor cursor, ArrayList<Article> articleList)
    {
        Article article;

        if (cursor.moveToFirst())
        {
            do {
                article = new Article();
                article.setId(cursor.getInt(0));
                article.setTitle(cursor.getString(1));
                article.setContent(cursor.getString(2));
                article.setAuthor(cursor.getString(3));
                article.setImage(cursor.getBlob(4));
                article.setPopularity(cursor.getInt(5));
                article.setReadingTime(cursor.getFloat(6));
                article.setReadingTimeStr(cursor.getString(7));
                articleList.add(article);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    public void deleteTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(ARTICLE_TABLE, null, null);
    }
}

//PureBlack Software / Murat BIYIK