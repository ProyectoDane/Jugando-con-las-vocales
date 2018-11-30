/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import android.media.MediaPlayer;
import android.util.Log;

import com.iceberg.playingwithvowels.ActivityBase;
import com.iceberg.playingwithvowels.R;
import com.iceberg.playingwithvowels.helpers.VowelsHelper.VowelsEnum;

public class SoundHelper {	
	public static void PlaySound(ActivityBase context, int resourceSound){		
		try{
			MediaPlayer player = MediaPlayer.create(context.getApplicationContext(), resourceSound);			
			player.start();
		}
		catch(Exception ex){	
			Log.d("sonido no encontrado", ex.getMessage());
		}
	}
	
	public static void PlaySound(ActivityBase context, VowelsHelper.VowelsEnum vowel){		
		if(vowel == VowelsEnum.A){
			PlaySound(context, R.raw.a);
		}
		else if(vowel == VowelsEnum.E){
			PlaySound(context, R.raw.e);
		}
		else if(vowel == VowelsEnum.I){
			PlaySound(context, R.raw.i);
		}	
		else if(vowel == VowelsEnum.O){
			PlaySound(context, R.raw.o);
		}
		else{
			PlaySound(context, R.raw.u);
		}
	}
	
	public static void PlaySound(ActivityBase context, String resourceName){
		int resourceId = context.getResources().getIdentifier(resourceName, "raw", context.getPackageName());
		PlaySound(context, resourceId);
	}
}
