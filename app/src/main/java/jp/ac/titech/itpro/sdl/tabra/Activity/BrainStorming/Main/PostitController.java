package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.content.Context;
import android.graphics.Point;
import android.widget.AbsoluteLayout;

import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

/**
 * Created by hskk1120551 on 15/07/20.
 */
public class PostitController {
    private static final String TAG = PostitController.class.getSimpleName();

    private Context mContext;
    private AbsoluteLayout mParentView;
    private PostitFactory mFactory;

    private Point center = new Point(0,0);

    public PostitController(Context context, AbsoluteLayout parent) {
        this.mContext = context;
        this.mParentView = parent;
        this.mFactory = new PostitFactory(mContext, this);
    }

    public void createPostit(Item item) {
        PostitView postitview = mFactory.createPostitView();

        postitview.setItem(item);

        mParentView.addView(postitview);
    }

    public Point getCenter() {
        return this.center;
    }

    public void setCenter(int x, int y) {
        this.center.x = x;
        this.center.y = y;
    }
}
