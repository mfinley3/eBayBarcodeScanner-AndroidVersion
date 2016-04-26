package cs436project.ebaybarcodescanner_androidversion;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.HashMap;

public class PostToEbay extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_to_ebay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.webtoolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.post_on_ebay);
        actionBar.setSubtitle(R.string.once_done);
        actionBar.setDisplayShowTitleEnabled(true);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        ApplicationState state = ((ApplicationState) getApplicationContext());
        ArrayList<HashMap<String,Object>> history = state.getHistory();
        webView.loadUrl("http://csr.ebay.com/sell/list.jsf?usecase=create&mode=AddItem&categoryId=" + history.get(0).get("catID").toString()+ "&rp=srp&title=" + history.get(0).get("title").toString());

    }

}