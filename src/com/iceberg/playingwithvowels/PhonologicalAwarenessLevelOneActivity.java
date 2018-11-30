/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import com.iceberg.playingwithvowels.R;
import com.iceberg.playingwithvowels.helpers.AnimationHelper;
import com.iceberg.playingwithvowels.helpers.PhonologicalAwarenessVowelImageAdapter;
import com.iceberg.playingwithvowels.helpers.SoundHelper;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class PhonologicalAwarenessLevelOneActivity extends ActivityBase {

	public final static String ARG_POSITION = "position";
	public final static String ARG_SCREEN = "screen";
	private int _screen = 1;
	private String _vowel = "";
	private int[] _images = new int[5];
	int _currentPosition = -1;
	private ImageButton _buttonOk;
	private View _currentThumbView;
	private TextView _textView;
	private boolean _zoomViewOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonological_awareness_level_one);
		
		if (savedInstanceState == null) {
			_currentPosition = getIntent().getIntExtra(ARG_POSITION, 0);
			_screen = getIntent().getIntExtra(ARG_SCREEN, 1);
		} else {
			_currentPosition = savedInstanceState.getInt(ARG_POSITION);
			_screen = savedInstanceState.getInt(ARG_SCREEN);
		}

		updateView(_currentPosition);

		AjustLayout(getResources().getConfiguration().orientation);
		
		if (savedInstanceState == null) {
			new Handler().post(new Runnable() {
	            @Override
	            public void run() {
	                SoundHelper.PlaySound(PhonologicalAwarenessLevelOneActivity.this, VowelsEnum.values()[_currentPosition]);
	            }
	        });
		}
	}

	@Override
	public Intent GetUpIntent() {
		return new Intent(this, PhonologicalAwarenessSelectVowelActivity.class);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(ARG_POSITION, _currentPosition);
		outState.putInt(ARG_SCREEN, _screen);
	}

	private void AjustLayout(int orientation) {
		LinearLayout main = (LinearLayout) findViewById(R.id.phonological_awareness_vowel_layout_main);
		LinearLayout firstChild = (LinearLayout) findViewById(R.id.phonological_awareness_vowel_layout_first_child);
		FrameLayout secondChild = (FrameLayout) findViewById(R.id.phonological_awareness_vowel_layout_second_child);

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

	private void updateView(int position) {
		_zoomViewOpen = false;
		_currentPosition = position;
		_vowel = VowelsEnum.values()[position].name();

		if (_textView == null)
			_textView = (TextView) findViewById(R.id.phonological_awareness_vowel_vowel_text);

		_textView.setText(_vowel);

		View expandedImageView = findViewById(R.id.phonological_awareness_vowel_expanded_image_content);
		expandedImageView.setVisibility(View.GONE);

		if (_buttonOk != null) {
			_buttonOk.setVisibility(View.INVISIBLE);
		}

		_images = PhonologicalAwareness.GetScreen(VowelsEnum.values()[position], _screen);

		GridView gridView = (GridView) findViewById(R.id.phonological_awareness_vowel_gridview);
		gridView.setAdapter(new PhonologicalAwarenessVowelImageAdapter(this, _images));
		gridView.setOnItemClickListener(_gridViewOnItemClickListener);
	}

	@SuppressLint("DefaultLocale")
	private OnItemClickListener _gridViewOnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			if(_zoomViewOpen)
				return;
			
			_currentThumbView = v.findViewById(R.id.view_phonological_awareness_imageview);
			String imageName = getResources().getResourceEntryName(_images[position]);

			imageName = imageName.replace("ic_", "");
			
			SoundHelper.PlaySound(PhonologicalAwarenessLevelOneActivity.this, imageName);

			if (imageName.toUpperCase().startsWith(_vowel)) {				
				_zoomViewOpen = true;
				AnimationHelper
						.zoomImageFromThumbView(
								_currentThumbView,
								_images[position],
								findViewById(R.id.phonological_awareness_vowel_expanded_image_content),
								(ImageView) findViewById(R.id.phonological_awareness_vowel_expanded_image),
								findViewById(R.id.phonological_awareness_vowel_gridview_container));

				_buttonOk = (ImageButton) findViewById(R.id.phonological_awareness_vowel_button_next);
				_buttonOk.setVisibility(View.VISIBLE);
				_buttonOk.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						AnimationHelper
								.thumbViewFromImageZoom(
										_currentThumbView,
										findViewById(R.id.phonological_awareness_vowel_expanded_image_content),
										findViewById(R.id.phonological_awareness_vowel_gridview_container));

						_screen = PhonologicalAwareness.GetNextScreen(VowelsEnum.values()[_currentPosition], _screen);

						if (_screen == 1) {
							finish();
						} else {
							updateView(_currentPosition);
						}
					}
				});
			} else {
				AnimationHelper.shake(v);
			}
		}
	};
}
