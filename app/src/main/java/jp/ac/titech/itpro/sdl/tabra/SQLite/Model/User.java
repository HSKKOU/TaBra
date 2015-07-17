package jp.ac.titech.itpro.sdl.tabra.SQLite.Model;

import android.database.Cursor;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_NAME;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class User extends BaseModel {
    private String name;

    public User(){
        super();
    }

    public User(String name){
        super();
        this.name = name;
    }

    // password
    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    // set All Culomns
    public void setAllColumns(Cursor c){
        super.setCommonCulomns(c);
        this.name = c.getString(c.getColumnIndex(KEY_NAME));
    }
}
