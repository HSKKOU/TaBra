package jp.ac.titech.itpro.sdl.tabra.ServerConnector;

/**
 * Created by hskk1120551 on 15/07/24.
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnector extends AsyncTask<String, Integer, String> {
    public interface ServerConnectorDelegate {
        void recieveResponse(String serverConnectorId, String responseStr);
    }

    public static final String GET = "GET";         // 取得
    public static final String PUT = "PUT";         // 更新
    public static final String POST = "POST";       // 追加
    public static final String DELETE = "DELETE";   // 削除

    private static final String HOST_PORT = "52.69.63.235:3000";
    private static final String API_PATH = "/api/v1";

    // TODO: fix https.
    private static final String API_BASE_URL = "http://" + HOST_PORT + API_PATH;

    private static String TAG = "ServerConnector";

    public static final String USERS = "users";
    public static final String THEMES = "themes";
    public static final String ITEMS = "items";
    public static final String CHAR_SET = "UTF-8";

    private ServerConnectorDelegate delegate;

    private String serverConnectorId;

    public ServerConnector(ServerConnectorDelegate delegate) {
        this.delegate = delegate;
    }

    private HttpURLConnection getConnection(String urlStr, String method, String queryStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = this.connectServer(url);
        con.setRequestMethod(method);
        if(!"".equals(method)){
            con.setDoInput(true);
            con.setDoOutput(true);
        }
        this.setDefaultHeader(con);

        if(!"".equals(method)){
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, CHAR_SET));
            writer.write(queryStr);
            writer.flush();
            writer.close();
            os.close();
        }

        con.connect();
        return con;
    }

    private BufferedReader getBufferedReader(HttpURLConnection con) throws IOException {
        InputStream conIS = con.getInputStream();
        return new BufferedReader(new InputStreamReader(conIS, CHAR_SET));
    }

    private String buf2str(BufferedReader br) throws IOException {
        String line = null;
        String message = "";
        while ((line = br.readLine()) != null) {
            message += line;
        }
        return message;
    }

    private void setDefaultHeader(HttpURLConnection con) {
        // set header's property
        con.setRequestProperty("Accept-Language", CHAR_SET);
    }

    private HttpURLConnection connectServer(URL _url) throws IOException {
        return (HttpURLConnection)_url.openConnection();
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d(TAG, "doInBackground");

        serverConnectorId = params[0];

        String typeStr = "/" + params[1];
        String idStr = params[2];

        if(!"".equals(idStr)){
            idStr = "/" + idStr;
        }
        String method = params[3];
        String queryStr = params[4];

        String urlStr = API_BASE_URL + typeStr + idStr;
        Log.d(TAG, urlStr + ", " + method + ", " + queryStr);

        try{
            HttpURLConnection con = this.getConnection(urlStr, method, queryStr);
            BufferedReader br = this.getBufferedReader(con);
            return buf2str(br);
        }catch(IOException e){
            Log.d(TAG, e.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if(s != null) Log.d(TAG, s);
        this.delegate.recieveResponse(serverConnectorId, s);
    }
}
