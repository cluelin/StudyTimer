package cluelin.studytimer;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by cluel on 2017-12-10.
 */

public class StopWatch {

    private static StopWatch instance = null;

    //스톱워치의 상태를 위한 상수

    final static int IDLE = 0;

    final static int RUNNING = 1;

    final static int PAUSE = 2;

    int mStatus = IDLE;//처음 상태는 IDLE

    long mBaseTime;

    long mPauseTime;

    int mSplitCount;


//싱글톤
    private StopWatch(){


    }

    public static StopWatch getInstance(){

        if(instance == null){
            instance = new StopWatch();
        }

        return instance;

    }


    String getEllapse(){

        long now = SystemClock.elapsedRealtime();

        long ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고

        //아래에서 포맷을 예쁘게 바꾼다음 리턴해준다.

        String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);

        return sEll;

    }


    public long startWatch(Handler mTimer){


        //타이머의 상태에 따라서 동작이 분기됨.
        switch (mStatus){
            case IDLE:
                mStatus = RUNNING;
                return SystemClock.elapsedRealtime();
            case RUNNING:
                mStatus = PAUSE;
                return SystemClock.elapsedRealtime();
            case PAUSE:


        }


        return 0;
    }


}
