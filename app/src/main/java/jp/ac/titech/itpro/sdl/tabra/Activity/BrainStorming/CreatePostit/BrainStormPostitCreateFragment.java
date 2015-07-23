package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.CreatePostit;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity;
import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main.PostitFactory;
import jp.ac.titech.itpro.sdl.tabra.Activity.SpeechRecognizer.VoiceRecog;
import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.ItemDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Item;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrainStormPostitCreateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrainStormPostitCreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrainStormPostitCreateFragment extends Fragment implements View.OnTouchListener, VoiceRecog.OnVoiceRecog {
    private static final String TAG = BrainStormPostitCreateFragment.class.getSimpleName();
    private static final String CENTERX = "centerX";
    private static final String CENTERY = "centerY";

    private OnFragmentInteractionListener mListener;

    private ItemDataController mItemCtrl;

    private EditText mContentEditView;
    private TextView mContentTextView;
    private TextView mUsernameTextView;
    private TextView mCreatedAtTextView;
    private ImageButton mMicButton;
    private LinearLayout mPostitView;

    private VoiceRecog mVoiceRecog;

    private String mPostitColor = "postit_red";

    private Point mMainFragmentCenter;

    public static BrainStormPostitCreateFragment newInstance(Point center) {
        BrainStormPostitCreateFragment fragment = new BrainStormPostitCreateFragment();
        Bundle args = new Bundle();
        args.putInt(CENTERX, center.x);
        args.putInt(CENTERY, center.y);
        fragment.setArguments(args);
        return fragment;
    }

    public BrainStormPostitCreateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int cx = getArguments().getInt(CENTERX);
            int cy = getArguments().getInt(CENTERY);
            this.mMainFragmentCenter = new Point(cx, cy);
        }

        mItemCtrl = new ItemDataController(getActivity());
        mVoiceRecog = new VoiceRecog(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brain_storm_postit_create, container, false);

        mContentEditView = (EditText)v.findViewById(R.id.postit_create_edit);
        mContentTextView = (TextView)v.findViewById(R.id.postit_create_content);
        mUsernameTextView = (TextView)v.findViewById(R.id.postit_create_username);
        mCreatedAtTextView = (TextView)v.findViewById(R.id.postit_create_created_at);
        mMicButton = (ImageButton)v.findViewById(R.id.postit_create_mic_button);

        mPostitView = (LinearLayout)v.findViewById(R.id.postit_create_postit);

        setListeners(v);

        String userName = ((BrainStormMainActivity)getActivity()).getmUserName();
        mUsernameTextView.setText(userName);

        return v;
    }

    private void setListeners(View v) {
        mPostitView.setOnTouchListener(this);

        v.findViewById(R.id.postit_color_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changePostitColor("postit_red", mPostitView);}
        });
        v.findViewById(R.id.postit_color_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changePostitColor("postit_yellow", mPostitView);}
        });
        v.findViewById(R.id.postit_color_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changePostitColor("postit_blue", mPostitView);}
        });
        v.findViewById(R.id.postit_color_green).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changePostitColor("postit_green", mPostitView);}
        });
        v.findViewById(R.id.postit_color_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changePostitColor("postit_white", mPostitView);}
        });

        mContentEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mContentTextView.setText(s);
            }
        });

        mMicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVoiceRecog.startRecognize();
            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardHide();
            }
        });
    }

    private void changePostitColor(String color, View v){
        keyboardHide();
        mPostitColor = color;
        int colorHex = PostitFactory.colorStr2Int(getActivity(), color);
        v.setBackgroundColor(colorHex);
    }

    private float mDownY;
    private int defY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        keyboardHide();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                defY = (int)v.getY();
                mDownY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                float moveY = event.getY() - mDownY;
                int currentY = (int)(moveY + v.getY());

                if (v.getY() <= defY) {
                    v.setY(currentY);
                }

                return false;
            case MotionEvent.ACTION_UP:
                if (v.getY() > defY) {
                    return false;
                }

                String contentText = mContentEditView.getText().toString();
                if (v.getY() < defY - v.getHeight() / 4 && !"".equals(contentText)) {
                    this.animateTransitionPostitView(v, v.getHeight()*(-1));
                } else {
                    this.animateTransitionPostitView(v, defY);
                }

                return false;
        }
        return false;
    }

    private void animateTransitionPostitView(final View target, final int dest) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, "y", target.getY(), dest);
        oa.setDuration(500);
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(dest == defY){return;}
                BrainStormMainActivity activity = (BrainStormMainActivity)getActivity();
                Log.d(TAG, mMainFragmentCenter.x + "," + mMainFragmentCenter.y);
                Item item = new Item(
                        activity.getmThemeId(),
                        mContentTextView.getText().toString(),
                        activity.getmUserName(),
                        mPostitColor,
                        mMainFragmentCenter.x,
                        mMainFragmentCenter.y
                );
                mItemCtrl.createItem(item);
                ((BrainStormMainActivity)getActivity()).popFragment();
            }
        });
        oa.start();
    }

    private void keyboardHide() {
        Activity activity = getActivity();
        View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onReciveVoice(String voiceStr) {
        mContentEditView.setText(voiceStr);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
