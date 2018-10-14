package com.example.cas.gamebacklog;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "game")
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo (name = "name")
    private String name;

    @ColumnInfo (name = "console")
    private String console;

    @ColumnInfo (name = "status")
    private String status;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getConsole() {
        return console;
    }
    public void setConsole(String console) {
        this.console = console;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


    public Game(String name, String console, String status) {
        this.name = name;
        this.console = console;
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.console);
        dest.writeString(this.status);
    }

    protected Game(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.console = in.readString();
        this.status = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
}
