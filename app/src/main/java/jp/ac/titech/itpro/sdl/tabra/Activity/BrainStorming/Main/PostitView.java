package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

/**
 * Created by hskk1120551 on 15/07/21.
 */
public class PostitView extends View {
    private static final String TAG = PostitView.class.getSimpleName();
    private Item item;

    public PostitView(Context context) {
        super(context);
    }

    public PostitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PostitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Item getItem(){return this.item;}
    public void setItem(Item item){this.item = item;}
}
