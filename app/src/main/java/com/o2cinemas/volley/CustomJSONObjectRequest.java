package com.o2cinemas.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 3/29/2016.
 */
public class CustomJSONObjectRequest extends JsonObjectRequest {

    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }



    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("application/json" ,"charset=utf-8");
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("X-API-KEY"," ZD22Asbujjj8jirwEwU9zopg/2deL7cgAZx6MTccZhnYq7CGuvSZXB4NJyszXQ8eD5K/qAPORkOR1LbxpOSAcA==");
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy

        return new DefaultRetryPolicy(60000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }
}

