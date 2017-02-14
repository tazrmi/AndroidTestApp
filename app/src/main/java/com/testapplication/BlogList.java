package com.testapplication;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.testapplication.Helpers.BlogListAdapter;
import com.testapplication.Helpers.ConnectionHelper;
import com.testapplication.Helpers.NetworkStateReceiver;
import com.testapplication.Helpers.WebServiceController;
import com.testapplication.Model.Blog;

import java.util.ArrayList;
import java.util.List;

/**
 * A screen that shows the list of blogs.
 */

public class BlogList extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    List<Blog> listOfBlogs = new ArrayList<>();
    ListView listView;

    private NetworkStateReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_list);

        receiver = new NetworkStateReceiver();

        //Checking for an Internet connection. If the connection is not available a reciever is registered,
        // so the activity will restart after the connection has been established

        if (!ConnectionHelper.isNetworkAvailable(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet connection was detected. Please connect to the Internet and try again.", Toast.LENGTH_SHORT).show();
            receiver.addListener(BlogList.this);
            this.registerReceiver(receiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        }
        else {
            listView = (ListView) findViewById(R.id.list);
            WebServiceController.getBlogs(getApplicationContext(), this);
            try {
                this.unregisterReceiver(receiver);
            }
            catch (Exception e){

            }
        }
    }

    //Recieving the data from the web service, setting the list adapter with the data recieved
    public void setUp(final List<Blog> blogs) {
       listOfBlogs = blogs;
       listView.setAdapter(new BlogListAdapter(listOfBlogs, getApplicationContext()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent blogListDetailsIntent = new Intent(BlogList.this, BlogDetails.class);
                blogListDetailsIntent.putExtra("id", blogs.get(i).getId());
                startActivity(blogListDetailsIntent);
            }
        });
    }

    @Override
    public void networkAvailable() {
       finish();
       startActivity(getIntent());
    }

    @Override
    public void networkUnavailable() {
        Toast.makeText(getApplicationContext(), "No Internet connection was detected. Please connect to the Internet and try again.", Toast.LENGTH_SHORT).show();
    }
}
