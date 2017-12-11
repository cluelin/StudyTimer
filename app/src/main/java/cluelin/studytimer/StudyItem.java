package cluelin.studytimer;

/**
 * Created by cluel on 2017-11-21.
 */

public class StudyItem {

    int timerID;
    String itemName = "새로운 항목을 입력해주세요.";
    String recordingTime = "00:00:00";
    StopWatch stopWatch = new StopWatch();


    public StopWatch getStopWatch() {
        return stopWatch;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    public int getTimerID() {
        return timerID;
    }

    public void setTimerID(int timerID) {
        this.timerID = timerID;
    }
}
