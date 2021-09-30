package com.pureblacksoft.creepyone.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pureblacksoft.creepyone.data.Portrait;

import java.util.ArrayList;

public class PortraitDB extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PortraitDB";
    private static final String PORTRAIT_TABLE = "Portraits";
    private static final String PORTRAIT_ID = "portrait_id";
    private static final String PORTRAIT_TITLE = "portrait_title";
    private static final String PORTRAIT_IMAGE = "portrait_image";
    private static final String PORTRAIT_INDEX = "portrait_index";
    private static final String PORTRAIT_TABLE_SQL = "CREATE TABLE "
            + PORTRAIT_TABLE + " ("
            + PORTRAIT_ID + " INTEGER PRIMARY KEY UNIQUE, "
            + PORTRAIT_TITLE + " TEXT, "
            + PORTRAIT_IMAGE + " BLOB, "
            + PORTRAIT_INDEX + " INTEGER )";

    public PortraitDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(PORTRAIT_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + PORTRAIT_TABLE);
        this.onCreate(db);
    }

    public void addPortrait(Integer id, String title, byte[] image, Integer index)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PORTRAIT_ID, id);
        values.put(PORTRAIT_TITLE, title);
        values.put(PORTRAIT_IMAGE, image);
        values.put(PORTRAIT_INDEX, index);
        db.insert(PORTRAIT_TABLE, null, values);
    }

    public void addPortraitList(ArrayList<Portrait> portraitList)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        int portraitListSize = portraitList.size();
        for (int i = 0; i < portraitListSize; i++)
        {
            addPortrait(portraitList.get(i).getId(), portraitList.get(i).getTitle(), portraitList.get(i).getImage(), portraitList.get(i).getIndex());
        }
    }

    public ArrayList<Portrait> getPortraitList()
    {
        String query = "SELECT * FROM " + PORTRAIT_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<Portrait> portraitList = new ArrayList<>();
        Portrait portrait;
        if (cursor.moveToLast())
        {
            do {
                portrait = new Portrait();
                portrait.setId(cursor.getInt(0));
                portrait.setTitle(cursor.getString(1));
                portrait.setImage(cursor.getBlob(2));
                portrait.setIndex(cursor.getInt(3));
                portraitList.add(portrait);
            }
            while (cursor.moveToPrevious());
        }
        cursor.close();

        return portraitList;
    }

    public void deleteTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PORTRAIT_TABLE, null, null);
    }
}

//PureBlack Software / Murat BIYIK