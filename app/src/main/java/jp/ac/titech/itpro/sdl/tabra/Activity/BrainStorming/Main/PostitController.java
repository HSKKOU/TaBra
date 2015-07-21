package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.tabra.R;
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
        View postitview = mFactory.createPostitView();

        TextView nameText = (TextView)postitview.findViewById(R.id.postit_hidden_id);
        nameText.setText(item.getId() + "");

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
