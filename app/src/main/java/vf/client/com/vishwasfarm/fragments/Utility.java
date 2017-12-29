package vf.client.com.vishwasfarm.fragments;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Methods commonly used in the application. gets the advantage of code
 * re-usability
 * 
 */
@SuppressLint("SimpleDateFormat")
public class Utility {


	/**
	 * Get object animator
	 * 
	 * @param target
	 *            The object whose property is to be animated
	 * @param propertyName
	 *            The name of the property being animated
	 * @param from
	 *            Source value from where animation will start
	 * @param to
	 *            Destination value from where animation will end
	 * @param animationDuration
	 * @return ObjectAnimator object with animation duration and other
	 *         properties set
	 */
	public static ObjectAnimator getObjectAnimator(Object target,
			String propertyName, float from, float to, int animationDuration) {

		ObjectAnimator objectanimator = ObjectAnimator.ofFloat(target,
				propertyName, from, to);
		objectanimator.setDuration(animationDuration);
		return objectanimator;

	}


	public static void reverseAnimateOption(final RelativeLayout llOption) {

		llOption.clearAnimation();

		ObjectAnimator anim = ObjectAnimator.ofFloat(llOption, "translationX",
				0f, 50f);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				llOption.clearAnimation();
				llOption.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		anim.setDuration(AnimationConstants.SWIPE_ANIMATION_DURATION);
		anim.start();
	}
	
	public static void reverseAnimateOptionRelative(final RelativeLayout llOption) {

		llOption.clearAnimation();

		ObjectAnimator anim = ObjectAnimator.ofFloat(llOption, "translationX",
				0f, 50f);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				llOption.clearAnimation();
				llOption.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		anim.setDuration(AnimationConstants.SWIPE_ANIMATION_DURATION);
		anim.start();
	}
	
	
	
	public static void reverseAnimateOptionLeft(final LinearLayout llOption) {

		llOption.clearAnimation();

		ObjectAnimator anim = ObjectAnimator.ofFloat(llOption, "translationX",0f, -50f);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				llOption.clearAnimation();
				llOption.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		anim.setDuration(AnimationConstants.SWIPE_ANIMATION_DURATION);
		anim.start();	
	}

	public static void animateOptions(final RelativeLayout llOption) {

		llOption.clearAnimation();

		ObjectAnimator anim = ObjectAnimator.ofFloat(llOption, "translationX",
				50f, 0f);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				llOption.clearAnimation();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		anim.setDuration(AnimationConstants.SWIPE_ANIMATION_DURATION);
		anim.start();

	}
	
	public static void animateOptionsRelative(final RelativeLayout llOption) {

		llOption.clearAnimation();

		ObjectAnimator anim = ObjectAnimator.ofFloat(llOption, "translationX",
				50f, 0f);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				llOption.clearAnimation();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		anim.setDuration(AnimationConstants.SWIPE_ANIMATION_DURATION);
		anim.start();

	}
	
	
	public static void animateOptionsLeft(final LinearLayout llOption) {

		llOption.clearAnimation();

		ObjectAnimator anim = ObjectAnimator.ofFloat(llOption,"translationX", -50f, 0f);
		anim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				llOption.clearAnimation();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});

		anim.setDuration(AnimationConstants.SWIPE_ANIMATION_DURATION);
		anim.start();	

	}



	/**
	 * Interface defines animation durations
	 *
	 */
	public interface AnimationConstants {

		int SWIPE_ANIMATION_DURATION 		= 30;
		int WEB_LAYOUT_ANIMATION_DURATION 	= 200;

	}
}