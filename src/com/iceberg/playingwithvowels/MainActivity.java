/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.iceberg.playingwithvowels.R;

public class MainActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayHomeAsUpEnabled(false);

		Button phonologicalAwarenes = (Button) this.findViewById(R.id.phonological_awareness_btn);
		phonologicalAwarenes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent intent = new Intent(MainActivity.this, PhonologicalAwarenessActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
					startActivity(intent);
				} catch (Exception ex) {
					Log.w("Intent", ex.getMessage());
				}
			}
		});

		Button visualPerception = (Button) this.findViewById(R.id.visual_perception_btn);
		visualPerception.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent intent = new Intent(MainActivity.this, VisualPerceptionActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
					startActivity(intent);
				} catch (Exception ex) {
					Log.w("Intent", ex.getMessage());
				}
			}
		});

		Button graphomotor = (Button) this.findViewById(R.id.graphomotor_btn);
		graphomotor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent intent = new Intent(MainActivity.this, GraphomotorActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
					startActivity(intent);
				} catch (Exception ex) {
					Log.w("Intent", ex.getMessage());
				}
			}
		});
	}

	@Override
	public Intent GetUpIntent() {
		return new Intent(this, MainActivity.class);
	}
}
