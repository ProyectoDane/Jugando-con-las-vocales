/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import java.util.Random;
import com.iceberg.playingwithvowels.R;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class VisualPerceptionVowelsAdapter extends BaseAdapter{
	private Context _context;
	private int _imageSourceID;
	private float[] _rotations = new float[] { 0, 115, 230, 300 };
	private int[] _images = new int[4];
	private VowelsEnum _vowel;
	
	public VisualPerceptionVowelsAdapter(Context context, VowelsEnum vowel){
		_context = context;
		_vowel = vowel;
				
		_images = new int[] { _imageSourceID, _imageSourceID, _imageSourceID, _imageSourceID };
	}
	
	@Override
	public int getCount() {
		return _images.length;
	}

	@Override
	public Object getItem(int position) {
		return _images[position];
	}

	@Override
	public long getItemId(int arg0) {		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout rowView = null;
		
		if (convertView == null){
			rowView = (LinearLayout) LayoutInflater.from(_context).inflate(R.layout.view_visual_perception_row, parent, false);
		}
		else{
			rowView = (LinearLayout) convertView;
		}
		
		boolean allUsed = true;
		for (float rotation : _rotations) {
			if(rotation != -1)
				allUsed = false;
		}
		
		if(allUsed)
			_rotations = new float[] { 0, 115, 230, 300 };
		
		Random random = new Random();
		int rotationPosition = random.nextInt(4);
		float rotation = -1;
		while(rotation == -1){			
			rotation = _rotations[rotationPosition];
			_rotations[rotationPosition] = -1;
			rotationPosition = random.nextInt(4);
		}
		
		TextView textView = (TextView) rowView.findViewById(R.id.view_visual_perception_textview);
		textView.setRotation(rotation);
		
		textView.setText(_vowel.name());
		
		return rowView;
	}

}
