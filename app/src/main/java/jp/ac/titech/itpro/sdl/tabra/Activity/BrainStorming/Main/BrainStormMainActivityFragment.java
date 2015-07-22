package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

    private Point mScrollSize = new Point(0,0);
    private Point mWhiteBoardSize = new Point(0,0);

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
        mScrollSize.x = size.x;
        mScrollSize.y = size.y - 400;

        int l = (mScrollSize.x - mWhiteBoard.getWidth()) / 2;
        int t = (mScrollSize.y - mWhiteBoard.getHeight()) / 2;
        mPostitCtrl.setCenter(mScrollSize.x / 2, mScrollSize.y / 2);
        mWhiteBoardSize = new Point(mWhiteBoard.getWidth(), mWhiteBoard.getHeight());
        mWhiteBoard.layout(l, t, l + mWhiteBoardSize.x, t + mWhiteBoardSize.y);

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
                public boolean onTouch(View v, MotionEvent event) {return onPostitTouch(v, event);}
            });
            postitView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {return clickPostitLong(v);}
            });
            mWhiteBoard.addView(postitView);
        }
    }

    private void clickPostit(View v) {
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    private boolean clickPostitLong(View v) {
        Toast.makeText(getActivity(), "Long Clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void pushCreateButton(View v) {
        ((BrainStormMainActivity)getActivity()).pushFragment(BrainStormPostitCreateFragment.newInstance(this.mPostitCtrl.getCenter()));
    }

    private Point mDown_WhiteBoard = new Point(0,0);
    private boolean onWhiteBoardTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mDown_WhiteBoard = new Point((int)event.getX(), (int)event.getY());
            return true;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float moveX = event.getX() - mDown_WhiteBoard.x;
            float moveY = event.getY() - mDown_WhiteBoard.y;

            int w = v.getWidth();
            int h = v.getHeight();

            int l = (int)(v.getX() + moveX);
            int t = (int)(v.getY() + moveY);
            int r = l + w;
            int b = t + h;

            if(l > kMargin){l = kMargin;}
            if(t > kMargin){t = kMargin;}
            if(r < mScrollSize.x - kMargin){l = mScrollSize.x - kMargin - w;}
            if(b < mScrollSize.y - kMargin){t = mScrollSize.y - kMargin - h;}

            setXY(v, l, t);

            Rect moveViewRect = viewRect(v);
            mPostitCtrl.setCenter(mScrollSize.x / 2 - moveViewRect.x - kMargin, mScrollSize.y / 2 - moveViewRect.y - kMargin);

            return false;
        }

        return false;
    }

    private Point mDown_Postit;
    private boolean onPostitTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mDown_Postit = new Point((int)event.getX(), (int)event.getY());
            mWhiteBoardSize = new Point(mWhiteBoard.getWidth(), mWhiteBoard.getHeight());
            return false;
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float moveX = event.getX() - mDown_Postit.x;
            float moveY = event.getY() - mDown_Postit.y;

            int w = v.getWidth();
            int h = v.getHeight();

            int l = (int)(v.getX() + moveX);
            int t = (int)(v.getY() + moveY);
            int r = l + w;
            int b = t + h;

            if(l < kMargin){l = kMargin;}
            if(t < kMargin){t = kMargin;}
            if(r > mWhiteBoardSize.x - kMargin){l = mWhiteBoardSize.x - kMargin - w;}
            if(b > mWhiteBoardSize.y - kMargin){t = mWhiteBoardSize.y - kMargin - h;}

            setXY(v, l, t);

            Rect moveViewRect = viewRect(v);
            Point moveViewCenter = moveViewRect.center();

            if((int)moveX == 0 && (int)moveY == 0) {return false;}
            v.setOnLongClickListener(null);
            return true;

        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            String idStr = ((TextView)v.findViewById(R.id.postit_hidden_id)).getText().toString();
            try{
                long id = Long.parseLong(idStr);
                mItemCtrl.updateItemPosition(id, (int)v.getX(), (int)v.getY());
            }catch(Exception e){
                Log.e(TAG, "Illegal id");
            }

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickPostitLong(v);
                    return true;
                }
            });

            return true;
        }

        return true;
    }

    private Rect viewRect(View v) {
        return new Rect((int)v.getX(), (int)v.getY(), v.getWidth(), v.getHeight());
    }

    private void setXY(View v, int x, int y){
        v.setX(x);
        v.setY(y);
    }
}

class Rect {
    public int x;
    public int y;
    public int width;
    public int height;

    public Rect(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public Point center() {
        return new Point(x + width/2, y + height/2);
    }
}
