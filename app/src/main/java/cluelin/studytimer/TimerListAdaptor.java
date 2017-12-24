package cluelin.studytimer;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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


    public static void setCursor(int cursor) {
        TimerListAdaptor.cursor = cursor;
    }

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

        //각 뷰의 초기 설정을 해준다.
        //실질적으로 보여지는 각 행은 convertView에 담겨진다.

        final EditText itemName = (EditText) convertView.findViewById(R.id.itemName);
        TextView stopWatchTextView = (TextView) convertView.findViewById(R.id.recordingTime);

        final StudyItem studyItem = (StudyItem) getItem(position);
        itemName.setHint(studyItem.getItemName());

        long ell = studyItem.getStopWatch().getRecordingTime();

        //시 분 초 로 나눠준다.
        String sEll = studyItem.getStopWatch().getStringTime();
        stopWatchTextView.setText(sEll);


        itemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                studyItem.setItemName(itemName.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        stopWatchTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        // 스톱워치 부분을 터치 했을 때 이벤트 발생
        stopWatchTextView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                //이전에 작동하던 스톱워치가 아니면서
                //처음 시작하는 값이 아닌경우
                //이전 스톱워치를 정지시킨다.
                if (cursor != position) {
                    removeCursor();
                }


                //현재값을 커서로 지정해주고.
                //현재 타이머를 작동시킨다.
                cursor = position;
                StudyItem studyItem = (StudyItem) getItem(position);
                studyItem.setTargetStopWatch((TextView) v);

                StopWatch stopWatch = studyItem.getStopWatch();
                stopWatch.setmTimer(studyItem.getTimerHandler());

                stopWatch.start();


            }
        });


        return convertView;
    }

    public void removeCursor() {

        if (cursor != -1) {
            StudyItem studyItem = (StudyItem) getItem(cursor);
            StopWatch stopWatch = studyItem.getStopWatch();
            stopWatch.stop();
        }

    }

    public void initialize() {
        studyItems.clear();

        cursor = -1;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        studyItems.remove(position);
        notifyDataSetChanged();
    }


}
