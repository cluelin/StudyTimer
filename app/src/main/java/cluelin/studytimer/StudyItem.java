package cluelin.studytimer;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Created by cluel on 2017-11-21.
 */

public class StudyItem {


    String itemName = "새로운 항목을 입력해주세요.";
    StopWatch stopWatch = new StopWatch();
    TextView targetStopWatch;


    Handler timerHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            //텍스트뷰를 수정해준다.

            long ell = getStopWatch().getEllapse();

            //경과한 시간 저장.
            stopWatch.setRecordingTime(ell);

            //경과한 시간을 포맷후 textview변경.
            String sEll = stopWatch.getStringTime(ell);
            targetStopWatch.setText(sEll);


            //메시지를 다시 보낸다.
            timerHandler.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것

        }
    };


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public StopWatch getStopWatch() {
        return stopWatch;
    }


    public void setTargetStopWatch(TextView targetStopWatch) {
        this.targetStopWatch = targetStopWatch;
    }




    public Handler getTimerHandler() {
        return timerHandler;
    }

}
