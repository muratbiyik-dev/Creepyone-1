package com.pureblacksoft.creepyone.data;

import android.content.Context;

import com.pureblacksoft.creepyone.R;

public class Article
{
    private Integer id;
    private String title;
    private String content;
    private String author;
    private byte[] image;
    private Integer popularity;
    private Float readingTime;
    private String readingTimeStr;

    public Article() {}

    //Getters
    public Integer getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getContent()
    {
        return this.content;
    }

    public String getAuthor()
    {
        return this.author;
    }

    public byte[] getImage()
    {
        return this.image;
    }

    public Integer getPopularity()
    {
        return this.popularity;
    }

    public Float getReadingTime()
    {
        return this.readingTime;
    }

    public String getReadingTimeStr()
    {
        return this.readingTimeStr;
    }
    //Getters/

    //Setters
    public void setId(Integer id)
    {
        this.id = id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public void setPopularity(Integer popularity)
    {
        this.popularity = popularity;
    }

    public void setReadingTime(Float readingTime)
    {
        this.readingTime = readingTime;
    }

    public void setReadingTimeStr(String readingTimeStr)
    {
        this.readingTimeStr = readingTimeStr;
    }
    //Setters/

    public static float calcReadingTime(String string)
    {
        float readingTime;
        float count = 0;
        int strLength = string.length();
        char[] chr = new char[strLength];

        for (int i = 0; i < strLength; i++)
        {
            chr[i] = string.charAt(i);

            if ( ((i > 0) && (chr[i] != ' ') && (chr[i - 1] == ' ')) || ((chr[0] != ' ') && (i == 0)) )
            {
                count++;
            }
        }

        readingTime = count / 200;

        return readingTime;
    }

    public static String findReadingTimeStr(Context context, float readingTime, int appLocaleCode)
    {
        int readingTimeInt = Math.round(readingTime);

        String readingTimeStr;
        if (appLocaleCode == 2)
        {
            if (readingTimeInt < 1)
            {
                readingTimeStr = context.getString(R.string.Reading_Time_Unit_Under1_TR);
            }
            else if (readingTimeInt == 1)
            {
                readingTimeStr = context.getString(R.string.Reading_Time_Unit_1_TR);
            }
            else
            {
                readingTimeStr = String.valueOf(readingTimeInt) + context.getString(R.string.Reading_Time_Unit_Over1_TR);
            }
        }
        else
        {
            if (readingTimeInt < 1)
            {
                readingTimeStr = context.getString(R.string.Reading_Time_Unit_Under1_EN);
            }
            else if (readingTimeInt == 1)
            {
                readingTimeStr = context.getString(R.string.Reading_Time_Unit_1_EN);
            }
            else
            {
                readingTimeStr = String.valueOf(readingTimeInt) + context.getString(R.string.Reading_Time_Unit_Over1_EN);
            }
        }

        return readingTimeStr;
    }
}

//PureBlack Software / Murat BIYIK