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
	
	
	private Context mContext;
	private Looper mLooper;
	private List<ContactPersonWrapper> mContactPersonWrappers = new ArrayList<ContactPersonWrapper>();
	private OnWorkingObserver mOnWorkingObserver;
	
	private int mCallInternal = 30*1000;
	private WorkHandler mWorkHandler;
	private int mCurWorkingIndex = 0;
	
	private boolean mIsCall = true;
	private boolean mIsSendMessage = true;
	
	public OneByOneWork(Context context, Looper looper)
	{
		mContext = context;
		mLooper = looper;
		mWorkHandler = new WorkHandler(mLooper);
	}
	
	public interface OnWorkingObserver
	{
		public void onDoWork(int index, int workType);
		public void onOver(int overIndex, int totalCount);
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
	
	/**
	 * @param s
	 */
	public void setCallInternal(int internal)
	{
		Log.i(LOG_TAG, "setCallInternal: " + internal*1000);
		mCallInternal = internal*1000;
	}
	
	public void startWork(boolean isCall, boolean isSendMessage)
	{
		if (mContactPersonWrappers.size()==0)
		{
			Log.w(LOG_TAG, "not start, wrappers size is 0");
			return;
		}
		mIsCall = isCall;
		mIsSendMessage = isSendMessage;
		mWorkHandler.sendEmptyMessageDelayed(MSG_WHAT_CALL, 3000);
	}
	
	public void pauseWork()
	{
		mWorkHandler.removeCallbacksAndMessages(null);
	}
	
	public void stopWork()
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
					int indexOk = mCurWorkingIndex-1;
					if (mOnWorkingObserver != null)
					{
						mOnWorkingObserver.onDoWork(indexOk, ITEM_STATE_DONE_OK);
						mOnWorkingObserver.onOver(indexOk, mContactPersonWrappers.size());
					}
					Log.i(LOG_TAG, "end call : " + indexOk);
					endCall();
					// send message
					ContactPersonWrapper contactWrapper =  mContactPersonWrappers.get(indexOk);
					if (mIsSendMessage && contactWrapper.isSupportMessage())
					{
						MessageSender.instance().send(contactWrapper.getContactPerson().getPhoneNumber(), 
							contactWrapper.generateShortMessage(), null, null);
					}
					
					mCurWorkingIndex = 0;
					return;
				}
				
				Log.i(LOG_TAG, "calling index: " + mCurWorkingIndex);
				int indexOk = mCurWorkingIndex-1;
				if (indexOk >=0)
				{
					Log.i(LOG_TAG, "end call : " + indexOk);
					endCall();
					// send message
					ContactPersonWrapper contactWrapper =  mContactPersonWrappers.get(indexOk);
					if (mIsSendMessage && contactWrapper.isSupportMessage())
					{
						MessageSender.instance().send(contactWrapper.getContactPerson().getPhoneNumber(), 
								contactWrapper.generateShortMessage(), null, null);
					}
					
					if (mOnWorkingObserver != null)
					{
						mOnWorkingObserver.onDoWork(indexOk, ITEM_STATE_DONE_OK);
						mOnWorkingObserver.onOver(indexOk, mContactPersonWrappers.size());
					}
				}
				if (mOnWorkingObserver != null)
				{
					mOnWorkingObserver.onDoWork(mCurWorkingIndex, ITEM_STATE_CALLING);
				}
				ContactPersonWrapper contactWrapper =  mContactPersonWrappers.get(mCurWorkingIndex);
				Log.i(LOG_TAG, "to call : " + contactWrapper.getContactPerson().getName());
				// call
				if (mIsCall)
				{
					MicroPhone.instance(mContext).call(contactWrapper.getContactPerson().getPhoneNumber());
				}
				
				mCurWorkingIndex++;
				mWorkHandler.sendEmptyMessageDelayed(MSG_WHAT_CALL, mCallInternal);
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
