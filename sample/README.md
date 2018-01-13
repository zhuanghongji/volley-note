# 示例分析
Android 系统中主要提供了两种方式来进行 HTTP 通信，`HttpURLConnection`和 `HttpClient`，几乎在任何项目的代码中我们都能看到这两个类的身影，使用率非常高。

# 请求队列的单例实现
大致代码如下
```java
public class VolleySingleton {

    private static Context sContext;
    private static VolleySingleton sInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private VolleySingleton(Context context) {
        sContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new VolleySingleton(context);
        }
        return sInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(sContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
```

# StringRequest
构造并发送请求
```java
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
```
返回结果如下图所示

![](./image/StringRequestLog.png)

上述 `StringRequest` 的构造方法构造的其实是一个 `GET` 请求，使用另外一个构造方法可以构造 `POST` 或其它类型的请求，比如：
```java
StringRequest postRequest = new StringRequest(Request.Method.POST, url, listener, errorListener) {
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        params.put("param1", "value1");
        params.put("param2", "value2");
        params.put("param2", "value3");
        return params
    }
};
```

# JsonRequest
JsonRequest 是是一个抽象类，无法直接创建它的示例。但可以从它的两个直接子类 `JsonObjectRequest` 和 `JsonArrayRequest` 入手，用法都和 `StringRequest` 类似。

在编写测试代码前，写介绍个工具：[MockApi](http://wanandroid.com/tools/mockapi)  
功能如网页所述：支持生成一个访问链接，返回任何文本数据：JSON，XML等...

## JsonObjectRequest
现在我们在 MockApi 上生成一个访问链接  [http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonObjectRequest](http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonObjectRequest)  
>如果现在该链接失效的话，你可以自己使用该工具返回的你指定的数据。

链接返回内容为：
```JSON
{
  "students" : [
    {
      "id" : "3",
      "name" : "张三",
      "scope" : "93"
    },
    {
      "id" : "4",
      "name" : "李四",
      "scope" : "94"
    },
    {
      "id" : "5",
      "name" : "王五",
      "scope" : "95"
    },
    {
      "id" : "6",
      "name" : "赵六",
      "scope" : "96"
    },
    {
    "id" : "7",
    "name" : "孙七",
    "scope" : "97"
    }
  ]
}
```

示例代码和请求响应打印的日志如下：
```java
String url = "http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonObjectRequest";
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
```
```
com.zhuanghongji.volley.sample D/MainActivity: response = {"students":[{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}]}
com.zhuanghongji.volley.sample D/MainActivity: student name = 张三
com.zhuanghongji.volley.sample D/MainActivity: student name = 李四
com.zhuanghongji.volley.sample D/MainActivity: student name = 王五
com.zhuanghongji.volley.sample D/MainActivity: student name = 赵六
com.zhuanghongji.volley.sample D/MainActivity: student name = 孙七
```

## JsonArrayRequest
现在我们在 MockApi 上生成另一个访问链接  [http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonArrayRequest](http://wanandroid.com/tools/mockapi/1921/zhuanghongjiJsonArrayRequest)  

链接返回内容为：
```JSON
[
  {
    "id": "3",
    "name": "张三",
    "scope": "93"
  },
  {
    "id": "4",
    "name": "李四",
    "scope": "94"
  },
  {
    "id": "5",
    "name": "王五",
    "scope": "95"
  },
  {
    "id": "6",
    "name": "赵六",
    "scope": "96"
  },
  {
    "id": "7",
    "name": "孙七",
    "scope": "97"
  }
]
```

示例代码和请求响应打印的日志如下：
```java
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
```
```
com.zhuanghongji.volley.sample D/MainActivity: response = [{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}]
com.zhuanghongji.volley.sample D/MainActivity: student scope = 93
com.zhuanghongji.volley.sample D/MainActivity: student scope = 94
com.zhuanghongji.volley.sample D/MainActivity: student scope = 95
com.zhuanghongji.volley.sample D/MainActivity: student scope = 96
com.zhuanghongji.volley.sample D/MainActivity: student scope = 97
```

## 两个子类的不同点
`JsonObjectRequest` 与 `JsonArrayRequest` 的不同点在于，前者需要返回结果是 ”数组结构的Json数据“，而后者 “单个结构的Json数据”。

现在我们将上面两个示例的 url 调换一下进行测试。

### `JsonObjectRequest` 去请求 ”数组结构的Json数据“
Volley 解析出现异常，回调 `onErrorResponse()` 方法，打印日志如下:
```
com.zhuanghongji.volley.sample E/MainActivity: org.json.JSONException: Value [{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}] of type org.json.JSONArray cannot be converted to JSONObject
                                               com.android.volley.ParseError: org.json.JSONException: Value [{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}] of type org.json.JSONArray cannot be converted to JSONObject
                                                   at com.android.volley.toolbox.JsonObjectRequest.parseNetworkResponse(JsonObjectRequest.java:73)
                                                   at com.android.volley.NetworkDispatcher.processRequest(NetworkDispatcher.java:132)
                                                   at com.android.volley.NetworkDispatcher.run(NetworkDispatcher.java:87)
                                                Caused by: org.json.JSONException: Value [{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}] of type org.json.JSONArray cannot be converted to JSONObject
                                                   at org.json.JSON.typeMismatch(JSON.java:111)
                                                   at org.json.JSONObject.<init>(JSONObject.java:163)
                                                   at org.json.JSONObject.<init>(JSONObject.java:176)
                                                   at com.android.volley.toolbox.JsonObjectRequest.parseNetworkResponse(JsonObjectRequest.java:68)
                                                   at com.android.volley.NetworkDispatcher.processRequest(NetworkDispatcher.java:132) 
                                                   at com.android.volley.NetworkDispatcher.run(NetworkDispatcher.java:87) Â
```

### `JsonArrayRequest` 去请求 ”单一结构的Json数据“
Volley 同样会出现解析异常，回调 `onErrorResponse()` 方法，打印日志如下：
```
com.zhuanghongji.volley.sample E/MainActivity: org.json.JSONException: Value {"students":[{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}]} of type org.json.JSONObject cannot be converted to JSONArray
                                               com.android.volley.ParseError: org.json.JSONException: Value {"students":[{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}]} of type org.json.JSONObject cannot be converted to JSONArray
                                                   at com.android.volley.toolbox.JsonArrayRequest.parseNetworkResponse(JsonArrayRequest.java:70)
                                                   at com.android.volley.NetworkDispatcher.processRequest(NetworkDispatcher.java:132)
                                                   at com.android.volley.NetworkDispatcher.run(NetworkDispatcher.java:87)
                                                Caused by: org.json.JSONException: Value {"students":[{"id":"3","name":"张三","scope":"93"},{"id":"4","name":"李四","scope":"94"},{"id":"5","name":"王五","scope":"95"},{"id":"6","name":"赵六","scope":"96"},{"id":"7","name":"孙七","scope":"97"}]} of type org.json.JSONObject cannot be converted to JSONArray
                                                   at org.json.JSON.typeMismatch(JSON.java:111)
                                                   at org.json.JSONArray.<init>(JSONArray.java:96)
                                                   at org.json.JSONArray.<init>(JSONArray.java:108)
                                                   at com.android.volley.toolbox.JsonArrayRequest.parseNetworkResponse(JsonArrayRequest.java:65)
                                                   at com.android.volley.NetworkDispatcher.processRequest(NetworkDispatcher.java:132) 
                                                   at com.android.volley.NetworkDispatcher.run(NetworkDispatcher.java:87) 
```









.....
