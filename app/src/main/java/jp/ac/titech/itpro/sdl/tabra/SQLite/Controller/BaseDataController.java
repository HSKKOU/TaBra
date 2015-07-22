package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_CREATED_AT;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_UPDATED_AT;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class BaseDataController {
    private static final String TAG = BaseDataController.class.getSimpleName();

    protected DBOpenHelper dbHelper;
    protected SQLiteDatabase db;
    protected String tableName = "";

    public BaseDataController(Context _context, String _tableName){
        dbHelper = new DBOpenHelper(_context);
        this.tableName = _tableName;
    }

    protected long createModel(ContentValues _v) {
        db = dbHelper.getWritableDatabase();
        long now = (new Date()).getTime();
        _v.put(KEY_CREATED_AT, now);
        _v.put(KEY_UPDATED_AT, now);
        long id = db.insert(this.tableName, null, _v);
        db.close();
        return id;
    }

    protected long updateModel(ContentValues _v, String _selection, String[] _selectionArgs) {
        db = dbHelper.getWritableDatabase();
        long now = (new Date()).getTime();
        _v.put(KEY_UPDATED_AT, now);
        long id = db.update(this.tableName, _v, _selection, _selectionArgs);
        db.close();
        return id;
    }

    public int deleteAll() {
        db = dbHelper.getWritableDatabase();
        int num = db.delete(this.tableName, null, null);
        db.close();
        return num;
    }
}
