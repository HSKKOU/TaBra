package jp.ac.titech.itpro.sdl.tabra.SQLite.Model;

import android.database.Cursor;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_OVERVIEW;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_TITLE;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class Theme extends BaseModel {
    private String title;
    private String overview;

    public Theme(){super();}

    public Theme(String title, String overview){
        super();
        this.title = title;
        this.overview = overview;
    }

    // title
    public String getTitle(){return this.title;}
    public void setTitle(String title){this.title = title;}

    // overview
    public String getOverview(){return this.overview;}
    public void setOverview(String title){this.overview = overview;}

    // set All Culomns
    public void setAllColumns(Cursor c){
        super.setCommonCulomns(c);
        this.title = c.getString(c.getColumnIndex(KEY_TITLE));
        this.overview = c.getString(c.getColumnIndex(KEY_OVERVIEW));
    }
}
