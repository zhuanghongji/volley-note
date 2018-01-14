package com.zhuanghongji.volley.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.zhuanghongji.volley.sample.volley.GsonRequest;
import com.zhuanghongji.volley.sample.volley.VolleySingleton;
import com.zhuanghongji.volley.sample.volley.XmlRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStringRequestTest(View view) {
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


    public void onJsonObjectRequestTest(View view) {
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

    public void onJsonArrayRequestTest(View view) {
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


    public void onImageRequestTest(View view) {
        String tag = view.getTag().toString();
        String url = "RIGHT".equals(tag) ?
                "http://www.wanandroid.com/resources/image/pc/logo.png" : "http://ERROR.png";
        final ImageView imageView = findViewById(R.id.imageView);
        ImageRequest request = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.red);
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


    public void onImageLoaderTest(View view) {
        final ImageView imageView = findViewById(R.id.imageView);
        String url = "http://www.wanandroid.com/resources/image/pc/logo.png";
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(
                imageView, R.drawable.green, R.drawable.red);
        VolleySingleton.getInstance(this).getImageLoader().get(
                url, listener, 200, 200);
    }

    public void onNetworkImageViewTest(View view) {
        String url = "https://www.baidu.com/img/bd_logo1.png"; // 百度首页 Logo
        NetworkImageView networkImageView = findViewById(R.id.networkImageView);
        networkImageView.setDefaultImageResId(R.drawable.green);
        networkImageView.setErrorImageResId(R.drawable.red);

        ImageLoader loader = VolleySingleton.getInstance(this).getImageLoader();
        networkImageView.setImageUrl(url, loader);
    }


    public void onGsonRequestTest(View view) {
        String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiGsonRequest";
        GsonRequest<Weather> request = new GsonRequest<>(url, Weather.class, null,
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        Log.d(TAG, "response = " + weather.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.getMessage(), error);
                    }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public void onXmlRequestTest(View view) {
        String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiXmlRequest";
        XmlRequest request = new XmlRequest(url, new Response.Listener<XmlPullParser>() {
            @Override
            public void onResponse(XmlPullParser response) {
                try {
                    int eventType = response.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                String nodeName = response.getName();
                                if ("city".equals(nodeName)) {
                                    String quName = response.getAttributeValue(0);
                                    Log.d(TAG, "quName is " + quName);
                                }
                                break;
                        }
                        eventType = response.next();
                    }
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
