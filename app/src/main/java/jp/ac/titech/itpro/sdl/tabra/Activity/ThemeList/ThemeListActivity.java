package jp.ac.titech.itpro.sdl.tabra.Activity.ThemeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity;
import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.ThemeDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.UserDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Theme;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.User;
import jp.ac.titech.itpro.sdl.tabra.ServerConnector.ServerConnector;

import static jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity.PARAM_SERVER_THEME_ID;
import static jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity.PARAM_THEME_ID;
import static jp.ac.titech.itpro.sdl.tabra.Activity.BrainStorming.BrainStormMainActivity.PARAM_USER_NAME;

public class ThemeListActivity extends Activity implements AdapterView.OnItemClickListener, ServerConnector.ServerConnectorDelegate, AdapterView.OnItemLongClickListener {
    private static final String TAG = ThemeListActivity.class.getSimpleName();

    private ListView mThemeListView;
    private ArrayAdapter<ThemeListItem> mListAdapter;

    private UserDataController mUserDataCtrl;
    private ThemeDataController mThemeDataCtrl;

    private String mUserId = "";
    private String mUserName = "";
    private String mThemeIds = "";

    private static final String CREATE_USER = "CREATE_USER";
    private static final String CREATE_THEME = "CREATE_THEME";
    private static final String LOGIN = "LOGIN";
    private static final String GET_THEMES = "GET_THEMES";
    private static final String INVITE_USER = "INVITE_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_list);

        mThemeListView = (ListView)findViewById(R.id.theme_listview);
        mThemeListView.setOnItemClickListener(this);
        mThemeListView.setOnItemLongClickListener(this);

        mUserDataCtrl = new UserDataController(this);
        mThemeDataCtrl = new ThemeDataController(this);

        this.showLoginView();
//        this.init();
    }

    private void showLoginView() {
        final LinearLayout layout = new LinearLayout(ThemeListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText edtUN = new EditText(ThemeListActivity.this);
        edtUN.setHint("ユーザ名");
        final EditText edtPW = new EditText(ThemeListActivity.this);
        edtPW.setHint("パスワード");
        layout.addView(edtUN);
        layout.addView(edtPW);
        new AlertDialog.Builder(ThemeListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ログイン：ユーザ名とパスワードを入力してください")
                .setView(layout)
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = edtUN.getText().toString();
                        String password = edtPW.getText().toString();
                        login(userName, password);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void login(String un, String pw) {
        (new ServerConnector(this)).execute(LOGIN, ServerConnector.USERS, "", ServerConnector.POST, "type=get_theme_ids&name=" + un + "&passwd=" + pw);
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

        (new ServerConnector(this)).execute(CREATE_THEME, ServerConnector.THEMES, "", ServerConnector.POST, "type=create_theme&title=" + title + "&overview=" + overview + "&creator_id=" + mUserId);
    }

    private List<ThemeListItem> createThemeListItems(){
        List<ThemeListItem> list = new ArrayList<ThemeListItem>();

        list.add(new ThemeListItem(-1, "＋"));

        if(isConnectedInternet(this)){
            mThemeDataCtrl.deleteAll();
            (new ServerConnector(this)).execute(GET_THEMES, ServerConnector.THEMES, "", ServerConnector.POST, "type=get_themes&theme_ids=" + mThemeIds);
            return list;
        }

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
        intent.putExtra(PARAM_SERVER_THEME_ID, t.getServer_id());
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isConnectedInternet(this)){return false;}
        ListView listView = (ListView) parent;

        Log.d(TAG, "position: " + position);
        if(position == 0){
            return true;
        }

        ThemeListItem t = mListAdapter.getItem(position);

        this.showInviteView(t.getServer_id());

        return true;
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
            if(CREATE_USER.equals(serverConnectorId)){
                finishCreateUser(json);
            }else if(CREATE_THEME.equals(serverConnectorId)){
                finishCreateTheme(json);
            }else if(LOGIN.equals(serverConnectorId)){
                finishLogin(json);
            }else if(GET_THEMES.equals(serverConnectorId)){
                finishGetThemes(json);
            }else if(INVITE_USER.equals(serverConnectorId)){

            }
        }catch(JSONException e){
            Log.d(TAG, "json parse error");
        }
    }

    private void finishCreateUser(JSONObject json) throws JSONException {
        String un = json.getString("name");
        String pw = json.getString("passwd");
        mUserId = json.getString("user_id");
        User u = new User(un, pw);
        mUserDataCtrl.createUser(u);
        mUserName = un;
        setupThemeList();
    }

    private void finishCreateTheme(JSONObject json) throws JSONException {
        String title = json.getString("title");
        String overview = json.getString("overview");
        String themeId = json.getString("theme_id");
        Theme t = new Theme(title, overview);
        mThemeDataCtrl.createTheme(t);

        if("".equals(mThemeIds)){
            mThemeIds = themeId;
        }else{
            mThemeIds += ("," + themeId);
        }
        setupThemeList();
    }

    private void finishLogin(JSONObject json) throws JSONException {
        mUserId = json.getString("user_id");
        mUserName = json.getString("name");
        mThemeIds = json.getString("theme_ids");
        setupThemeList();
    }

    private void finishGetThemes(JSONObject json) throws JSONException {
        JSONArray ja = json.getJSONArray("themes");
        for(int i=0; i<ja.length(); i++){
            JSONObject jo = ja.getJSONObject(i);
            Theme t = new Theme(
                    jo.getString("title"),
                    jo.getString("overview")
            );
            mThemeDataCtrl.createTheme(t);
        }

        List<ThemeListItem> list = new ArrayList<ThemeListItem>();
        list.add(new ThemeListItem(-1, "＋"));
        List<Theme> themeList =  mThemeDataCtrl.getAllThemes();
        for(int i=0; i<themeList.size(); i++){
            Theme t = themeList.get(i);
            ThemeListItem tli = new ThemeListItem(t.getId(), t.getTitle());
            JSONObject jo = ja.getJSONObject(i);
            tli.setServer_id(jo.getString("id"));
            list.add(tli);
        }
        mListAdapter = new ThemeListAdapter(this, list);
        mThemeListView.setAdapter(mListAdapter);
    }

    class PlayGround implements ServerConnector.ServerConnectorDelegate {
        private final String TAG = PlayGround.class.getSimpleName();

        private ServerConnector sc;

        public PlayGround() {
            this.sc = new ServerConnector(this);
        }

        public void testGET() {
            sc.execute("PlayGround", ServerConnector.USERS, "", ServerConnector.POST, "type=get_theme_ids&name=aaa");
        }

        @Override
        public void recieveResponse(String serverConnectorId, String responseStr) {
            Log.d(TAG, "ServerConnector ID: " + serverConnectorId);
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
        int id = item.getItemId();

        if (id == R.id.action_register) {
            if("".equals(mUserDataCtrl.getExistUser())){
                this.showRegisterUser();
            }
            return true;
        } else if(id == R.id.action_show_userID) {
            showUserIDView();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showRegisterUser() {
        final LinearLayout layout = new LinearLayout(ThemeListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText edtUN = new EditText(ThemeListActivity.this);
        edtUN.setHint("ユーザ名");
        final EditText edtPW = new EditText(ThemeListActivity.this);
        edtPW.setHint("パスワード");
        layout.addView(edtUN);
        layout.addView(edtPW);
        new AlertDialog.Builder(ThemeListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ユーザ登録：登録するユーザー名とパスワードを入力してください")
                .setView(layout)
                .setPositiveButton("Register", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = edtUN.getText().toString();
                        String password = edtPW.getText().toString();
                        registerUserName(userName, password);
                    }
                }).show();
    }

    private void registerUserName(String unStr, String passwd){
        (new ServerConnector(this)).execute(CREATE_USER, ServerConnector.USERS, "", ServerConnector.POST, "type=create_user&name=" + unStr + "&passwd=" + passwd);
    }

    private void showInviteView(final String server_id){
        final LinearLayout layout = new LinearLayout(ThemeListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText edtUI = new EditText(ThemeListActivity.this);
        edtUI.setHint("招待するユーザID");
        layout.addView(edtUI);
        new AlertDialog.Builder(ThemeListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("ユーザ招待")
                .setView(layout)
                .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userID = edtUI.getText().toString();
                        inviteUser(userID, server_id);
                    }
                }).show();
    }

    private void inviteUser(String ui, String si) {
        (new ServerConnector(this)).execute(INVITE_USER, ServerConnector.USERS, "", ServerConnector.POST, "type=invite_user&userId=" + ui + "&themeId=" + si);
    }

    private void showUserIDView() {
        final LinearLayout layout = new LinearLayout(ThemeListActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final TextView userIDView = new TextView(ThemeListActivity.this);
        userIDView.setWidth(300);
        userIDView.setHeight(100);
        userIDView.setTextSize(20);
        userIDView.setGravity(Gravity.CENTER);
        userIDView.setText(mUserId);
        userIDView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        layout.addView(userIDView);
        new AlertDialog.Builder(ThemeListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("あなたのユーザIDです")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private boolean isConnectedInternet(Context context) {
        ConnectivityManager cm =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if( info != null ){
            return info.isConnected();
        } else {
            return false;
        }
    }

}
