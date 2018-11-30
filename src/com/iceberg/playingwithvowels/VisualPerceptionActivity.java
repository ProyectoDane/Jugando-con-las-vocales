/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import com.iceberg.playingwithvowels.helpers.AnimationHelper;
import com.iceberg.playingwithvowels.helpers.SoundHelper;
import com.iceberg.playingwithvowels.helpers.VisualPerceptionVowelsAdapter;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;

public class VisualPerceptionActivity extends ActivityBase {

	private int _vowelPosition = 0;
	private VowelsEnum _vowel;	
	private ImageButton _buttonNext;	
	private TextView _currentThumbView;
	private TextView _vowelTextView;
	private final String VOWEL_KEY = "VOWEL_KEY";
	private final String VOWEL_POSITION_KEY = "VOWEL_POSITION_KEY";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visual_perception);
		
		if(savedInstanceState != null){
			String vowelSaved = savedInstanceState.getString(VOWEL_KEY);
			_vowel = VowelsEnum.valueOf(vowelSaved);
			_vowelPosition = savedInstanceState.getInt(VOWEL_POSITION_KEY);
		}
		
		SetVisualPerceptionScreen();
		AjustLayout(getResources().getConfiguration().orientation);
	}
		
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(VOWEL_KEY, _vowel.name());
		outState.putInt(VOWEL_POSITION_KEY, _vowelPosition);
		super.onSaveInstanceState(outState);
	}

	@Override
	public Intent GetUpIntent() {		
		return new Intent(this, MainActivity.class);
	}
		
	public void SetVisualPerceptionScreen(){
				
		if(_vowelPosition < VowelsEnum.values().length){
			_vowel = VowelsEnum.values()[_vowelPosition];	
			
			View expandedImageView = findViewById(R.id.visual_perception_expanded_image_content);
			expandedImageView.setVisibility(View.GONE);
			
			if(_buttonNext != null){
				_buttonNext.setVisibility(View.INVISIBLE);
			}
			
			if(_vowelTextView == null){
				_vowelTextView = (TextView) findViewById(R.id.visual_perception_vowel_text);
			}
			
			_vowelTextView.setText(_vowel.name());
									
			GridView gridView = (GridView) findViewById(R.id.visual_perception_gridview);			
	        gridView.setAdapter(new VisualPerceptionVowelsAdapter(this, _vowel)); 
	        gridView.setOnItemClickListener(_gridViewOnItemClickListener);
		}
		else{
			finish();
		}
	}
	
	private void AjustLayout(int orientation){
		LinearLayout main = (LinearLayout) findViewById(R.id.visual_perception_layout_main);
		LinearLayout firstChild = (LinearLayout) findViewById(R.id.visual_perception_layout_first_child);
		FrameLayout secondChild = (FrameLayout) findViewById(R.id.visual_perception_layout_second_child);

		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			main.setOrientation(LinearLayout.HORIZONTAL);
			firstChild.getLayoutParams().height = LayoutParams.MATCH_PARENT;
			firstChild.getLayoutParams().width = 0;
			secondChild.getLayoutParams().height = LayoutParams.MATCH_PARENT;
			secondChild.getLayoutParams().width = 0;
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			main.setOrientation(LinearLayout.VERTICAL);
			firstChild.getLayoutParams().height = 0;
			firstChild.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			secondChild.getLayoutParams().height = 0;
			secondChild.getLayoutParams().width = LayoutParams.MATCH_PARENT;
		}
	}

	
	
	@SuppressLint("DefaultLocale")
	private OnItemClickListener _gridViewOnItemClickListener = new OnItemClickListener() 
    {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
        {   
        	_currentThumbView = (TextView)v.findViewById(R.id.view_visual_perception_textview);
            if(_currentThumbView.getRotation() == 0 || (_currentThumbView.getRotation() == 180 && (_currentThumbView.getText() == VowelsEnum.I.name() || _currentThumbView.getText() == VowelsEnum.O.name()))){
            	            	
                TextView expandedImage = (TextView)findViewById(R.id.visual_perception_expanded_image);
                expandedImage.setText(_currentThumbView.getText());
                
                //SoundHelper.PlaySound(VisualPerceptionActivity.this, R.raw.correct);
            	AnimationHelper.zoomViewFromThumbView(_currentThumbView,            										  	
            										  findViewById(R.id.visual_perception_expanded_image_content),
            										  expandedImage, 
            										  findViewById(R.id.visual_perception_gridview_container));
            	
            	            	
            	_buttonNext = (ImageButton) findViewById(R.id.visual_perception_button_next);
            	_buttonNext.setVisibility(View.VISIBLE);
            	_buttonNext.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {						
						AnimationHelper.thumbViewFromImageZoom(_currentThumbView, 
															   findViewById(R.id.visual_perception_expanded_image_content), 
															   findViewById(R.id.visual_perception_gridview_container));		
						_vowelPosition++;
						SetVisualPerceptionScreen();
					}            		
            	});
            }
            else
            {
            	AnimationHelper.shake(v);
            }
        }
    };
}
