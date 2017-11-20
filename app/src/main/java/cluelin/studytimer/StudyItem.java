package cluelin.studytimer;

/**
 * Created by cluel on 2017-11-21.
 */

public class StudyItem {
    String itemName = "새로운 항목을 입력해주세요.";
    String stopWatch = "00:00:00";

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getStopWatch() {
        return stopWatch;
    }

    public void setStopWatch(String stopWatch) {
        this.stopWatch = stopWatch;
    }
}
