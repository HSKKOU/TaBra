package jp.ac.titech.itpro.sdl.tabra.SQLite.Model;

import android.content.ContentValues;
import android.database.Cursor;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_CREATED_AT;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_ID;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_UPDATED_AT;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class BaseModel {
    private long id;
    private long created_at;
    private long updated_at;

    public BaseModel(){}
    public BaseModel(long id){this.id = id;}

    // id
    public long getId(){return this.id;}

    // created_at
    public long getCreated_at(){return this.created_at;}
    public void setCreated_at(long created_at){this.created_at = created_at;}

    // updated_at
    public long getUpdated_at(){return this.updated_at;}
    public void setUpdated_at(long updated_at){this.updated_at = updated_at;}

    // set id, created_at, updated_at
    public void setCommonCulomns(Cursor c){
        this.id = c.getLong(c.getColumnIndex(KEY_ID));
        this.created_at = c.getLong(c.getColumnIndex(KEY_CREATED_AT));
        this.updated_at = c.getLong(c.getColumnIndex(KEY_UPDATED_AT));
    }

    public ContentValues trans2ContentValue() {
        ContentValues c = new ContentValues();
        c.put(KEY_ID, this.id);
        c.put(KEY_CREATED_AT, this.created_at);
        c.put(KEY_UPDATED_AT, this.updated_at);
        return c;
    }
}
