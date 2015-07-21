package jp.ac.titech.itpro.sdl.tabra.SQLite.Model;

import android.database.Cursor;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_COLOR;
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
    private String color;
    private int pos_x;
    private int pos_y;

    public Item(){}

    public Item(String name, String content, String username, String color, int pos_x, int pos_y){
        super();
        this.name = name;
        this.content = content;
        this.userName = username;
        this.color = color;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
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

    public String getColor(){return this.color;}
    public void setColor(String color){this.color = color;}

    public int getPos_x(){return this.pos_x;}
    public void setPos_x(int pos_x){this.pos_x = pos_x;}
    public int getPos_y(){return this.pos_y;}
    public void setPos_y(int pos_y){this.pos_y = pos_y;}

    // set All Culomns
    public void setAllColumns(Cursor c){
        super.setCommonCulomns(c);
        this.name = c.getString(c.getColumnIndex(KEY_NAME));
        this.content = c.getString(c.getColumnIndex(KEY_CONTENT));
        this.userName = c.getString(c.getColumnIndex(KEY_USER_NAME));
        this.color = c.getString(c.getColumnIndex(KEY_COLOR));
    }
}
