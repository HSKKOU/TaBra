package jp.ac.titech.itpro.sdl.tabra.Activity.ThemeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity;
import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.ThemeDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.UserDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Theme;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.User;
import jp.ac.titech.itpro.sdl.tabra.ServerConnector.ServerConnector;

import static jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity.PARAM_THEME_ID;
import static jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity.PARAM_USER_NAME;

public class ThemeListActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = ThemeListActivity.class.getSimpleName();

    private ListView mThemeListView;
    private ArrayAdapter<ThemeListItem> mListAdapter;

    private UserDataController mUserDataCtrl;
    private ThemeDataController mThemeDataCtrl;

    private String mUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_list);

        mThemeListView = (ListView)findViewById(R.id.theme_listview);
        mThemeListView.setOnItemClickListener(this);

        mUserDataCtrl = new UserDataController(this);
        mThemeDataCtrl = new ThemeDataController(this);

        this.init();

        PlayGround pg = new PlayGround();
        pg.testGET();
    }

    private void init() {
        String user = mUserDataCtrl.getExistUser();
        if(user == null || user.isEmpty()){
            this.showRegisterUser();
        }else{
            mUserName = user;
            this.setupThemeList();
        }
    }

    private void showRegisterUser() {
        final EditText edtUN = new EditText(ThemeListActivity.this);
        new AlertDialog.Builder(ThemeListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("使用するユーザー名を入力してください")
                .setView(edtUN)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = edtUN.getText().toString();
                        registerUserName(userName);
                    }
                }).show();
    }

    private void registerUserName(String unStr){
        User u = new User(unStr);
        mUserDataCtrl.createUser(u);
        mUserName = unStr;
        setupThemeList();
    }

    private void setupThemeList() {
        Log.d(TAG, mUserName);
        this.setTitle(mUserName + "さんのアクティビティ");
        List<ThemeListItem> themeLists = this.createThemeListItems();
        mListAdapter = new ThemeListAdapter(this, themeLists);
        this.mThemeListView.setAdapter(mListAdapter);
    }

    private void showCreateTheme(){
        final PopupWindow pw = new PopupWindow(ThemeListActivity.this);
        final View popupView = getLayoutInflater().inflate(R.layout.popup_create_theme, null);
        popupView.findViewById(R.id.theme_create_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pw.isShowing()) {
                    createTheme(popupView);
                    pw.dismiss();
                }
            }
        });

        pw.setContentView(popupView);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_background));

        pw.setOutsideTouchable(true);
        pw.setFocusable(true);

        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
        pw.setWindowLayoutMode((int) width, WindowManager.LayoutParams.WRAP_CONTENT);
        pw.setWidth((int) width);
        pw.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        pw.showAtLocation(findViewById(R.id.theme_listview), Gravity.CENTER, 0, 0);
    }

    private void createTheme(View v){
        String title = ((EditText)v.findViewById(R.id.theme_create_input_title)).getText().toString();
        String overview = ((EditText)v.findViewById(R.id.theme_create_input_overview)).getText().toString();
        Theme t = new Theme(title, overview);
        mThemeDataCtrl.createTheme(t);

        setupThemeList();
    }

    private List<ThemeListItem> createThemeListItems(){
        List<ThemeListItem> list = new ArrayList<ThemeListItem>();

        list.add(new ThemeListItem(-1, "＋"));

        List<Theme> themeList =  mThemeDataCtrl.getAllThemes();
        for(Theme t : themeList){
            list.add(new ThemeListItem(t.getId(), t.getTitle()));
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView) parent;

        Log.d(TAG, "position: " + position);
        if(position == 0){
            showCreateTheme();
            return;
        }

        ThemeListItem t = mListAdapter.getItem(position);

        Intent intent = new Intent(this, BrainStormMainActivity.class);
        intent.putExtra(PARAM_THEME_ID, t.getId());
        intent.putExtra(PARAM_USER_NAME, mUserName);
        startActivity(intent);
    }

    class PlayGround implements ServerConnector.ServerConnectorDelegate {
        private final String TAG = PlayGround.class.getSimpleName();

        private ServerConnector sc;

        public PlayGround() {
            this.sc = new ServerConnector(this);
        }

        public void testGET() {
            sc.execute(ServerConnector.USERS, "", ServerConnector.POST, "type=get_theme_ids&name=aaa");
        }

        @Override
        public void recieveResponse(String responseStr) {
            Log.d(TAG, "response: " + responseStr);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_theme_list, menu);
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
}
