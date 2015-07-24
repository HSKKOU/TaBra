package jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.CreatePostit.BrainStormPostitCreateFragment;
import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.DetailPostit.BrainStormPostitDetailFragment;
import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.Main.BrainStormMainActivityFragment;
import jp.ac.titech.itpro.sdl.tabra.R;

public class BrainStormMainActivity extends Activity implements
        BrainStormPostitCreateFragment.OnFragmentInteractionListener,
        BrainStormPostitDetailFragment.OnFragmentInteractionListener
{
    private static final String TAG = BrainStormMainActivity.class.getSimpleName();

    public static final String PARAM_THEME_ID = "THEME_ID";
    public static final String PARAM_USER_NAME = "USER_NAME";
    public static final String PARAM_SERVER_THEME_ID = "SERVER_THEME_ID";

    private long mThemeId;
    private String mUserName = "";
    private String mServerThemeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brain_storm_main);

        Intent intent = getIntent();
        mThemeId = intent.getLongExtra(PARAM_THEME_ID, -1);
        mUserName = intent.getStringExtra(PARAM_USER_NAME);
        mServerThemeId = intent.getStringExtra(PARAM_SERVER_THEME_ID);

        Log.d(TAG, "id: " + mThemeId + ", userName: " + mUserName + ", serverThemeId: " + mServerThemeId);

        replaceFragment(BrainStormMainActivityFragment.newInstance());
    }

    public void replaceFragment(Fragment f){
        getFragmentManager().beginTransaction().replace(R.id.container, f).commit();
    }
    public void pushFragment(Fragment f){
        getFragmentManager().beginTransaction().replace(R.id.container, f).addToBackStack(null).commit();
    }
    public void popFragment(){
        getFragmentManager().popBackStack();
    }
    public void clearAllFragments(){
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public long getmThemeId(){return this.mThemeId;}
    public String getmUserName(){return this.mUserName;}
    public String getmServerThemeId() {return mServerThemeId;}

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_brain_storm_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isConnectedInternet(Context context) {
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }
}
