package com.randerson.launcher;

import java.util.List;

import org.apache.http.protocol.HTTP;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button clickBtn = (Button) findViewById(R.id.clickBtn);
		
		clickBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// make the implicit intent
				Intent sendTo = new Intent(Intent.ACTION_SEND);
				sendTo.setType(HTTP.PLAIN_TEXT_TYPE);
				sendTo.putExtra("address", "");
				
				// Verify it resolves
				PackageManager packageManager = getPackageManager();
				List<ResolveInfo> activities = packageManager.queryIntentActivities(sendTo, 0);
				boolean safe = activities.size() > 0;

				
				if (safe) startActivity(sendTo);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
