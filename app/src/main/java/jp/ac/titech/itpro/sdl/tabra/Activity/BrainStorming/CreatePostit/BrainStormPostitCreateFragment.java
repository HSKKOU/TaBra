package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.CreatePostit;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity;
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
public class BrainStormPostitCreateFragment extends Fragment implements View.OnTouchListener {
    private static final String TAG = BrainStormPostitCreateFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private ItemDataController mItemCtrl;

    private EditText mContentEditView;
    private TextView mContentTextView;
    private TextView mUsernameTextView;
    private TextView mCreatedAtTextView;

    private String mPostitColor = "ffc0cb";

    public static BrainStormPostitCreateFragment newInstance() {
        BrainStormPostitCreateFragment fragment = new BrainStormPostitCreateFragment();
        return fragment;
    }

    public BrainStormPostitCreateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brain_storm_postit_create, container, false);

        LinearLayout postitView = (LinearLayout)v.findViewById(R.id.postit_create_postit);
        postitView.setOnTouchListener(this);

        mContentEditView = (EditText)v.findViewById(R.id.postit_create_edit);
        mContentTextView = (TextView)v.findViewById(R.id.postit_create_content);
        mUsernameTextView = (TextView)v.findViewById(R.id.postit_create_username);
        mCreatedAtTextView = (TextView)v.findViewById(R.id.postit_create_created_at);

        String userName = ((BrainStormMainActivity)getActivity()).getmUserName();
        mUsernameTextView.setText(userName);

        mItemCtrl = new ItemDataController(getActivity());

        return v;
    }


    private float mDownY;
    private int defY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
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

                if (v.getY() < defY - v.getHeight() / 4) {
                    this.animateTransitionPostitView(v, v.getHeight()*(-1));
                } else {
                    this.animateTransitionPostitView(v, defY);
                }

                return false;
        }
        return false;
    }

    private void animateTransitionPostitView(final View target, int dest) {
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
                BrainStormMainActivity activity = (BrainStormMainActivity)getActivity();
                Item item = new Item(
                        activity.getmThemeId(),
                        mContentTextView.getText().toString(),
                        activity.getmUserName(),
                        mPostitColor,
                        (int)target.getX(),
                        (int)target.getY()
                );
                mItemCtrl.createItem(item);
                ((BrainStormMainActivity)getActivity()).popFragment();
            }
        });
        oa.start();
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
