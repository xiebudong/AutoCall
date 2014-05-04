package com.fgc.autocall.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.fgc.autocall.app.business.MessageSender;
import com.fgc.autocall.app.business.MicroPhone;
import com.fgc.autocall.data.ContactPersonWrapper;

public class OneByOneWork {

	public static final String LOG_TAG = "OneByOneWork";
	public static final int ITEM_STATE_NOT_DO = 0;
	public static final int ITEM_STATE_CALLING = 1;
	public static final int ITEM_STATE_MESSAGING = 2;
	public static final int ITEM_STATE_DONE_OK = 3;
	public static final int ITEM_STATE_DONE_FAILED = 4;
	
	private static final long DELAY_CALL = 3*1000;	
	
	private Context mContext;
	private Looper mLooper;
	private List<ContactPersonWrapper> mContactPersonWrappers = new ArrayList<ContactPersonWrapper>();
	private OnWorkingObserver mOnWorkingObserver;
	
	private WorkHandler mWorkHandler;
	private int mCurWorkingIndex = 0;
	
	public OneByOneWork(Context context, Looper looper)
	{
		mContext = context;
		mLooper = looper;
		mWorkHandler = new WorkHandler(mLooper);
	}
	
	public interface OnWorkingObserver
	{
		public void onDoWork(int index, int workType);
	}
	
	public void setOnWorkingObserver(OnWorkingObserver observer)
	{
		mOnWorkingObserver = observer;
	}
	
	public void resetContacts(List<ContactPersonWrapper> contactsWrappers)
	{
		if (contactsWrappers == null)
		{
			return;
		}
		
		mContactPersonWrappers.clear();
		mContactPersonWrappers.addAll(contactsWrappers);
	}
	
	public void startWork()
	{
		if (mContactPersonWrappers.size()==0)
		{
			Log.w(LOG_TAG, "not start, wrappers size is 0");
			return;
		}
		mWorkHandler.sendEmptyMessageDelayed(MSG_WHAT_CALL, 3000);
	}
	
	public void pauseWork()
	{
		mWorkHandler.removeCallbacksAndMessages(null);
	}
	
	private void endCall()
	{
		Log.i(LOG_TAG, "endCall");
		ITelephony telephony = getITelephony(mContext);
		if (telephony != null)
		{
			try {
				telephony.endCall();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			Log.e(LOG_TAG, "telephony is null !");
		}
	}
	
	private final int MSG_WHAT_CALL = 0;
	private class WorkHandler extends Handler
	{
		public WorkHandler(Looper looper)
		{
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case MSG_WHAT_CALL:
				if (mCurWorkingIndex<0)
				{
					Log.e(LOG_TAG, "mCurWorkingIndex<0");
					return;
				}
				if (mCurWorkingIndex>=mContactPersonWrappers.size())
				{
					Log.w(LOG_TAG, "over");
					if (mOnWorkingObserver != null)
					{
						mOnWorkingObserver.onDoWork(mCurWorkingIndex-1, ITEM_STATE_DONE_OK);
					}
					Log.i(LOG_TAG, "end call : " + (mCurWorkingIndex-1));
					endCall();
					mCurWorkingIndex = 0;
					return;
				}
				
				Log.i(LOG_TAG, "calling index: " + mCurWorkingIndex);
				int indexOk = mCurWorkingIndex-1;
				if (indexOk >=0)
				{
					Log.i(LOG_TAG, "end call : " + indexOk);
					endCall();
					if (mOnWorkingObserver != null)
					{
						mOnWorkingObserver.onDoWork(indexOk, ITEM_STATE_DONE_OK);
					}
				}
				if (mOnWorkingObserver != null)
				{
					mOnWorkingObserver.onDoWork(mCurWorkingIndex, ITEM_STATE_CALLING);
				}
				ContactPersonWrapper contactWrapper =  mContactPersonWrappers.get(mCurWorkingIndex);
				Log.i(LOG_TAG, "to call : " + contactWrapper.getContactPerson().getName());
				// call
				MicroPhone.instance(mContext).call(contactWrapper.getContactPerson().getPhoneNumber());
				// send message
				MessageSender.instance().send(contactWrapper.getContactPerson().getPhoneNumber(), 
						contactWrapper.generateShortMessage(), null, null);
				
				mCurWorkingIndex++;
				mWorkHandler.sendEmptyMessageDelayed(MSG_WHAT_CALL, DELAY_CALL);
				break;
			}
		}
	}
	
	private static ITelephony getITelephony(Context context) {  
	      TelephonyManager mTelephonyManager = (TelephonyManager) context  
	              .getSystemService(context.TELEPHONY_SERVICE);  
	      Class<TelephonyManager> c = TelephonyManager.class;  
	      Method getITelephonyMethod = null;  
	      ITelephony iTelephony = null ;  
	      try {  
	          getITelephonyMethod = c.getDeclaredMethod("getITelephony",  
	                  (Class[]) null); // 获取声明的方法   
	          getITelephonyMethod.setAccessible(true);  
	      } catch (SecurityException e) {  
	          e.printStackTrace();  
	      } catch (NoSuchMethodException e) {  
	          e.printStackTrace();  
	      }  
	  
	      try {  
	          iTelephony = (ITelephony) getITelephonyMethod.invoke(  
	                  mTelephonyManager, (Object[]) null); // 获取实例   
	          return iTelephony;  
	      } catch (Exception e) {  
	          e.printStackTrace();  
	      }  
	      return iTelephony;  
	  }  
}
