package top.contrail.py.flask_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

public class VolleyRequest {

    private static final String TAG = "Volley";

    private JSONObject result_json;
    private String base_url;
    private RequestQueue queue;
    private String auth;

    VolleyRequest(){

        this.result_json = null;

    }

    void set_parameter(String url, RequestQueue queue, String auth){

        this.base_url = url;
        this.queue = queue;
        this.auth = auth;

    }

    VolleyRequest send_get_request(){

        String url = this.base_url;
        final VolleyRequest this_task = this;
        final String authorization = this.auth;

        Uri.Builder builder = Uri.parse(url).buildUpon();
        String paramUrl=builder.build().toString();
        Log.d(TAG, paramUrl);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, paramUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        this_task.result_json = response;
//                        callback.onSuccessResponse(response, this_task);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("OnErrorResponse", error.toString());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
                headers.put("Authorization", String.format("Basic %s", authorization));
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "");
                Log.d(TAG, headers.toString());

                return headers;
            }
        };

        queue.add(jsonObjectRequest);

        return this_task;

    }

//    void send_post_request(final VolleyCallback callback){
//
//        String url = this.base_url;
//        final VolleyRequest this_task = this;
//
//        Uri.Builder builder = Uri.parse(url).buildUpon();
//        builder.appendQueryParameter("aid", put_aid);
//        String paramUrl=builder.build().toString();
//        Log.d(TAG, paramUrl);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, paramUrl, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d(TAG, response.toString());
//                        callback.onSuccessResponse(response, this_task);
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO: Handle error
//                        Log.e("OnErrorResponse", error.toString());
//                    }
//                });
//
//        queue.add(jsonObjectRequest);
//    }

    JSONObject get_response(){

        return this.result_json;
    }


}
