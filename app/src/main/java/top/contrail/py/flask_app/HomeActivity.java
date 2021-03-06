package top.contrail.py.flask_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class HomeActivity extends AppCompatActivity {

    Button get=null;
    Button data_post=null;
    Button data_get=null;
    EditText data_edit=null;
    TextView content=null;
    private static final String TAG = "HomeActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.username:
                String username = MyApplication.getInstance().getUsername();
                Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                MyApplication.getInstance().setToken("");
                MyApplication.getInstance().setUsername("");

                Intent intent = new Intent();
                intent.setClass(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        get = (Button) findViewById(R.id.login_test);
        data_post = (Button) findViewById(R.id.data_post);
        data_get = (Button) findViewById(R.id.data_get);
        content=(TextView) findViewById(R.id.test_content);
        data_edit=(EditText) findViewById(R.id.dataEdit);

        final RequestQueue requestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();

        VolleyLog.DEBUG = true;

        final Handler login_test_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                JSONObject response = (JSONObject) msg.obj;

                String message = "";
                try{
                    message = response.getString("message");
                }catch (JSONException e){
                    Log.d(TAG, "Can't get the message. " + e.toString());
                }

//                content.setText(message);
                Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();

            }
        };

        final Handler data_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                JSONObject response = (JSONObject) msg.obj;

                String lines = "";
                Iterator iterator = response.keys();

                String key;
                String value = "";
                String line = "";
                String state = "failed";
                try{

                    while(iterator.hasNext()) {
                        key = (String) iterator.next();
                        value = response.getString(key);
                        line = String.format("%s: %s\n", key, value);
                        Log.d(TAG, line);
                        lines = lines.concat(line);
                    }

                    state = response.getString("state");
                    MyApplication.getInstance().setDataUrl(response.getString("url"));

                }catch (JSONException e){
                    Log.e(TAG, e.toString());
                }


                content.setText(lines);
                Toast.makeText(HomeActivity.this, state, Toast.LENGTH_SHORT).show();

            }
        };

        get.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                String url = getResources().getString(R.string.URL_TEST);
                String auth =  MyApplication.getInstance().getToken();
                Log.d(TAG, auth);

                VolleyRequest request = new VolleyRequest();
                request.set_parameter(url, requestQueue, auth, login_test_handler);

                VolleyRequest result_task = request.send_get_request();

            }

        });

        data_post.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                String url = getResources().getString(R.string.URL_DATA);
                String auth =  MyApplication.getInstance().getToken();
                Log.d(TAG, auth);

                String text = data_edit.getText().toString();
                JSONObject data = new JSONObject();
                try{
                    data.put("body", text);
                }catch (JSONException e){
                    Log.e(TAG, "Set post data error.");
                    Log.e(TAG, e.toString());
                }
                Log.d(TAG, data.toString());

                VolleyRequest request = new VolleyRequest();
                request.set_parameter(url, requestQueue, auth, data, data_handler);

                VolleyRequest result_task = request.send_post_request();

            }

        });

        data_get.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {

                String url_base = getResources().getString(R.string.URL_BASE);
                String data_url = MyApplication.getInstance().getDataUrl();

                String url = String.format("%s%s", url_base, data_url);
                Log.d(TAG, url);

                String auth =  MyApplication.getInstance().getToken();
                Log.d(TAG, auth);

                VolleyRequest request = new VolleyRequest();
                request.set_parameter(url, requestQueue, auth, data_handler);

                VolleyRequest result_task = request.send_get_request();

            }

        });

//        Button logoutButton = (Button) findViewById(R.id.logout);
//        logoutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MyApplication.getInstance().setToken("");
//                MyApplication.getInstance().setUsername("");
//
//                Intent intent = new Intent();
//                intent.setClass(HomeActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });

    }
}
