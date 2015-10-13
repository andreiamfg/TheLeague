package com.vfs.theleague.model;

import android.os.Parcel;
import android.os.Parcelable;

// Parcelable Object of summoner info & Stats
public class SummonerInfo implements Parcelable
{
    String summonerId;
    public void setSummonerId(String id){ this.summonerId = id;}
    public String getSummonerId(){return summonerId;}

    String summonerName;
    public void setSummonerName(String name){ this.summonerName = name;}
    public String getSummonerName(){return summonerName;}

    String summonerIcon;
    public void setSummonerIcon(String icon){ this.summonerIcon = icon;}
    public String getSummonerIcon(){return summonerIcon;}

    String summonerLevel;
    public void setSummonerLevel(String level){ this.summonerLevel = level;}
    public String getSummonerLevel(){return summonerLevel;}

    String summonerKills;
    public void setSummonerKills(String kills){ this.summonerKills = kills;}
    public String getSummonerKills(){return summonerKills;}

    String summonerWins;
    public void setSummonerWins(String deaths){ this.summonerWins = deaths;}
    public String getSummonerWins(){return summonerWins;}

    String summonerAssists;
    public void setSummonerAssists(String deaths){ this.summonerAssists = deaths;}
    public String getSummonerAssists(){return summonerAssists;}


    // Constructor of object
    public static final Parcelable.Creator<SummonerInfo> CREATOR = new Parcelable.Creator<SummonerInfo>()
    {
        @Override
        public SummonerInfo createFromParcel(Parcel source)
        {
            SummonerInfo thisSummoner = new SummonerInfo();
            thisSummoner.summonerId = source.readString();
            thisSummoner.summonerName = source.readString();
            thisSummoner.summonerIcon = source.readString();
            thisSummoner.summonerLevel = source.readString();
            thisSummoner.summonerKills = source.readString();
            thisSummoner.summonerWins = source.readString();
            thisSummoner.summonerAssists = source.readString();

            return thisSummoner;
        }

        @Override
        public SummonerInfo[] newArray(int size)
        {
            return new SummonerInfo[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    // Deconstructor of object to primitive data
    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeString(this.getSummonerId());
        parcel.writeString(this.getSummonerName());
        parcel.writeString(this.getSummonerIcon());
        parcel.writeString(this.getSummonerLevel());
        parcel.writeString(this.getSummonerKills());
        parcel.writeString(this.getSummonerWins());
        parcel.writeString(this.getSummonerAssists());
    }
}
