package com.binxin.zdapp.classes;

import android.os.Handler;  
import android.os.Message;  

//邠心工作室
//开放源代码之CountdownTimer
//2015.01.21
//正德应用 6.6
public abstract class CountdownTimer
{
	private final long mCountdownInterval;//单位时间
    private long mTotalTime;	//倒计时时间
    private long mRemainTime;
	private static final int MSG_RUN = 1;
	private static final int MSG_PAUSE = 2;
	private int Total_MAX = 720;
	
    public CountdownTimer(long millisInFuture, long countDownInterval, int max)
	{
		mTotalTime = millisInFuture;
		mCountdownInterval = countDownInterval;
		mRemainTime = millisInFuture;
		Total_MAX = max;
    }  

	public final void seek(int value)
	{
        synchronized (CountdownTimer.this)
		{
			mRemainTime = ((100 - value) * mTotalTime) / 100;
		}
    }
	
    public final void cancel()
	{
		mHandler.removeMessages(MSG_RUN);
		mHandler.removeMessages(MSG_PAUSE);
	}

	public final void resume()
	{
		mHandler.removeMessages(MSG_PAUSE);
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
	}

    public final void pause()
	{
		mHandler.removeMessages(MSG_RUN);
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));  
    }
	
	public synchronized final CountdownTimer start()
	{
		if (mRemainTime <= 0)
		{
			onFinish();
			return this;
		}
		mHandler.sendEmptyMessage(MSG_RUN);
		return this;
	}
	
	public abstract void onTick(long millisUntilFinished, int percent);  

	public abstract void onFinish();
	
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			synchronized (CountdownTimer.this)
			{
				if (msg.what == MSG_RUN)
				{
					mRemainTime = mRemainTime - mCountdownInterval;
					if (mRemainTime <= 0)
					{
						onFinish();
                    }
					/*else if (mRemainTime < mCountdownInterval)
					{  
                        sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);  
                    }*/
					else
					{
						onTick(mRemainTime, new Long(Total_MAX * (mTotalTime - mRemainTime) / mTotalTime).intValue());
						sendEmptyMessageDelayed(MSG_RUN, mCountdownInterval);
					}
				}
				else if (msg.what == MSG_PAUSE)
				{
					
				}
			}
		}
	};
}
