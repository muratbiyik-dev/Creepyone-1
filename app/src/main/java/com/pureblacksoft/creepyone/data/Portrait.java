package com.pureblacksoft.creepyone.data;

public class Portrait
{
    private Integer id;
    private String title;
    private byte[] image;
    private Integer index;

    public Portrait() {}

    //Getters
    public Integer getId()
    {
        return this.id;
    }

    public String getTitle()
    {
        return this.title;
    }

    public byte[] getImage()
    {
        return this.image;
    }

    public Integer getIndex()
    {
        return this.index;
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

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public void setIndex(Integer index)
    {
        this.index = index;
    }
    //Setters/
}

//PureBlack Software / Murat BIYIK