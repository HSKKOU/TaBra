package jp.ac.titech.itpro.sdl.tabra.SQLite.Controller;

import android.content.ContentValues;
import android.content.Context;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_CONTENT;
import static jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.DBOpenHelper.KEY_NAME;
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
        v.put(KEY_NAME, item.getName());
        v.put(KEY_CONTENT, item.getContent());
        v.put(KEY_USER_NAME, item.getName());
        return super.createModel(v);
    }
}
