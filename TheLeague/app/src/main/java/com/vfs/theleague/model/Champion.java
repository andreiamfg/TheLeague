package com.vfs.theleague.model;

// Data Model class to be used by database
// that maps champion Id with Champion Name and Image Name
public class Champion
{
    private int id;
    private String name;
    private String img;

    public Champion()
    {
    }

    public Champion(int id,String name,String img)
    {
        super();
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public long getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImg()
    {
        return img;
    }

    public void setImg(String img)
    {
        this.img = img;
    }

    @Override
    public String toString()
    {
        return "Champion [id=" + id + ", name=" + name + " ,img=" + img + "]";
    }
}
