package com.example.brian.orginder2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.UrlQuerySanitizer;

import java.net.URI;


public class MainActivity extends ActionBarActivity {

    public String da_url;

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri parsed_url = Uri.parse(url);
            Log.d("host",parsed_url.getHost());
            Log.d("path",parsed_url.getPath());
            Log.d("raw_url",url);
            if (!(parsed_url.getHost().equals("www.facebook.com") && parsed_url.getPath().equals("/connect/login_success.html"))) {
                view.loadUrl(url);
            }
            else
            {
                String at = url.split("#")[1].split("=")[1].split("&")[0];
                Log.d("objective",url);
                Log.d("at",at);
                Log.d("uid",da_url);
                view.loadUrl("file:///android_asset/tinder_api.html?access_token="+at+"&uid="+da_url);
            }
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        da_url = intent.getStringExtra("user_token");
        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("https://www.facebook.com/dialog/oauth?client_id=464891386855067&redirect_uri=https://www.facebook.com/connect/login_success.html&scope=basic_info,email,public_profile,user_about_me,user_activities,user_birthday,user_education_history,user_friends,user_interests,user_likes,user_location,user_photos,user_relationship_details&response_type=token");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar w
        // automatically handle clicks on the Home/Up button, so longlkl
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
