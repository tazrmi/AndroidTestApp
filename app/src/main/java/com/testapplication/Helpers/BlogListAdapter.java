package com.testapplication.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.testapplication.Model.Blog;
import com.testapplication.R;

import java.io.InputStream;
import java.util.List;

/**
 * Class used as an ListView adapter that shows the blog data that has been recieved. The adapter uses the blog_list_view_item
 * layouts to populate the ListView
 */

public class BlogListAdapter extends BaseAdapter{

    List<Blog> blogs;
    Context context;

    public BlogListAdapter (List<Blog> blogs, Context context){
        this.blogs = blogs;
        this.context = context;
    }

    @Override
    public int getCount() {
        return blogs.size();
    }

    @Override
    public Object getItem(int i) {
        return blogs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Blog blog = blogs.get(i);
        View blogItemView = LayoutInflater.from(context).inflate(R.layout.blog_list_view_item, null, false);

        TextView blogTitle = (TextView) blogItemView.findViewById(R.id.blogItemTitle);
        TextView blogDescription = (TextView) blogItemView.findViewById(R.id.blogItemDescription);
        ImageView blogImage = (ImageView) blogItemView.findViewById(R.id.blogItemImage);
        blogTitle.setText(blog.getTitle());
        blogDescription.setText(Html.fromHtml(blog.getDescription()));

        new DownloadImageTask(blogImage)
                .execute(blog.getImage());

        return blogItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}


