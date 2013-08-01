/*
 * project 		DocuWeb
 * 
 * package 		com.randerson.docuweb
 * 
 * @author 		Rueben Anderson
 * 
 * date			Aug 1, 2013
 * 
 */
package com.randerson.docuweb;

import libs.FileSystem;
import libs.InterfaceManager;

import com.randerson.JS.JSInterface;
import com.randerson.interfaces.ActivityInterface;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class NoteActivity extends Activity implements ActivityInterface {

	final String URL = "https://dl.dropboxusercontent.com/u/68223018/apps/docuweb/docuweb.html";
	
	InterfaceManager UIFactory = null;
	JSInterface interfaceJS;
	WebView webApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_main);
	
		interfaceJS = new JSInterface(this);
		
		// instantiate the UIFactory singleton
		UIFactory = new InterfaceManager(this);
		
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
		
		// verify the requestcode
		if (requestCode == 0)
		{
			// verify that the data object is valid
			if (data != null)
			{
				// get the intent extras data
				Bundle bundle = data.getExtras();
				
				// verify that the bundle is valid and has the data key
				if (bundle != null && bundle.containsKey("data"))
				{
					// create the bitmap image and save it to the application file system
					Bitmap image = (Bitmap) bundle.get("data");
					
					// save the image and capture the system write data returned
					String[] result = FileSystem.writeBitmapFile(this, image, "default_image.jpg", false, false, 90, Bitmap.CompressFormat.JPEG);
					
					// the success string
					String success = result[0];
					
					// verify that the operation was a success
					if (success.equals("true"))
					{
						// grab the file path from the write data
						String path = (String) result[1];
						
						// pass the image path to the js interface
						interfaceJS.PATH = path;
						
						// show a toast to the user that the photo was updated
						(UIFactory.createToast("Photo Updated", false)).show();
					}
					
					/* removed - html img doesn't receive permission to load the file
					// call method to retrieve the image for the webview
					interfaceJS.callJSRetrieveImage(webApp);
					*/
				}
			}
		}
	}

	@Override
	public void endActivity() {
		
		// call the finish method
		finish();
		
	}

}
