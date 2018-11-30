/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;

public abstract class ActivityBase extends Activity {
	
	@SuppressLint("NewApi")
	@Override
	public void startActivity(Intent intent, Bundle options) {
		super.startActivity(intent, options);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigth);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_rigth);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	public abstract Intent GetUpIntent();

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			
            Intent upIntent = GetUpIntent();
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {                
                TaskStackBuilder.create(this)                        
                        		.addNextIntent(upIntent)
                    			.startActivities();
                finish();
            } 
            else {
                NavUtils.navigateUpTo(this, upIntent);
            }
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		//getMenuInflater().inflate(R.menu.phonological_awareness, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}
}
