package com.tommy.the5ddit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-6292085259531298/2620204168");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Insert your code here
                WebView myWebView = (WebView) findViewById(R.id.webView);
                myWebView.loadUrl("http://www.5ddit.com"); // refreshes the WebView

            }
        });


        WebView myWebView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setLongClickable(true);
        myWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView myWebView = (WebView) v;
                WebView.HitTestResult result = myWebView.getHitTestResult();
                if (result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE){
                    Toast.makeText(MainActivity.this, "copied to clipboard: "+result.getExtra(), Toast.LENGTH_SHORT).show();
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(result.getExtra(), result.getExtra());
                    clipboard.setPrimaryClip(clip);
                }



                return false;
            }
        });
        myWebView.loadUrl("http://www.5ddit.com");
        swipeLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(true);
            }
        });

        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                swipeLayout.setRefreshing(true);
                return true;
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                swipeLayout.setRefreshing(false);
            }
            @Override
            public void onPageFinished(WebView view, String url){
                swipeLayout.setRefreshing(false);
            }
        });
        myWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
                                webView.goBack();
                                swipeLayout.setRefreshing(true);
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });




    }
}
