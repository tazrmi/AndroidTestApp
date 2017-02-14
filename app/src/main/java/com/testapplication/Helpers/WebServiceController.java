package com.testapplication.Helpers;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.testapplication.BlogDetails;
import com.testapplication.BlogList;
import com.testapplication.LoginActivity;
import com.testapplication.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that communicates with the web services. Used in this class is the Vollet HTTP library.
 */

public class WebServiceController {

    //login of the user to get the token; POST method with parameters in the body method in order to send a raw request
    public static void loginUser (final Context context, final LoginActivity activity){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://blogsdemo.creitiveapps.com:16427/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObjectLogin = new JSONObject(response);
                            activity.tokenRecieved(jsonObjectLogin);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
                public byte[] getBody() throws com.android.volley.AuthFailureError {
                String str = "{\"email\":\""+ User.getInstance().getEmail()+"\",\"password\":\""+User.getInstance().getPassword()+"\"}";
                return str.getBytes();
            }

            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Client-Platform", "Android");
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    //getting the JSON file containing the list of blogs;
    // GET method with the token in the header of the request
    public static void getBlogs (final Context context, final BlogList activity){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="http://blogsdemo.creitiveapps.com:16427/blogs";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonBlogs = new JSONArray(response);
                            activity.setUp(JSONParser.returnBlogs(jsonBlogs));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context, "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Client-Platform", "Android");
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("X-Authorize", User.getInstance().getToken());
                return params;
            }
        };

        queue.add(stringRequest);
    }

    //getting the JSON file containing the html content to be shown as the blog details;
    // GET method with the token in the header of the request
    public static void getBlogDetails(final Context applicationContext, final BlogDetails blogDetails, int id) {
        RequestQueue queue = Volley.newRequestQueue(applicationContext);
        String url ="http://blogsdemo.creitiveapps.com:16427/blogs/" + id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject content = new JSONObject(response.replace("\\n", ""));
                            blogDetails.setUp(content.getString("content"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(applicationContext, "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(applicationContext, "There has been a mistake. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Client-Platform", "Android");
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("X-Authorize", User.getInstance().getToken());
                return params;
            }
        };

        queue.add(stringRequest);
    }
}
