package cluelin.studytimer;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by cluel on 2017-12-10.
 */

public class StopWatch {



    //스톱워치의 상태를 위한 상수

    final static int IDLE = 0;

    final static int RUNNING = 1;

    final static int PAUSE = 2;

    int mStatus = IDLE;//처음 상태는 IDLE

    long mBaseTime;

    long mPauseTime;


    TextView targetStopWatch;

    public StopWatch(){

    }

    Handler mTimer = new Handler(){

        public void setTextView(TextView textView){
            targetStopWatch = textView;
        }
        //핸들러는 기본적으로 handleMessage에서 처리한다.

        public void handleMessage(android.os.Message msg) {

            //텍스트뷰를 수정해준다.

            targetStopWatch.setText(getEllapse());

            //메시지를 다시 보낸다.

            mTimer.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것

        };

    };

    public TextView getTargetStopWatch() {
        return targetStopWatch;
    }

    public void setTargetStopWatch(TextView targetStopWatch) {
        this.targetStopWatch = targetStopWatch;
    }

    String getEllapse(){

        long now = SystemClock.elapsedRealtime();

        long ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고

        Log.d("태그", " : "+now + " : "+ mBaseTime);
        //아래에서 포맷을 예쁘게 바꾼다음 리턴해준다.

        String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);

        return sEll;

    }


    public void start(){


        //타이머의 상태에 따라서 동작이 분기됨.
        switch (mStatus){
            case IDLE:
                mStatus = RUNNING;
                mBaseTime = SystemClock.elapsedRealtime();
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
