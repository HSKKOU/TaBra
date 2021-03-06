package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.User;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_PASSWD;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_USER_NAME;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.TABLE_USERS;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.USER_COLUMS;


/**
 * Created by hskk1120551 on 15/07/17.
 */
public class UserDataController extends BaseDataController {
    private static final String TAG = UserDataController.class.getSimpleName();

    public UserDataController(Context context){
        super(context, TABLE_USERS);
    }

    public long createUser(User user){
        ContentValues v = new ContentValues();
        v.put(KEY_USER_NAME, user.getName());
        v.put(KEY_PASSWD, user.getPasswd());
        return super.createModel(v);
    }

    public String validateUserPasswd(String username, String passwd) {
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                this.tableName,
                USER_COLUMS,
                KEY_USER_NAME + "=? AND " + KEY_PASSWD + "=?",
                new String[]{username, passwd},
                null,
                null,
                null,
                null
        );
        String ret = "";
        if(c.moveToFirst()){
            ret = c.getString(c.getColumnIndex(KEY_USER_NAME));
        }

        db.close();
        return ret;
    }

    public String getExistUser(){
        db = dbHelper.getReadableDatabase();
        Cursor c = db.query(this.tableName, USER_COLUMS, null, null, null, null, null, null);
        String ret = "";
        if(c.moveToFirst()){
            ret = c.getString(c.getColumnIndex(KEY_USER_NAME));
        }

        db.close();
        return ret;
    }
}
