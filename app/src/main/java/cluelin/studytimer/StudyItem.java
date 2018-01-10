package cluelin.studytimer;

import android.os.Handler;
import android.widget.TextView;

/**
 * Created by cluel on 2017-11-21.
 */

public class StudyItem {


    String itemName;
    StopWatch stopWatch;
    TextView targetStopWatch;

    StudyItem(){

        itemName = null;
        stopWatch  = new StopWatch();
        targetStopWatch = null;
    }

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
