package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import jp.ac.titech.itpro.sdl.tabra.R;

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

    public View createPostitView() {
        View postit = mInflater.inflate(R.layout.postit, null);
        Point center = mPostitCtrl.getCenter();
        postit.setX(center.x - mPostitH/2);
        postit.setY(center.y - mPostitH / 2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mPostitW, mPostitH);
        postit.setLayoutParams(layoutParams);
        return postit;
    }
}
