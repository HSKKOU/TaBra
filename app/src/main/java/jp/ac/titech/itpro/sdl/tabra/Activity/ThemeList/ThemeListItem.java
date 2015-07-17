package jp.ac.titech.itpro.sdl.tabra.Activity.ThemeList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class ThemeListItem implements Parcelable {
    private long id;
    private String title;

    public ThemeListItem(long id, String title){
        this.id = id;
        this.title = title;
    }

    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}

    public long getId(){return id;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ThemeListItem> CREATOR = new Creator<ThemeListItem>() {
        @Override
        public ThemeListItem createFromParcel(Parcel source) {
            return null;
        }

        public ThemeListItem[] newArray(int size) {
            return new ThemeListItem[size];
        }
    };
}
