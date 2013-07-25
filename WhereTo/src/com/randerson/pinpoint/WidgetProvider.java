/*
 * project 		Pinpoint
 * 
 * package 		com.randerson.pinpoint
 * 
 * @author 		Rueben Anderson
 * 
 * date			Jul 25, 2013
 * 
 */
package com.randerson.pinpoint;

import java.util.Calendar;

import libs.UniArray;

import com.randerson.classes.FileSystem;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		Log.i("WidgetProvider", "Updated");
		
		Calendar now = Calendar.getInstance();
		now.setTimeZone(now.getTimeZone());
		
		String hour = "" + now.get(Calendar.HOUR);
		String min = "" + now.get(Calendar.MINUTE);
		String sec = "" + now.get(Calendar.SECOND);
		String updateTime = null;
		
		// format the minutes time string
		if (min.length() < 2)
		{
			min = "0" + min;
		}
		
		// format the seconds time string
		if (sec.length() < 2)
		{
			sec = "0" + sec;
		}
		
		if (hour.equals("0"))
		{
			hour = "12";
		}
		
		// format the whole update time string
		updateTime = "Last Updated " + hour + ":" + min + ":" + sec;
		
		// grab the app data
		UniArray data = (UniArray) FileSystem.readObjectFile(context, "Defaults", true);
		
		// string the count of notes saved
		String numNotes = "" + data.objectsLength();
		
		for (int i = 0; i < appWidgetIds.length; i++)
		{
			int appWidgetId = appWidgetIds[i];
			
			Intent pinpoint = new Intent(context, MainActivity.class);
	        pinpoint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        
	        // create the pending activity intent
	        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, pinpoint, 0);

	        // create the remove views object from the widget layout
	        RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
	        
	        // update the textview data in the layout
			rViews.setTextViewText(R.id.num_notes_value, numNotes);
			rViews.setTextViewText(R.id.update_time, updateTime);
	        
			// set the click listener for the 
	        rViews.setOnClickPendingIntent(R.id.launch_button, pendingIntent);

	        // Tell the AppWidgetManager to perform an update on the current app widget
	        appWidgetManager.updateAppWidget(appWidgetId, rViews);
	        
	        super.onUpdate(context, appWidgetManager, appWidgetIds);
		}
	}

}
