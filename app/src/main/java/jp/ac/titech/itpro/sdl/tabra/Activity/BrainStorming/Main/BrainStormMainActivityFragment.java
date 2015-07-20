package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;

import jp.ac.titech.itpro.sdl.tabra.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrainStormMainActivityFragment extends Fragment implements View.OnTouchListener {

    private static final String TAG = BrainStormMainActivityFragment.class.getSimpleName();

    private LinearLayout mCustomScrollView;
    private AbsoluteLayout mWhiteBoard;
    private Button mCreatePostitButton;

    private PostitController mPostitCtrl;

    private float mDownX;
    private float mDownY;

    private int mScrollW;
    private int mScrollH;

    private int mWhiteBoardW = 4000;
    private int mWhiteBoardH = 4000;

    private static final int kMargin = 30;

    public BrainStormMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brain_storm_main, container, false);

        mCustomScrollView = (LinearLayout)v.findViewById(R.id.brainstorm_main_custom_scrollview);
        mWhiteBoard = (AbsoluteLayout)v.findViewById(R.id.brainstorm_main_whiteboard);
        mCreatePostitButton = (Button)v.findViewById(R.id.brainstorm_main_create_postit_button);

        mPostitCtrl = new PostitController(getActivity(), mWhiteBoard);

        mCreatePostitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushCreateButton(v);
            }
        });

        WindowManager wm = getActivity().getWindowManager();
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        mScrollW = size.x;
        mScrollH = size.y - 400;

        int l = (mScrollW - mWhiteBoardW) / 2;
        int t = (mScrollH - mWhiteBoardH) / 2;
        mPostitCtrl.setCenter(mScrollW/2, mScrollH/2);
        mWhiteBoard.layout(l, t, l + mWhiteBoardW, t + mWhiteBoardH);

        mWhiteBoard.setOnTouchListener(this);

        return v;
    }

    private void pushCreateButton(View v) {
        mPostitCtrl.createPostit();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mDownX = event.getX();
            mDownY = event.getY();

            return true;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float moveX = event.getX() - mDownX;
            float moveY = event.getY() - mDownY;

            int w = v.getWidth();
            int h = v.getHeight();

            int l = (int)(v.getX() + moveX);
            int t = (int)(v.getY() + moveY);
            int r = l + w;
            int b = t + h;

            if(l > kMargin){l = kMargin;}
            if(t > kMargin){t = kMargin;}
            if(r < mScrollW - kMargin){l = mScrollW - kMargin - w;}
            if(b < mScrollH - kMargin){t = mScrollH - kMargin - h;}

            v.setX(l);
            v.setY(t);
            mPostitCtrl.setCenter(mScrollW/2 - l - kMargin, mScrollH/2 - t - kMargin);

            return false;
        }

        return false;
    }
}
