package cluelin.studytimer;

import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by cluel on 2017-12-10.
 */

public class StopWatch{

    //스톱워치의 상태를 위한 상수
    final static int IDLE = 0;

    final static int RUNNING = 1;

    final static int PAUSE = 2;

    int mStatus = IDLE;//처음 상태는 IDLE

    long mBaseTime;

    long mPauseTime;

    long initialTime = 0;

    Handler mTimer;

    public StopWatch(){

    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public void setmTimer(Handler mTimer) {
        this.mTimer = mTimer;
    }

    public long getBaseTime() {
        return mBaseTime;
    }

    public void start(){


        //타이머의 상태에 따라서 동작이 분기됨.
        switch (mStatus){
            case IDLE:
                mStatus = RUNNING;
                mBaseTime = SystemClock.elapsedRealtime() - initialTime;
                mTimer.sendEmptyMessage(0);
                break;
            case RUNNING:
                mTimer.removeMessages(0);
                mPauseTime = SystemClock.elapsedRealtime();
                mStatus = PAUSE;
                break;
            case PAUSE:
                mBaseTime += (SystemClock.elapsedRealtime() - mPauseTime);


                mTimer.sendEmptyMessage(0);
                mStatus = RUNNING;
                break;
        }



    }

    public void stop(){
        mTimer.removeMessages(0);
        mPauseTime = SystemClock.elapsedRealtime();
        mStatus = PAUSE;

    }


}
