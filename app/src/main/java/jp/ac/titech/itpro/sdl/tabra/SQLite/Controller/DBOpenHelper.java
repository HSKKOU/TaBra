package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = DBOpenHelper.class.getSimpleName();

    // about DB
    public static final String DB_NAME = "TaBra";
    public static final int DB_VERSION = 1;

    // TABLE name
    public static final String TABLE_THEMES = "themes";
    public static final String TABLE_ITEMS = "items";
    public static final String TABLE_USERS = "users";

    // Common column names
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_UPDATED_AT = "updated_at";
    public static final String KEY_USER_NAME = "user_name";

    // USER TABLE - column names

    // THEME TABLE - column names
    public static final String KEY_TITLE = "title";
    public static final String KEY_OVERVIEW = "overview";

    // ITEM TABLE - column names
    public static final String KEY_NAME = "name";
    public static final String KEY_CONTENT = "content";

    // Statements
    // USER TABLE create statement
    private static final String CREATE_TABLE_USER
            = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_USER_NAME + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " INTEGER NOT NULL,"
            + KEY_UPDATED_AT + " INTEGER NOT NULL"
            + ")";

    // THEME TABLE create statement
    private static final String CREATE_TABLE_THEME
            = "CREATE TABLE " + TABLE_THEMES + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_TITLE + " TEXT NOT NULL,"
            + KEY_OVERVIEW + " TEXT,"
            + KEY_CREATED_AT + " INTEGER NOT NULL,"
            + KEY_UPDATED_AT + " INTEGER NOT NULL"
            + ")";

    // ITEM TABLE create statement
    private static final String CREATE_TABLE_ITEM
            = "CREATE TABLE " + TABLE_ITEMS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_NAME + " TEXT NOT NULL,"
            + KEY_CONTENT + " TEXT,"
            + KEY_USER_NAME + " TEXT NOT NULL,"
            + KEY_CREATED_AT + " INTEGER NOT NULL,"
            + KEY_UPDATED_AT + " INTEGER NOT NULL"
            + ")";


    public DBOpenHelper(Context context){
        super(context, DB_NAME + ".db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_THEME);
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // delete
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THEMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

        // recreate
        onCreate(db);
    }
}
