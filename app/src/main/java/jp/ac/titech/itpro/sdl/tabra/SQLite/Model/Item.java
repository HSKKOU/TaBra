package jp.ac.titech.itpro.sdl.tabra.SQLite.Model;

import android.content.ContentValues;
import android.database.Cursor;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_COLOR;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_CONTENT;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_POS_X;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_POS_Y;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_SERVER_ID;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_THEME_ID;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_USER_NAME;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class Item extends BaseModel {
    private long theme_id;
    private long server_id;
    private String content;
    private String userName;
    private String color;
    private int pos_x;
    private int pos_y;

    public Item(){}

    public Item(long theme_id, long server_id , String content, String username, String color, int pos_x, int pos_y){
        super();
        this.theme_id = theme_id;
        this.server_id = server_id;
        this.content = content;
        this.userName = username;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }

    // theme_id
    public long getTheme_id(){return this.theme_id;}
    public void setTheme_id(long theme_id){this.theme_id = theme_id;}

    // server_id
    public long getServer_id() {return server_id;}
    public void setServer_id(long server_id) {this.server_id = server_id;}

    // content
    public String getContent(){return this.content;}
    public void setContent(String content){this.content = content;}

    // username
    public String getUserName(){return this.userName;}
    public void setUserName(String username){this.userName = username;}

    public String getColor(){return this.color;}
    public void setColor(String color){this.color = color;}

    public int getPos_x(){return this.pos_x;}
    public void setPos_x(int pos_x){this.pos_x = pos_x;}
    public int getPos_y(){return this.pos_y;}
    public void setPos_y(int pos_y){this.pos_y = pos_y;}

    // set All Culomns
    public void setAllColumns(Cursor c){
        super.setCommonCulomns(c);
        this.theme_id = c.getLong(c.getColumnIndex(KEY_THEME_ID));
        this.server_id = c.getLong(c.getColumnIndex(KEY_SERVER_ID));
        this.content = c.getString(c.getColumnIndex(KEY_CONTENT));
        this.userName = c.getString(c.getColumnIndex(KEY_USER_NAME));
        this.color = c.getString(c.getColumnIndex(KEY_COLOR));
        this.pos_x = c.getInt(c.getColumnIndex(KEY_POS_X));
        this.pos_y = c.getInt(c.getColumnIndex(KEY_POS_Y));
    }

    public ContentValues trans2ContentValue() {
        ContentValues c = super.trans2ContentValue();
        c.put(KEY_THEME_ID, this.theme_id);
        c.put(KEY_SERVER_ID, this.server_id);
        c.put(KEY_CONTENT, this.content);
        c.put(KEY_USER_NAME, this.userName);
        c.put(KEY_COLOR, this.color);
        c.put(KEY_POS_X, this.pos_x);
        c.put(KEY_POS_Y, this.pos_y);
        return c;
    }

    public String toString() {
        return "id:" + this.getId()
                + "server_id" + this.getServer_id()
                + "theme_id" + this.getTheme_id()
                + "content" + this.getContent()
                + "userName" + this.getUserName()
                + "color" + this.getColor();
    }
}
