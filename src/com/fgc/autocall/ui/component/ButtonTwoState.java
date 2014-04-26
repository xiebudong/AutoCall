package com.fgc.autocall.ui.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/*
 * two state button, auto switch background, default state is state1.
 * @author: FGC
 */
public class ButtonTwoState extends Button{

	private static final String LOG_TAG = "ButtonTwoState";
	
	public ButtonTwoState(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ButtonTwoState(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ButtonTwoState(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}

	
	private Context mContext;
	private Drawable mBgState1;
	private Drawable mBgState2;
	private OnTwoStateSwitchListener mOnSwitchListener;
	private boolean mState = true;
	
	private void init(Context context)
	{
		mContext = context;
		mBgState1 = getBackground();
		mBgState2 = getBackground();
		setOnClickListener(mOnClickListenenr);
	}
	
	private OnClickListener mOnClickListenenr = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			doSwitch();
		}
	};
	
	public interface OnTwoStateSwitchListener
	{
		public void onSwitch(boolean isState1);
	}
	
	public void setOnTwoStateSwitchListener(OnTwoStateSwitchListener listener)
	{
		mOnSwitchListener = listener;
	}
	
	public void setTwoStateDrawble(Drawable state1, Drawable state2)
	{
		mBgState1 = state1;
		mBgState2 = state2;
		setBackgroundDrawable(mBgState1);
	}
	
	public void setTwoStateDrawble(int stateSrcId1, int stateSrcId2)
	{
		mBgState1 = mContext.getResources().getDrawable(stateSrcId1);
		mBgState2 = mContext.getResources().getDrawable(stateSrcId2);
		setBackgroundDrawable(mBgState1);
	}
	
	private void doSwitch()
	{
		if (mState == true)
		{
			mState = false;
			setBackgroundDrawable(mBgState2);
		}
		else
		{
			mState = true;
			setBackgroundDrawable(mBgState1);
		}
		
		if (mOnSwitchListener != null)
		{
			mOnSwitchListener.onSwitch(mState);
		}
	}
}
