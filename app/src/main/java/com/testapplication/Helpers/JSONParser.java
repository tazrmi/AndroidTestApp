package com.testapplication.Helpers;

import com.testapplication.Model.Blog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tazrmi on 2/12/2017.
 */

public class JSONParser {

    //populates the list of blogs with the data recieved from the webservice
    public static List<Blog> returnBlogs (JSONArray array) throws JSONException {
        List<Blog> blogs = new ArrayList<>();

        for (int i=0; i<array.length(); i++){
            JSONObject jsonBlog = array.getJSONObject(i);
            Blog blog = new Blog();
            blog.setId(jsonBlog.getInt("id"));
            blog.setTitle(jsonBlog.getString("title"));
            blog.setDescription(jsonBlog.getString("description"));
            blog.setImage(jsonBlog.getString("image_url"));
            blogs.add(blog);
        }
        return blogs;
    }
}
