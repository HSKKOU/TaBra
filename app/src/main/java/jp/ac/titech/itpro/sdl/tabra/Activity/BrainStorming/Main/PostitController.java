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

        TextView idText = (TextView)postitview.findViewById(R.id.postit_hidden_id);
        idText.setText(item.getId() + "");

        TextView contentText = (TextView)postitview.findViewById(R.id.postit_text);
        contentText.setText(item.getContent() + "");

        postitview.setBackgroundColor(colorStr2Int(mContext, item.getColor()));

        postitview.setX(item.getPos_x());
        postitview.setY(item.getPos_y());

        mParentView.addView(postitview);
    }

    public Point getCenter() {
        return this.center;
    }

    public void setCenter(int x, int y) {
        this.center.x = x;
        this.center.y = y;
    }

    public static int colorStr2Int(Context context, String colorStr) {
        if("postit_red".equals(colorStr)){return context.getResources().getColor(R.color.postit_red);}
        else if("postit_yellow".equals(colorStr)){return context.getResources().getColor(R.color.postit_yellow);}
        else if("postit_blue".equals(colorStr)){return context.getResources().getColor(R.color.postit_blue);}
        else if("postit_green".equals(colorStr)){return context.getResources().getColor(R.color.postit_green);}
        else{return context.getResources().getColor(R.color.postit_white);}
    }
}
