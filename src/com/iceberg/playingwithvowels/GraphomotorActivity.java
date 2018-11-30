/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import java.util.Arrays;

import com.iceberg.playingwithvowels.helpers.TouchVowelView;
import com.iceberg.playingwithvowels.helpers.TouchVowelView.TouchVowelListener;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GraphomotorActivity extends ActivityBase implements TouchVowelListener{
	private TouchVowelView _vowelView;
	private ImageButton _btnNext;
	private ImageView _btnOk;
	private VowelsEnum _currentVowel = VowelsEnum.A;
	private LinearLayout _vowelContent;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_graphomotor);
		
		
		_vowelView = new TouchVowelView(this, GetCanvasLayoutParams(), _currentVowel);
				
		_vowelContent = (LinearLayout)findViewById(R.id.graphomotor_touch_content_view);		
		_vowelContent.addView(_vowelView);
		
		_btnNext = (ImageButton)findViewById(R.id.graphomotor_next);
		_btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				int position = Arrays.asList(VowelsEnum.values()).indexOf(_currentVowel);
				position++;
				if(position == 5){
					finish();
				}
				else{
					_currentVowel = Arrays.asList(VowelsEnum.values()).get(position);
					
					_vowelContent.removeView(_vowelView);
					
					_vowelView = new TouchVowelView(GraphomotorActivity.this, GetCanvasLayoutParams(), _currentVowel);
					_vowelView.invalidate();
					
					_vowelContent.addView(_vowelView);
					
					_btnNext.setVisibility(View.INVISIBLE);
					_btnOk.setVisibility(View.INVISIBLE);
				}
			}
		});
		_btnNext.setVisibility(View.INVISIBLE);
		
		_btnOk = (ImageView) findViewById(R.id.graphomotor_ok);
		_btnOk.setVisibility(View.INVISIBLE);
		
		//setContentView(new TouchVowelView(this));
		//setContentView(new PaintView(this));
	}
	
	
	@Override
	public Intent GetUpIntent() {
		return new Intent(this, MainActivity.class);
	}

	@Override
	public void onVowelCompleted() {
		_btnOk.setVisibility(View.VISIBLE);
		_btnNext.setVisibility(View.VISIBLE);		
	}
	
	private LayoutParams GetCanvasLayoutParams(){
		LayoutParams params = new LayoutParams(400, 400);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		
		return params;	
	}
	
}
