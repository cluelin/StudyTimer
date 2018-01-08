package cluelin.studytimer;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by cluel on 2017-11-21.
 */

public class StudyItem {


    String itemName = null;
    StopWatch stopWatch = new StopWatch();
    TextView targetStopWatch;

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


}
