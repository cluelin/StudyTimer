package cluelin.studytimer;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

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
    long mBaseTime = 0;

    //일시 정지 시점.
    long mPauseTime = 0;

    Handler mTimer;

    int position;

    public StopWatch(){
        mTimer = TimerListAdaptor.getInstance().getTimerHandler();

        if(mTimer == null)
            Log.d("태그", "Adapter가 할당이 안됨; ");
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
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

                initial();

                break;
            case RUNNING:
                stop();

                break;
            case PAUSE:

                start();

                break;
        }
    }

    public void setSTATUS(int STATUS) {

        Log.d("태그", position + "에서 status change : " + STATUS);
        StopWatch.STATUS = STATUS;
    }

    public void initial(){

        Log.d("태그", position + "번째 타이머 시작! initial! 초기 값 : " + initialTime);


        mBaseTime = SystemClock.elapsedRealtime() - initialTime;
        mTimer.sendEmptyMessage(position);

        instanceStatus = RUNNING;

    }

    public void start(){

        Log.d("태그", position + "번째 타이머 시작! start ");


        mBaseTime += (SystemClock.elapsedRealtime() - mPauseTime);

        mTimer.sendEmptyMessage(position);

        instanceStatus = RUNNING;


    }

    public void stop(){

        mTimer.removeMessages(position);
        mPauseTime = SystemClock.elapsedRealtime();
        instanceStatus = PAUSE;

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


    //각 스톱워치에 저장된 시간을 반환.
    long getRecordedTime(){

        //mBaseTime이 초기값일 경우 초기값을 반환해주면 해결.
        if (instanceStatus == IDLE){
            return initialTime;
        }

        mBaseTime += (SystemClock.elapsedRealtime() - mPauseTime);

        stop();

        return getEllapse();

    }


}
