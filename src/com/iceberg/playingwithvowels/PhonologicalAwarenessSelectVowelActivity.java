/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.Arrays;


public class PhonologicalAwarenessSelectVowelActivity extends ActivityBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonological_awareness_select_vowel);
		
		Button btnA = (Button) this.findViewById(R.id.phonological_awareness_select_a);
		btnA.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				VowelSelected(Arrays.asList(VowelsEnum.values()).indexOf(VowelsEnum.A));				
			}
		});
		
		Button btnE = (Button) this.findViewById(R.id.phonological_awareness_select_e);
		btnE.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				VowelSelected(Arrays.asList(VowelsEnum.values()).indexOf(VowelsEnum.E));	
			}
		});
		
		Button btnI = (Button) this.findViewById(R.id.phonological_awareness_select_i);
		btnI.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				VowelSelected(Arrays.asList(VowelsEnum.values()).indexOf(VowelsEnum.I));
			}
		});
		
		
		Button btnO = (Button) this.findViewById(R.id.phonological_awareness_select_o);
		btnO.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				VowelSelected(Arrays.asList(VowelsEnum.values()).indexOf(VowelsEnum.O));
			}
		});
		
		
		Button btnU = (Button) this.findViewById(R.id.phonological_awareness_select_u);
		btnU.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				VowelSelected(Arrays.asList(VowelsEnum.values()).indexOf(VowelsEnum.U));
			}
		});
	}
	
	private void VowelSelected(int position){
		try {
			Intent intent = new Intent(PhonologicalAwarenessSelectVowelActivity.this, PhonologicalAwarenessLevelOneActivity.class);			
			intent.putExtra(PhonologicalAwarenessLevelOneActivity.ARG_POSITION, position);
			intent.putExtra(PhonologicalAwarenessLevelOneActivity.ARG_SCREEN, 1);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
			startActivity(intent);
		} catch (Exception ex) {
			Log.w("Intent", ex.getMessage());
		}
	}

	@Override
	public Intent GetUpIntent() {		
		return new Intent(this, PhonologicalAwarenessActivity.class);
	}


}
