package jp.ac.titech.itpro.sdl.tabra.SQLite.Model;

import android.database.Cursor;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_CONTENT;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_NAME;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_USER_NAME;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class Item extends BaseModel {
    private String name;
    private String content;
    private String userName;

    public Item(){}

    public Item(String name, String content, String username){
        super();
        this.name = name;
        this.content = content;
        this.userName = username;
    }

    // name
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    // content
    public String getContent(){return this.content;}
    public void setContent(String content){this.content = content;}

    // username
    public String getUserName(){return this.userName;}
    public void setUserName(String username){this.userName = username;}

    // set All Culomns
    public void setAllColumns(Cursor c){
        super.setCommonCulomns(c);
        this.name = c.getString(c.getColumnIndex(KEY_NAME));
        this.content = c.getString(c.getColumnIndex(KEY_CONTENT));
        this.userName = c.getString(c.getColumnIndex(KEY_USER_NAME));
    }
}
