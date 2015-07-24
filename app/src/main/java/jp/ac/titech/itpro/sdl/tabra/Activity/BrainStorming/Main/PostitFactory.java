package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

/**
 * Created by hskk1120551 on 15/07/20.
 */
public class PostitFactory {
    private static final String TAG = PostitFactory.class.getSimpleName();

    private Context mContext;
    private LayoutInflater mInflater;
    private PostitController mPostitCtrl;

    private int mPostitW = 240;
    private int mPostitH = 300;

    public PostitFactory(Context context, PostitController pc) {
        this.mContext = context;
        mPostitCtrl = pc;
        mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View createPostitView(Item item) {
        View postit = mInflater.inflate(R.layout.postit, null);
        Point center = mPostitCtrl.getCenter();
        postit.setX(center.x - mPostitH/2);
        postit.setY(center.y - mPostitH / 2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mPostitW, mPostitH);
        postit.setLayoutParams(layoutParams);

        TextView idText = (TextView)postit.findViewById(R.id.postit_hidden_id);
        idText.setText(item.getServer_id() + "");

        TextView contentText = (TextView)postit.findViewById(R.id.postit_text);
        contentText.setText(item.getContent() + "");

        postit.setBackgroundColor(colorStr2Int(mContext, item.getColor()));

        postit.setX(item.getPos_x());
        postit.setY(item.getPos_y());

        return postit;
    }

    public static int colorStr2Int(Context context, String colorStr) {
        if("postit_red".equals(colorStr)){return context.getResources().getColor(R.color.postit_red);}
        else if("postit_yellow".equals(colorStr)){return context.getResources().getColor(R.color.postit_yellow);}
        else if("postit_blue".equals(colorStr)){return context.getResources().getColor(R.color.postit_blue);}
        else if("postit_green".equals(colorStr)){return context.getResources().getColor(R.color.postit_green);}
        else{return context.getResources().getColor(R.color.postit_white);}
    }
}
