/**  
 *   Copyright (C) 2015  CADISA
 *   
 *   Check License.txt
 */

package com.iceberg.playingwithvowels.helpers;

import com.iceberg.playingwithvowels.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class AnimationHelper {
	
	
	public static void zoomImageFromThumbView(final View thumbView,	int imageResID, final View expandedImageViewContent, ImageView expandedImageView,	View parentGlobalVisibleRect) {
		
		expandedImageView.setImageResource(imageResID);
		zoomViewFromThumbView(thumbView, expandedImageViewContent, expandedImageView, parentGlobalVisibleRect);
	}
	
	public static void zoomViewFromThumbView(final View thumbView,	final View expandedImageViewContent, View expandedImageView, View parentGlobalVisibleRect) {
		
		final AnimatorSet animatorExpand;

		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		thumbView.getGlobalVisibleRect(startBounds);
		parentGlobalVisibleRect.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		thumbView.setAlpha(0f);
		expandedImageViewContent.setVisibility(View.VISIBLE);
		expandedImageViewContent.setPivotX(0);
		expandedImageViewContent.setPivotY(0);

		animatorExpand = new AnimatorSet();
		animatorExpand
				.play(ObjectAnimator.ofFloat(expandedImageViewContent, View.X, startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageViewContent, View.Y, startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageViewContent, View.SCALE_X, startScale, 1f))
				.with(ObjectAnimator.ofFloat(expandedImageViewContent, View.SCALE_Y, startScale, 1f));

		animatorExpand.setDuration(thumbView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime));
		animatorExpand.setInterpolator(new DecelerateInterpolator());		
		animatorExpand.start();
	}
	
	
	public static void thumbViewFromImageZoom(final View thumbView,	final View expandedImageViewContent, View parentGlobalVisibleRect) {		
		final AnimatorSet animatorCollapse;
		
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		thumbView.getGlobalVisibleRect(startBounds);
		parentGlobalVisibleRect.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		float startScale;
		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}
		
		final float startScaleFinal = startScale;

		animatorCollapse = new AnimatorSet();
		animatorCollapse
				.play(ObjectAnimator.ofFloat(expandedImageViewContent, View.X, startBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageViewContent, View.Y, startBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageViewContent, View.SCALE_X, startScaleFinal))
				.with(ObjectAnimator.ofFloat(expandedImageViewContent, View.SCALE_Y, startScaleFinal));
		animatorCollapse.setDuration(thumbView.getContext().getResources().getInteger(android.R.integer.config_shortAnimTime));
		animatorCollapse.setInterpolator(new DecelerateInterpolator());
		animatorCollapse.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				thumbView.setAlpha(1f);
				expandedImageViewContent.setVisibility(View.GONE);				
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				thumbView.setAlpha(1f);
				expandedImageViewContent.setVisibility(View.GONE);
			}
		});
		
		animatorCollapse.start();
	}
	
	public static void shake(View view){
		Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.shake);
    	animation.setDuration(1000);
    	view.startAnimation(animation);
	}
}
