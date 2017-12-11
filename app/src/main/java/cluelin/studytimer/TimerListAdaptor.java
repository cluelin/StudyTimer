package cluelin.studytimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cluel on 2017-12-10.
 */

public class TimerListAdaptor extends BaseAdapter {

    static int cursor = -1;
    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<StudyItem> studyItems;


    public TimerListAdaptor(Context context, int layout, ArrayList<StudyItem> studyItems) {
        this.context = context;
        this.layout = layout;
        this.studyItems = studyItems;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return studyItems.size();
    }

    @Override
    public Object getItem(int position) {
        return studyItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        EditText itemName = (EditText) convertView.findViewById(R.id.itemName);
        TextView stopWatch = (TextView) convertView.findViewById(R.id.recordingTime);

        StudyItem studyItem = (StudyItem) getItem(position);

        itemName.setHint(studyItem.getItemName());
        stopWatch.setText(studyItem.getRecordingTime());


        // 스톱워치 부분을 터치 했을 때 이벤트 발생
        stopWatch.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (cursor != position && cursor != -1  ) {
                    StudyItem studyItem = (StudyItem) getItem(cursor);
                    StopWatch stopWatch = studyItem.getStopWatch();
                    stopWatch.stop();
                }


                StudyItem studyItem = (StudyItem) getItem(position);
                cursor = position;
                StopWatch stopWatch = studyItem.getStopWatch();

                stopWatch.setTargetStopWatch((TextView) v);

                stopWatch.start();


            }
        });


        return convertView;
    }
}
