package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.content.Context;
import android.graphics.Point;
import android.view.View;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

/**
 * Created by hskk1120551 on 15/07/20.
 */
public class PostitController {
    private static final String TAG = PostitController.class.getSimpleName();

    private Context mContext;
    private PostitFactory mFactory;

    private Point mCenter = new Point(0,0);

    public PostitController(Context context) {
        this.mContext = context;
        this.mFactory = new PostitFactory(mContext, this);
    }

    public View createPostit(Item item) {
        return mFactory.createPostitView(item);
    }

    public Point getCenter() {
        return this.mCenter;
    }

    public void setCenter(int x, int y) {
        this.mCenter.x = x;
        this.mCenter.y = y;
    }
}
