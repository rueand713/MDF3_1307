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

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

public class WidgetConfig extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		Intent intent = getIntent();
		
		if (intent != null)
		{
			Bundle bundle = intent.getExtras();
			
			if (bundle != null)
			{
				int widgetId = bundle.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
				
				if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID)
				{
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
					UniArray data = (UniArray) FileSystem.readObjectFile(this, "Defaults", true);
					
					// string the count of notes saved
					String numNotes = "" + data.objectsLength();
					
					// create the remove views object from the widget layout
			        RemoteViews rViews = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
			        
			        // update the textview data in the layout
					rViews.setTextViewText(R.id.num_notes_value, numNotes);
					rViews.setTextViewText(R.id.update_time, updateTime);
					
					Intent pinpoint = new Intent(this, MainActivity.class);
			        pinpoint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			        
			        // create the pending activity intent
			        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, pinpoint, 0);
			        
					// set the click listener for the 
			        rViews.setOnClickPendingIntent(R.id.launch_button, pendingIntent);
			        
			        // Tell the AppWidgetManager to perform an update on the current app widget
			        AppWidgetManager.getInstance(this).updateAppWidget(widgetId, rViews);

			        
			        Intent widgetProvider = new Intent(this, WidgetProvider.class);
					PendingIntent pService = PendingIntent.getBroadcast(this, 0, widgetProvider, 0);
					
					AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
			        alarm.setRepeating(AlarmManager.RTC, (System.currentTimeMillis() + 1000 * 3), 10000, pService);
			        
			        Intent resultValue = new Intent();
			        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			        setResult(RESULT_OK, resultValue);
			        finish();
				}
			}
		}
		
	}
	
}
