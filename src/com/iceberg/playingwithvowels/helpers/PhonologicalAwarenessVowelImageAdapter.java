/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import java.util.Arrays;
import com.iceberg.playingwithvowels.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhonologicalAwarenessVowelImageAdapter extends BaseAdapter {
	private Context _context;
	private int[] _images;

	public PhonologicalAwarenessVowelImageAdapter(Context context, int[] images) {
		_context = context;
		_images = images;
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
	public long getItemId(int id) {
		int position = Arrays.asList(_images).indexOf(id);

		if (position == -1)
			return 0;
		else
			return _images[position];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout content = null;
		ImageView imageView = null;
		if (convertView == null)
			content = (LinearLayout) LayoutInflater.from(_context).inflate(R.layout.view_phonological_awareness_image_row, parent, false);
		else
			content = (LinearLayout) convertView;
		
		imageView = (ImageView) content.findViewById(R.id.view_phonological_awareness_imageview);
		imageView.setImageResource(_images[position]);
		
		return content;
	}
}
