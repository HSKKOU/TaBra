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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity;
import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.CreatePostit.BrainStormPostitCreateFragment;
import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.DetailPostit.BrainStormPostitDetailFragment;
import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.ItemDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;
import jp.ac.titech.itpro.sdl.tabra.ServerConnector.ServerConnector;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrainStormMainActivityFragment extends Fragment implements ServerConnector.ServerConnectorDelegate {

    private static final String TAG = BrainStormMainActivityFragment.class.getSimpleName();

    private static final String GET_ITEMS = "GET_ITEMS";
    private static final String UPDATE_POS = "UPDATE_POS";

    private static final int POSTIT_TAG = 1;

    private LinearLayout mCustomScrollView;
    private AbsoluteLayout mWhiteBoard;
    private Button mCreatePostitButton;
    private Button mSyncButton;

    private PostitController mPostitCtrl;

    private Point mScrollSize = new Point(0,0);
    private Point mWhiteBoardSize = new Point(0,0);

    private static final int kMargin = 50;

    private ItemDataController mItemCtrl;

    private BrainStormMainActivity mActivity;

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

        mActivity = (BrainStormMainActivity)getActivity();

        mCustomScrollView = (LinearLayout)v.findViewById(R.id.brainstorm_main_custom_scrollview);
        mWhiteBoard = (AbsoluteLayout)v.findViewById(R.id.brainstorm_main_whiteboard);
        mCreatePostitButton = (Button)v.findViewById(R.id.brainstorm_main_create_postit_button);
        mSyncButton = (Button)v.findViewById(R.id.brainstorm_main_syncronize_button);

        mPostitCtrl = new PostitController(getActivity());

        mCreatePostitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushCreateButton(v);
            }
        });

        mSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPostits();
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
        if(mActivity.isConnectedInternet(mActivity)){
            mWhiteBoard.removeAllViews();
            String serverThemeId = mActivity.getmServerThemeId();
            (new ServerConnector(this)).execute(GET_ITEMS, ServerConnector.ITEMS, "", ServerConnector.POST, "type=get_items_by_theme_id&theme_id=" + serverThemeId);
        }else{
            List<Item> itemList = mItemCtrl.getAllItems(mActivity.getmThemeId());
            setItems(itemList);
        }
    }

    private void setItems(List<Item> itemList) {
        for(Item item: itemList){
            View postitView = mPostitCtrl.createPostit(item);
            postitView.setTag(POSTIT_TAG);
            postitView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {return onPostitTouch(v, event);}
            });
//            postitView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {return clickPostitLong(v);}
//            });
            mWhiteBoard.addView(postitView);
        }
    }

    private void clickPostit(View v) {
        Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    private boolean clickPostitLong(View v) {
        Toast.makeText(getActivity(), "Long Clicked", Toast.LENGTH_SHORT).show();
        String itemIdStr = ((TextView)v.findViewById(R.id.postit_hidden_id)).getText().toString();
        mActivity.pushFragment(BrainStormPostitDetailFragment.newInstance(itemIdStr));
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
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return clickPostitLong(v);
                }
            });
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
            (new ServerConnector(this)).execute(UPDATE_POS, ServerConnector.ITEMS, "", ServerConnector.POST, "type=update_item_pos&id="+idStr+"&pos_x="+(int)v.getX()+"&pos_y="+(int)v.getY());
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

    @Override
    public void recieveResponse(String serverConnectorId, String responseStr) {
        JSONObject json = null;
        try{
            json = new JSONObject(responseStr);
            String result = json.getString("result");
            if(!"success".equals(result)){
                Log.d(TAG, "failed");
                return;
            }
            if(GET_ITEMS.equals(serverConnectorId)){
                finishGetItems(json);
            } else if(UPDATE_POS.equals(serverConnectorId)) {
                finishUpdatePos(json);
            }
        }catch(JSONException e){
            Log.d(TAG, "json parse error");
        }
    }

    private void finishGetItems(JSONObject json) throws JSONException {
        mItemCtrl.deleteAllByThemeId(mActivity.getmServerThemeId());
        JSONArray ja = json.getJSONArray("data");
        for(int i=0; i<ja.length(); i++){
            JSONObject jo = ja.getJSONObject(i);
            mItemCtrl.createItem(jsonObj2Item(jo));
        }

        try{
            long sid = Long.parseLong(mActivity.getmServerThemeId());
            List<Item> itemList = mItemCtrl.getAllItems(sid);
            setItems(itemList);
        }catch(Exception e){

        }
    }

    private void finishUpdatePos(JSONObject json) throws JSONException {
        long id = json.getLong("id");
        int posX = json.getInt("pos_x");
        int posY = json.getInt("pos_y");
        mItemCtrl.updateItemPosition(id, posX, posY);
    }

    private Item jsonObj2Item(JSONObject jo) throws JSONException {
        Log.d(TAG, jo.toString());
        Item ret = new Item(
                jo.getLong("theme_id"),
                jo.getLong("id"),
                jo.getString("content"),
                jo.getString("username"),
                jo.getString("color"),
                jo.getInt("pos_x"),
                jo.getInt("pos_y")
        );

        return ret;
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
