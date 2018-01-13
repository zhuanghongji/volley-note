package com.zhuanghongji.volley.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.zhuanghongji.volley.sample.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStringRequestGetTest(View view) {
        String url = "https://www.baidu.com";
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        };
        StringRequest req = new StringRequest(url, listener, errorListener);

        VolleySingleton.getInstance(this).addToRequestQueue(req);

        // post
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("param1", "value1");
                params.put("param2", "value2");
                params.put("param2", "value3");
                return params;
            }
        };
    }


    public void onJsonObjectRequestPostTest(View view) {
        String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonObjectRequest";
        // String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonArrayRequest";
        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "response = " + response.toString());
                try {
                    JSONArray array = (JSONArray) response.get("students");
                    for (int i = 0, length = array.length(); i < length; i++) {
                        JSONObject student = (JSONObject) array.get(i);
                        Log.d(TAG, "student name = " + student.get("name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void onJsonArrayRequestPostTest(View view) {
        // String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonObjectRequest";
        String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonArrayRequest";
        JsonArrayRequest request = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "response = " + response.toString());
                try {
                    for (int i = 0, length = response.length(); i < length; i++) {
                        JSONObject student = (JSONObject) response.get(i);
                        Log.d(TAG, "student scope = " + student.get("scope"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.getMessage(), error);
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


}
