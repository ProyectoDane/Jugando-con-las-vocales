/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.iceberg.playingwithvowels.helpers.AnimationHelper;
import com.iceberg.playingwithvowels.helpers.PhonologicalAwarenessVowelImageAdapter;
import com.iceberg.playingwithvowels.helpers.SoundHelper;
import com.iceberg.playingwithvowels.helpers.VowelsHelper;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;

public class PhonologicalAwarenessLevelTwoActivity extends ActivityBase {

	private int _vowelPosition;
	private VowelsEnum _vowel;
	private int _screen;	
	private ImageButton _buttonNext;	
	private int[] _images = new int[5];
	private View _currentThumbView;
	private TextView _vowelTextView;	
	private final String CURRENT_VOWEL_POSITION_KEY = "CURRENT_VOWEL_POSITION";
	private final String CURRENT_VOWEL_SCREEN_KEY = "CURRENT_VOWEL_SCREEN";
	private boolean _zoomViewOpen = false;
	private ArrayList<RandomScreen> _allScreens = new ArrayList<RandomScreen>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phonological_awareness_level_two);
		
		initScreens();
						
		SetScreen();
		
		AjustLayout(getResources().getConfiguration().orientation);
	}
	
	@Override
	public Intent GetUpIntent() {		
		return new Intent(this, PhonologicalAwarenessActivity.class);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(CURRENT_VOWEL_POSITION_KEY, _vowelPosition);
		outState.putInt(CURRENT_VOWEL_SCREEN_KEY, _screen);
		super.onSaveInstanceState(outState);
	}
    	
	
	private void SetScreen(){
		_zoomViewOpen = false;
		
		if(_allScreens.size() == 0){
			finish();
			return;
		}
				
		RandomScreen nextScreen = _allScreens.get(0); //take always the first element 
		_vowel = nextScreen.vowel;						
		_images = nextScreen.screens;
		
		View expandedImageView = findViewById(R.id.phonological_awareness_vowel_expanded_image_content);
		expandedImageView.setVisibility(View.GONE);
		
		if(_buttonNext != null){
			_buttonNext.setVisibility(View.INVISIBLE);
		}
		
		if(_vowelTextView == null){
			_vowelTextView = (TextView) findViewById(R.id.phonological_awareness_vowel_vowel_text);
		}
		
		_vowelTextView.setText(_vowel.name());
								
		GridView gridView = (GridView) findViewById(R.id.phonological_awareness_vowel_gridview);
        gridView.setAdapter(new PhonologicalAwarenessVowelImageAdapter(this, _images)); 
        gridView.setOnItemClickListener(_gridViewOnItemClickListener);
        
        _allScreens.remove(0); //remove the element used;
	}
	
	private String _currentImageName = "";
	@SuppressLint("DefaultLocale")
	private OnItemClickListener _gridViewOnItemClickListener = new OnItemClickListener() 
    {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
        {         
        	if(_zoomViewOpen)
        		return;
        	
        	_currentThumbView = v.findViewById(R.id.view_phonological_awareness_imageview);
            String imageName = getResources().getResourceEntryName(_images[position]);
            
            imageName = imageName.replace("ic_", "");
            _currentImageName = imageName;
            
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    SoundHelper.PlaySound(PhonologicalAwarenessLevelTwoActivity.this, _currentImageName);
                }
            });
            
            if(imageName.toUpperCase().startsWith(_vowel.name())){            	           	            	
            	_zoomViewOpen = true;
            	AnimationHelper.zoomImageFromThumbView(_currentThumbView, 
            											_images[position], 
            											findViewById(R.id.phonological_awareness_vowel_expanded_image_content),
            											(ImageView)findViewById(R.id.phonological_awareness_vowel_expanded_image), 
            											findViewById(R.id.phonological_awareness_vowel_gridview_container));
            	
            	
            	_buttonNext = (ImageButton) findViewById(R.id.phonological_awareness_vowel_button_next);
            	_buttonNext.setVisibility(View.VISIBLE);
            	_buttonNext.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View view) {						
						AnimationHelper.thumbViewFromImageZoom(_currentThumbView, 
															   findViewById(R.id.phonological_awareness_vowel_expanded_image_content), 
															   findViewById(R.id.phonological_awareness_vowel_gridview_container));											
						SetScreen();
					}            		
            	});
            }
            else
            {
            	AnimationHelper.shake(v);
            }
        }
    };
 
    private void AjustLayout(int orientation){
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
        
    private void initScreens(){    	 
    	_allScreens = new ArrayList<RandomScreen>();
    	
    	_allScreens.add(new RandomScreen(VowelsEnum.A, new int[] { R.drawable.ic_luna, R.drawable.ic_agua, R.drawable.ic_silla }));
    	_allScreens.add(new RandomScreen(VowelsEnum.A, new int[] { R.drawable.ic_auto, R.drawable.ic_raton, R.drawable.ic_munieca }));
    	_allScreens.add(new RandomScreen(VowelsEnum.A, new int[] { R.drawable.ic_vestido, R.drawable.ic_abeja, R.drawable.ic_reloj }));
    	_allScreens.add(new RandomScreen(VowelsEnum.A, new int[] { R.drawable.ic_banana, R.drawable.ic_anana, R.drawable.ic_manzana }));
    	_allScreens.add(new RandomScreen(VowelsEnum.A, new int[] { R.drawable.ic_tren, R.drawable.ic_camion, R.drawable.ic_avion }));
    	
    	_allScreens.add(new RandomScreen(VowelsEnum.E, new int[] { R.drawable.ic_elefante, R.drawable.ic_oso, R.drawable.ic_zapato }));
    	_allScreens.add(new RandomScreen(VowelsEnum.E, new int[] { R.drawable.ic_perro, R.drawable.ic_remera, R.drawable.ic_espejo }));
    	_allScreens.add(new RandomScreen(VowelsEnum.E, new int[] { R.drawable.ic_escalera, R.drawable.ic_vestido, R.drawable.ic_reloj }));
    	_allScreens.add(new RandomScreen(VowelsEnum.E, new int[] { R.drawable.ic_moto, R.drawable.ic_leon, R.drawable.ic_escudo }));
    	_allScreens.add(new RandomScreen(VowelsEnum.E, new int[] { R.drawable.ic_ensalada, R.drawable.ic_campanas, R.drawable.ic_boton }));
    	
    	_allScreens.add(new RandomScreen(VowelsEnum.I, new int[] { R.drawable.ic_pollera, R.drawable.ic_bota_de_lluvia, R.drawable.ic_indio }));
    	_allScreens.add(new RandomScreen(VowelsEnum.I, new int[] { R.drawable.ic_tambor, R.drawable.ic_iguana, R.drawable.ic_heladera }));
    	_allScreens.add(new RandomScreen(VowelsEnum.I, new int[] { R.drawable.ic_isla, R.drawable.ic_guitarra, R.drawable.ic_zapatilla }));
    	
    	_allScreens.add(new RandomScreen(VowelsEnum.O, new int[] { R.drawable.ic_ojo, R.drawable.ic_lapicera, R.drawable.ic_galletitas }));
    	_allScreens.add(new RandomScreen(VowelsEnum.O, new int[] { R.drawable.ic_arbol, R.drawable.ic_paraguas, R.drawable.ic_ola }));
    	_allScreens.add(new RandomScreen(VowelsEnum.O, new int[] { R.drawable.ic_taza, R.drawable.ic_ojota, R.drawable.ic_caballo }));
    	_allScreens.add(new RandomScreen(VowelsEnum.O, new int[] { R.drawable.ic_cama, R.drawable.ic_oso, R.drawable.ic_pan }));
    	
    	_allScreens.add(new RandomScreen(VowelsEnum.U, new int[] { R.drawable.ic_lapiz, R.drawable.ic_gato, R.drawable.ic_unia }));
    	_allScreens.add(new RandomScreen(VowelsEnum.U, new int[] { R.drawable.ic_cuaderno, R.drawable.ic_pajaro, R.drawable.ic_uvas }));
    	_allScreens.add(new RandomScreen(VowelsEnum.U, new int[] { R.drawable.ic_tortuga, R.drawable.ic_uno, R.drawable.ic_mochila }));
    	    	
    	Collections.shuffle(_allScreens);    
    }
    
    @SuppressWarnings("unused")
	private class RandomScreen {
    	public RandomScreen(VowelsEnum v, int[] s){
    		vowel = v;
    		screens = s;    		
    	}
    	
    	public VowelsEnum vowel;
    	public int[] screens;
    }
}
