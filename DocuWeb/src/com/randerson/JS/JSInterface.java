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
		
		if (context instanceof ActivityInterface)
		{
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
		
		if (parent != null)
		{
			parent.launchCamera();
		}
		
	}
	
	@android.webkit.JavascriptInterface
	public String getImagePath()
	{
		Log.i("Javascript", "getImagePath");
		return PATH;
	}
	
	public void callJSRetrieveImage(WebView webview)
	{
		if (webview != null)
		{
			webview.loadUrl("javascript:javaClass.setImage()");
		}
	}
	
}
