/*
 * project 		DocuWeb
 * 
 * package 		com.randerson.JS
 * 
 * @author 		Rueben Anderson
 * 
 * date			Aug 1, 2013
 * 
 */
package com.randerson.JS;

import java.util.HashMap;

import libs.FileSystem;
import libs.InterfaceManager;
import libs.UniArray;

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
		// save the note data
		UniArray saveData = (UniArray) FileSystem.readObjectFile(CONTEXT, "save_data", true);
		
		// verify if the saveData object is valid
		if (saveData == null)
		{
			// create the new save data object
			saveData = new UniArray();
		}
		
		// create the hashmap item
		HashMap<String, Object> item = new HashMap<String, Object>();
		
		// store the passed in strings into the hashmap
		item.put("title", value[0]);
		item.put("people", value[1]);
		item.put("time", value[2]);
		item.put("location", value[3]);
		item.put("description", value[4]);
		item.put("timestamp", value[5]);
		
		// store the bitmap data into the hashmap
		item.put("image", PATH);
		
		// store the new saved data
		saveData.putObject(value[0], item);
		FileSystem.writeObjectFile(CONTEXT, saveData, "save_data", true);
		
		// show toast informing the user that the data was saved
		InterfaceManager UIFactory = new InterfaceManager(CONTEXT);
		(UIFactory.createToast("Data Saved!", false)).show();
		
		// end the activity
		parent.endActivity();
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
