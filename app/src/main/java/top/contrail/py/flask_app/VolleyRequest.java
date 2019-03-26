package top.contrail.py.flask_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

class VolleyRequest {

    private static final String TAG = "Volley";

    private JSONObject result_json;
    private String base_url;
    private RequestQueue queue;
    private String auth;
    private JSONObject data = new JSONObject();

    private Handler handler = null;
    private boolean has_handler = false;

    VolleyRequest(){

        this.result_json = null;

    }

    void set_parameter(String url, RequestQueue queue, String auth){

        this.base_url = url;
        this.queue = queue;
        this.auth = auth;

    }

    void set_parameter(String url, RequestQueue queue, String auth, JSONObject data){

        this.base_url = url;
        this.queue = queue;
        this.auth = auth;
        this.data = data;

    }

    void set_parameter(String url, RequestQueue queue, String auth, Handler handler){

        this.base_url = url;
        this.queue = queue;
        this.auth = auth;
        this.handler = handler;
        this.has_handler = true;
    }

    void set_parameter(String url, RequestQueue queue, String auth, JSONObject data, Handler handler){

        this.base_url = url;
        this.queue = queue;
        this.auth = auth;
        this.data = data;
        this.handler = handler;
        this.has_handler = true;
    }

    VolleyRequest send_get_request(){

        String url = this.base_url;
        final VolleyRequest this_task = this;
        final String authorization = this.auth;

        boolean send = false;
        if (this.has_handler){
            send = true;
        }
        final boolean send_msg = send;
        final Handler handler_msg = this.handler;

        Uri.Builder builder = Uri.parse(url).buildUpon();
        String paramUrl=builder.build().toString();
        Log.d(TAG, paramUrl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, paramUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        this_task.result_json = response;

                        if (send_msg){
                            Message message=new Message();
                            message.what=1;
                            message.obj=response;
                            handler_msg.sendMessage(message);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("OnErrorResponse", error.toString());
                        JSONObject response = new JSONObject();
                        try{
                            response.put("state", "failed");
                        }catch (JSONException e){
                            Log.e(TAG, e.toString());
                        }

                        this_task.result_json = response;

                        if (send_msg){
                            Message message=new Message();
                            message.what=1;
                            message.obj=response;
                            handler_msg.sendMessage(message);
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", String.format("Basic %s", authorization));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                Log.d(TAG, headers.toString());

                return headers;
            }
        };

        queue.add(jsonObjectRequest);

        return this_task;

    }

    VolleyRequest send_post_request(){

        String url = this.base_url;
        final VolleyRequest this_task = this;
        final String authorization = this.auth;

        boolean send = false;
        if (this.has_handler){
            send = true;
        }
        final boolean send_msg = send;
        final Handler handler_msg = this.handler;

        Uri.Builder builder = Uri.parse(url).buildUpon();
        String paramUrl=builder.build().toString();
        Log.d(TAG, paramUrl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, paramUrl, this.data, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        this_task.result_json = response;

                        if (send_msg){
                            Message message=new Message();
                            message.what=1;
                            message.obj=response;
                            handler_msg.sendMessage(message);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("OnErrorResponse", error.toString());
                        JSONObject response = new JSONObject();
                        try{
                            response.put("state", "failed");
                        }catch (JSONException e){
                            Log.e(TAG, e.toString());
                        }

                        this_task.result_json = response;

                        if (send_msg){
                            Message message=new Message();
                            message.what=1;
                            message.obj=response;
                            handler_msg.sendMessage(message);
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", String.format("Basic %s", authorization));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                Log.d(TAG, headers.toString());

                return headers;
            }
        };

        queue.add(jsonObjectRequest);

        return this_task;
    }

    JSONObject get_response(){
        return this.result_json;
    }
}
