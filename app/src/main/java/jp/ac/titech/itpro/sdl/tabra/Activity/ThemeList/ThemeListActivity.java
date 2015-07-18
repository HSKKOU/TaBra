package jp.ac.titech.itpro.sdl.tabra.Activity.ThemeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.tabra.R;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.ThemeDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Controller.UserDataController;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.Theme;
import jp.ac.titech.itpro.sdl.tabra.SQLite.Model.User;

public class ThemeListActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = ThemeListActivity.class.getSimpleName();

    private ListView mThemeListView;

    private UserDataController mUserDataCtrl;
    private ThemeDataController mThemeDataCtrl;

    private String mUserName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_list);

        mThemeListView = (ListView)findViewById(R.id.theme_listview);
        mThemeListView.setOnItemClickListener(this);
        mThemeListView.setOnItemLongClickListener(this);

        mUserDataCtrl = new UserDataController(this);
        mThemeDataCtrl = new ThemeDataController(this);

        this.init();
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
        final EditText edt = new EditText(ThemeListActivity.this);
        new AlertDialog.Builder(ThemeListActivity.this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("使用するユーザー名を入力してください")
                .setView(edt)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userName = edt.getText().toString();
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
        this.mThemeListView.setAdapter(new ThemeListAdapter(this, themeLists));
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

        Intent intent = new Intent();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ListView listView = (ListView)parent;

        return false;
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
