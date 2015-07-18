package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Theme;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_OVERVIEW;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_TITLE;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.TABLE_THEMES;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class ThemeDataController extends BaseDataController {
    private static final String TAG = ThemeDataController.class.getSimpleName();

    public ThemeDataController(Context context){
        super(context, TABLE_THEMES);
    }

    public long createTheme(Theme theme){
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE, theme.getTitle());
        v.put(KEY_OVERVIEW, theme.getOverview());
        return super.createModel(v);
    }

    public List<Theme> getAllThemes(){
        db = dbHelper.getReadableDatabase();
        List<Theme> themeList = new ArrayList<Theme>();

        Cursor c = db.query(this.tableName, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Theme t = new Theme();
                t.setAllColumns(c);
                themeList.add(t);
            } while (c.moveToNext());
        }

        db.close();
        return themeList;
    }

    public List<String> getAllThemesTitle(){
        List<Theme> themeList = this.getAllThemes();
        List<String> titles = new ArrayList<String>();
        for(Theme t : themeList){
            titles.add(t.getTitle());
        }

        return titles;
    }
}
