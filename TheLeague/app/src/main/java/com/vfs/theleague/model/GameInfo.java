package com.vfs.theleague.model;

import android.os.Parcel;
import android.os.Parcelable;

// Object that holds game information
// and implements Parcelable so it can be sent through intents
public class GameInfo implements Parcelable
{
    String gameMode;
    public String getGameMode(){return gameMode;}

    String champion;
    public String getChampion(){return champion;}

    String outcome;
    public String getOutcome(){return outcome;}

    public GameInfo(String mode, String champ, String winOrLose)
    {
        this.gameMode = mode;
        this.champion = champ;
        this.outcome = winOrLose;
    }

    // Parcelable constructor
    public static final Parcelable.Creator<GameInfo> CREATOR = new Parcelable.Creator<GameInfo>()
    {
        @Override
        public GameInfo createFromParcel(Parcel source)
        {
            GameInfo thisGame;
            thisGame = new GameInfo(source.readString(), source.readString(), source.readString());

            return thisGame;
        }

        @Override
        public GameInfo[] newArray(int size)
        {
            return new GameInfo[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    // Parcelable deconstructor
    @Override
    public void writeToParcel(Parcel parcel, int flags)
    {
        parcel.writeString(this.getGameMode());
        parcel.writeString(this.getChampion());
        parcel.writeString(this.getOutcome());
    }
}
