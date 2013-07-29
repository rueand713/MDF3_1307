package com.randerson.JS;

import android.content.Context;
import android.util.Log;

public class JSInterface {

	Context CONTEXT = null;
	
	public JSInterface(Context context)
	{
		CONTEXT = context;
	}
	
	@android.webkit.JavascriptInterface
	public void clicked(String value)
	{
		Log.i("Clicked Fired", value);
	}
}
