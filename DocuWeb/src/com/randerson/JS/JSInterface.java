package com.randerson.JS;

import android.content.Context;
import android.util.Log;

public class JSInterface {

	Context CONTEXT = null;
	
	// constructor
	public JSInterface(Context context)
	{
		CONTEXT = context;
	}

	// method for interfacing with the app web ui by way of javascript
	@android.webkit.JavascriptInterface
	public void clicked(String value)
	{
		Log.i("Clicked Fired", value);
	}
}
