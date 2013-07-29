package com.randerson.docuweb;

import java.io.File;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {

	final String URL = "https://dl.dropboxusercontent.com/u/68223018/apps/docuweb/docuweb.html";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		File webApp = new File("/DocuWeb/src/com/randerson/docuweb/web_app.html");
		
		if (webApp.exists() == true)
		{
			Log.i("Web Data", "exists");
		}
		else
		{
			Log.i("Web Data", "not existed");
		}
		
		/*
		// create the webview from the layout
		WebView webview = (WebView) findViewById(R.id.webView);
		
		// verify that the webview is valid
		if (webview != null)
		{
			// set the webview to allow the use of javascript
			webview.getSettings().setJavaScriptEnabled(true);
			
			// setup the webview js interface object and namespace hook
			webview.addJavascriptInterface(new JSInterface(this), "Native");
			
			// load the hybrid app webpage url into the webview
			webview.loadUrl(URL);
		}
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
