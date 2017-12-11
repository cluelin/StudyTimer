package cluelin.studytimer;

import android.os.Handler;
import android.os.SystemClock;
import android.widget.TextView;

/**
 * Created by cluel on 2017-11-21.
 */

public class StudyItem {


    String itemName = "새로운 항목을 입력해주세요.";
    long recordingTime;
    StopWatch stopWatch = new StopWatch();
    TextView targetStopWatch;

    public StudyItem(){

    }

    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(long recordingTime) {
        this.recordingTime = recordingTime;
    }

    public void setTargetStopWatch(TextView targetStopWatch) {
        this.targetStopWatch = targetStopWatch;
    }

    Handler timerHandler = new Handler(){

        public void handleMessage(android.os.Message msg) {

            //텍스트뷰를 수정해준다.

            long ell = getEllapse();

            //경과한 시간 저장.
            setRecordingTime(ell);
            //경과한 시간을 포맷후 textview변경.
            String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);
            targetStopWatch.setText(sEll);


            //메시지를 다시 보낸다.
            timerHandler.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것

        };

    };

    long getEllapse(){

        long now = SystemClock.elapsedRealtime();

        long ell = now - stopWatch.getBaseTime();//현재 시간과 지난 시간을 빼서 ell값을 구하고

        return ell;

    }

    public Handler getTimerHandler() {
        return timerHandler;
    }

}
