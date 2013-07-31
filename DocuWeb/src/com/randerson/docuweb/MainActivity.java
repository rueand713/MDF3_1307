package com.randerson.docuweb;

import java.io.File;

import libs.FileSystem;

import com.randerson.JS.JSInterface;
import com.randerson.interfaces.ActivityInterface;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity implements ActivityInterface {

	final String URL = "https://dl.dropboxusercontent.com/u/68223018/apps/docuweb/docuweb.html";
	
	JSInterface interfaceJS;
	WebView webApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		interfaceJS = new JSInterface(this);
		
		// create the webview from the layout
		webApp = (WebView) findViewById(R.id.webView);
		
		// verify that the webview is valid
		if (webApp != null)
		{
			WebChromeClient chromeClient = new WebChromeClient();
			
			// set the webview to allow the use of javascript
			WebSettings settings = webApp.getSettings();
			settings.setJavaScriptEnabled(true);
			
			webApp.setWebChromeClient(chromeClient);
			
			// setup the webview js interface object and namespace hook
			webApp.addJavascriptInterface(interfaceJS, "Native");
			
			// load the hybrid app webpage url into the webview
			webApp.loadUrl(URL);
			
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void launchCamera() {
		
		// intent for launching the camera app
		Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		
		// verify that the intent is valid
		if (camera != null)
		{
			startActivityForResult(camera, 0);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0)
		{
			if (data != null)
			{
				Bundle bundle = data.getExtras();
				
				if (bundle != null && bundle.containsKey("data"))
				{
					// create the bitmap image and save it to the application file system
					Bitmap image = (Bitmap) bundle.get("data");
					
					// capture the system write data returned
					Object[] result = FileSystem.writeBitmapFile(this, image, "default_image.jpg", false, false, 90, Bitmap.CompressFormat.JPEG);
					
					// grab the file path from the write data
					String path = (String) result[1];
					
					// pass the image path to the js interface
					interfaceJS.PATH = path;
					
					Log.i("Path", path);
					
					// call method to retrieve the image for the webview
					interfaceJS.callJSRetrieveImage(webApp);
				}
			}
		}
	}

}
