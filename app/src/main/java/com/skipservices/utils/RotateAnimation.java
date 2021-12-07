package com.skipservices.utils;









import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.skipservices.R;


public class RotateAnimation extends Dialog {

	public RotateAnimation(Context context) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rotateanimation);
		ImageView imageview = (ImageView)findViewById(R.id.rotateimage);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		setCanceledOnTouchOutside(false);
		imageview.startAnimation(AnimationUtils.loadAnimation(context,R.anim.rotate));
	}

}
