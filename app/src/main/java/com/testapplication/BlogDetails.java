package com.testapplication;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.Toast;

import com.testapplication.Helpers.ConnectionHelper;
import com.testapplication.Helpers.NetworkStateReceiver;
import com.testapplication.Helpers.WebServiceController;

/*
* A screen that shows the details of the selected blog.
 */

public class BlogDetails extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {

    WebView webView;
    int blogId;

    private NetworkStateReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        receiver = new NetworkStateReceiver();

        //Getting the number of the selected blog to show the details off from the previous activity
        blogId = getIntent().getIntExtra("id", 1);

        //Checking for an Internet connection. If the connection is not available a reciever is registered,
        // so the activity will restart after the connection has been established
        if (!ConnectionHelper.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "No Internet connection was detected. Please connect to the Internet and try again.", Toast.LENGTH_SHORT).show();
            this.registerReceiver(receiver, new IntentFilter(CONNECTIVITY_SERVICE));

        } else {
            setContentView(R.layout.activity_blog_details);
            webView = (WebView) findViewById(R.id.webView);
            WebServiceController.getBlogDetails(getApplicationContext(), this, blogId);
            try {
                this.unregisterReceiver(receiver);
            }
            catch (Exception e){

            }
        }
    }

    //Recieving the html data from the web service, setting the WebView
    public void setUp(String html) {
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
//        webView.setInitialScale(30);
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
