package cluelin.studytimer;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by cluel on 2017-12-10.
 */

public class StopWatch{

    //스톱워치의 상태를 위한 상수
    final static int IDLE = 0;
    final static int RUNNING = 1;
    final static int PAUSE = 2;

    static int STATUS = IDLE;

    int instanceStatus = IDLE;//처음 상태는 IDLE

    long recordingTime;

    long mBaseTime;

    long mPauseTime;

    long initialTime = 0;

    Handler mTimer;


    public String getStringTime(long longTime){
        return String.format("%02d:%02d:%02d",
                longTime / 1000 / 60 / 60, longTime / 1000 / 60 % 60, (longTime/1000)%60);
    }

    public long getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(long recordingTime) {
        this.recordingTime = recordingTime;
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

    public void toggle(){


        //타이머의 상태에 따라서 동작이 분기됨.
        switch (instanceStatus){
            case IDLE:
                instanceStatus = RUNNING;
                STATUS = RUNNING;
                mBaseTime = SystemClock.elapsedRealtime() - initialTime;
                initialTime=0;
                mTimer.sendEmptyMessage(0);
                break;
            case RUNNING:
                mTimer.removeMessages(0);
                mPauseTime = SystemClock.elapsedRealtime();
                instanceStatus = PAUSE;
                STATUS = PAUSE;
                break;
            case PAUSE:
                mBaseTime += (SystemClock.elapsedRealtime() - mPauseTime);

                mTimer.sendEmptyMessage(0);
                STATUS = RUNNING;
                instanceStatus = RUNNING;
                break;
        }



    }

    public long getInitialTime() {
        return initialTime;
    }

    long getEllapse() {

        long now = SystemClock.elapsedRealtime();

        long ell = now - getBaseTime();//현재 시간과 지난 시간을 빼서 ell값을 구하고

        return ell;

    }


}
