package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_COLOR;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_CONTENT;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_POS_X;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_POS_Y;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_THEME_ID;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_USER_NAME;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.TABLE_ITEMS;

/**
 * Created by hskk1120551 on 15/07/17.
 */
public class ItemDataController extends BaseDataController {
    private static final String TAG = ItemDataController.class.getSimpleName();

    public ItemDataController(Context context){
        super(context, TABLE_ITEMS);
    }

    public long createItem(Item item){
        ContentValues v = new ContentValues();
        v.put(KEY_THEME_ID, item.getTheme_id());
        v.put(KEY_CONTENT, item.getContent());
        v.put(KEY_USER_NAME, item.getUserName());
        v.put(KEY_COLOR, item.getColor());
        v.put(KEY_POS_X, item.getPos_x());
        v.put(KEY_POS_Y, item.getPos_y());
        return super.createModel(v);
    }

    public List<Item> getAllItems(){
        db = dbHelper.getReadableDatabase();
        List<Item> itemList = new ArrayList<Item>();

        Cursor c = db.query(this.tableName, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                Item i = new Item();
                i.setAllColumns(c);
                itemList.add(i);
            } while (c.moveToNext());
        }

        db.close();
        return itemList;
    }
}
