package top.contrail.py.flask_app;

import android.app.Application;
import android.util.Base64;
import android.util.Log;

public class MyApplication extends Application {

    private static MyApplication flask_app;
    private static final String TAG = "MyApp";

    public static MyApplication getInstance(){
        return flask_app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        flask_app = this;
    }

    private String username = "";
    private String token = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        Log.d(TAG, this.username);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {

        String auth_text = String.format("%s:", token);
        Log.d(TAG, auth_text);
        this.token = Base64.encodeToString(auth_text.getBytes(), Base64.NO_WRAP);
        Log.d(TAG, this.token);
    }

}