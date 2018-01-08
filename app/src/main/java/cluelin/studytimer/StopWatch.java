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

    //전체 스탑워치 상태를 표기.
    static int STATUS = IDLE;

    //각 아이템별 스탑워치 상태를 표시.
    int instanceStatus = IDLE;//처음 상태는 IDLE

    //측정된 시간을 저장.
    long initialTime = 0;

    //시간을 계산하기 위한 근간이 되는 시점.
    long mBaseTime;

    //일시 정지 시점.
    long mPauseTime;

    Handler mTimer;

    int position;

    public void setPosition(int position) {
        this.position = position;
    }

    public String getStringTime(long longTime){
        return String.format("%02d:%02d:%02d",
                longTime / 1000 / 60 / 60, longTime / 1000 / 60 % 60, (longTime/1000)%60);
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }


    public void setmTimer(Handler mTimer) {
        this.mTimer = mTimer;
    }

    public void setmBaseTime(long mBaseTime) {
        this.mBaseTime = mBaseTime;
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
                mTimer.sendEmptyMessage(position);
                break;
            case RUNNING:
                stop();

                break;
            case PAUSE:
                mBaseTime += (SystemClock.elapsedRealtime() - mPauseTime);

                mTimer.sendEmptyMessage(position);
                STATUS = RUNNING;
                instanceStatus = RUNNING;
                break;
        }



    }

    public void stop(){

        mTimer.removeMessages(position);
        mPauseTime = SystemClock.elapsedRealtime();
        instanceStatus = PAUSE;
        STATUS = PAUSE;

    }


    public void pauseWrite(){
        mTimer.removeMessages(position);

    }

    public void restartWrite(){
        mTimer.sendEmptyMessage(position);
    }


    long getEllapse() {

        long now = SystemClock.elapsedRealtime();

        long ell = now - getBaseTime();//현재 시간과 지난 시간을 빼서 ell값을 구하고

        return ell;

    }


}
