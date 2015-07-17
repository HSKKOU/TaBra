package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.User;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_NAME;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_USER_NAME;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.TABLE_USERS;


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
        return super.createModel(v);
    }

    public String getExistUser(){
        db = this.dbHelper.getReadableDatabase();
        Cursor c = db.query(this.tableName, null, null, null, null, null, null, "1");
        if(c.moveToFirst()){
            return c.getString(c.getColumnIndex(KEY_NAME));
        }else{
            return null;
        }
    }
}
