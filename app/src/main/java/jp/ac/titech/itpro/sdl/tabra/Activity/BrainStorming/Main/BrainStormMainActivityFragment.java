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
import android.widget.Toast;

import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity;
import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.CreatePostit.BrainStormPostitCreateFragment;
import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.ItemDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrainStormMainActivityFragment extends Fragment {

    private static final String TAG = BrainStormMainActivityFragment.class.getSimpleName();

    private LinearLayout mCustomScrollView;
    private AbsoluteLayout mWhiteBoard;
    private Button mCreatePostitButton;

    private PostitController mPostitCtrl;

    private int mScrollW;
    private int mScrollH;

    private int mWhiteBoardW = 1500;
    private int mWhiteBoardH = 1000;

    private static final int kMargin = 50;

    private ItemDataController mItemCtrl;

    public static BrainStormMainActivityFragment newInstance() {
        BrainStormMainActivityFragment fragment = new BrainStormMainActivityFragment();
        return fragment;
    }

    public BrainStormMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_brain_storm_main, container, false);

        mCustomScrollView = (LinearLayout)v.findViewById(R.id.brainstorm_main_custom_scrollview);
        mWhiteBoard = (AbsoluteLayout)v.findViewById(R.id.brainstorm_main_whiteboard);
        mCreatePostitButton = (Button)v.findViewById(R.id.brainstorm_main_create_postit_button);

        mPostitCtrl = new PostitController(getActivity());

        mCreatePostitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushCreateButton(v);
            }
        });

        mItemCtrl = new ItemDataController(getActivity());

        this.initializeWhiteboard();

        this.setPostits();

        return v;
    }

    private void initializeWhiteboard() {
        WindowManager wm = getActivity().getWindowManager();
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        mScrollW = size.x;
        mScrollH = size.y - 400;

        int l = (mScrollW - mWhiteBoardW) / 2;
        int t = (mScrollH - mWhiteBoardH) / 2;
        mPostitCtrl.setCenter(mScrollW / 2, mScrollH / 2);
        mWhiteBoard.layout(l, t, l + mWhiteBoardW, t + mWhiteBoardH);

        mWhiteBoard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {return onWhiteBoardTouch(v, event);}
        });
    }

    private void setPostits() {
        BrainStormMainActivity activity = (BrainStormMainActivity)getActivity();
        List<Item> itemList = mItemCtrl.getAllItems(activity.getmThemeId());
        for(Item item: itemList){
            View postitView = mPostitCtrl.createPostit(item);
            postitView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return onPostitTouch(v, event);
                }
            });
            postitView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickPostitLong(v);
                    return true;
                }
            });
            mWhiteBoard.addView(postitView);
        }
    }

    private void clickPostit(View v) {
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    private void clickPostitLong(View v) {
        Toast.makeText(getActivity(), "Long Clicked", Toast.LENGTH_SHORT).show();
    }

    private void pushCreateButton(View v) {
        ((BrainStormMainActivity)getActivity()).pushFragment(BrainStormPostitCreateFragment.newInstance(this.mPostitCtrl.getCenter()));
    }

    private float mDownX_Whiteboard;
    private float mDownY_Whiteboard;
    private boolean onWhiteBoardTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mDownX_Whiteboard = event.getX();
            mDownY_Whiteboard = event.getY();
            return true;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float moveX = event.getX() - mDownX_Whiteboard;
            float moveY = event.getY() - mDownY_Whiteboard;

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

            setXY(v, l, t);
            mPostitCtrl.setCenter(mScrollW/2 - l - kMargin, mScrollH/2 - t - kMargin);

            return false;
        }

        return false;
    }

    private float mDownX_Postit;
    private float mDownY_Postit;
    private boolean onPostitTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mDownX_Postit = event.getX();
            mDownY_Postit = event.getY();
            return true;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float moveX = event.getX() - mDownX_Postit;
            float moveY = event.getY() - mDownY_Postit;

            int w = v.getWidth();
            int h = v.getHeight();

            int l = (int)(v.getX() + moveX);
            int t = (int)(v.getY() + moveY);
            int r = l + w;
            int b = t + h;

            if(l < kMargin){l = kMargin;}
            if(t < kMargin){t = kMargin;}
            if(r > mScrollW - kMargin*2){l = mScrollW - kMargin*2 - w;}
            if(b > mScrollH - kMargin*2){t = mScrollH - kMargin*2 - h;}

            setXY(v, l, t);

            return false;
        }

        return false;
    }

    private void setXY(View v, int x, int y){
        v.setX(x);
        v.setY(y);
    }
}
