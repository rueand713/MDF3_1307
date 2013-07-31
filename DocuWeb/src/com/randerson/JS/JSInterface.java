package com.randerson.JS;

import com.randerson.interfaces.ActivityInterface;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

public class JSInterface {

	Context CONTEXT = null;
	public String PATH = null;
	
	private ActivityInterface parent = null;
	
	// constructor
	public JSInterface(Context context)
	{
		CONTEXT = context;
		
		// verify that the context activity does implement the activityInterface
		if (context instanceof ActivityInterface)
		{
			// cast the context to the activity interface
			parent = (ActivityInterface) context;
		}
	}

	// method for receiving the string data from the web app
	@android.webkit.JavascriptInterface
	public void clicked(String[] value)
	{
		String v = new String(value[0]);
		Log.i("Accept Clicked", v);
	}
	
	// method for launching the camera from the web app
	@android.webkit.JavascriptInterface
	public void getCameraImage()
	{
		// verify that the parent object is valid
		if (parent != null)
		{
			// start the camera intent from the method in the parent activity
			parent.launchCamera();
		}
		
	}
	
	// method for returning the camera image's path to the javascript
	@android.webkit.JavascriptInterface
	public String getImagePath()
	{
		Log.i("Javascript", "getImagePath");
		return PATH;
	}
	
	// method for calling the javascript image setting method
	public void callJSRetrieveImage(WebView webview)
	{
		// verify that the webview is valid
		if (webview != null)
		{
			// call the javascript setimage method
			webview.loadUrl("javascript:javaClass.setImage()");
		}
	}
	
}
