/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import com.iceberg.playingwithvowels.R;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PhonologicalAwarenessActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonological_awareness);
		
		Button levelOne = (Button) this.findViewById(R.id.phonological_awareness_level_1);
		levelOne.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					 Intent intent = new Intent(PhonologicalAwarenessActivity.this, PhonologicalAwarenessSelectVowelActivity.class); 
					 startActivity(intent);
					
				} catch (Exception ex) {
					Log.w("Intent", ex.getMessage());
				}
			}
		});
		
		Button levelTwo = (Button) this.findViewById(R.id.phonological_awareness_level_2);
		levelTwo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				try {
					Intent intent = new Intent(PhonologicalAwarenessActivity.this, PhonologicalAwarenessLevelTwoActivity.class); 
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
