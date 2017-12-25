package cluelin.studytimer;

import android.content.Context;
import android.os.Handler;
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

    public static TimerListAdaptor instance = null;

    static int CURSOR = -1;
    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<StudyItem> studyItems;

    static public TimerListAdaptor getInstance(Context context, int layout, ArrayList<StudyItem> studyItems){
        if(instance == null)
            instance = new TimerListAdaptor(context, layout, studyItems);

        return instance;
    }

    private TimerListAdaptor(Context context, int layout, ArrayList<StudyItem> studyItems) {
        this.context = context;
        this.layout = layout;
        this.studyItems = studyItems;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public static void setCURSOR(int cursor) {
        TimerListAdaptor.CURSOR = cursor;
    }



    @Override
    public int getCount() {
        return studyItems.size();
    }

    @Override
    public StudyItem getItem(int position) {
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

        long initialTime = studyItem.getStopWatch().getInitialTime();

        //시 분 초 로 나눠준다.
        String sEll = studyItem.getStopWatch().getStringTime(initialTime);
        stopWatchTextView.setText(sEll);



        studyItem.setTargetStopWatch((TextView) stopWatchTextView);

        StopWatch stopWatch = studyItem.getStopWatch();
        stopWatch.setmTimer(studyItem.getTimerHandler());


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
                if (CURSOR != position) {
                    removeCursor();
                }


                //현재값을 커서로 지정해주고.
                //현재 타이머를 작동시킨다.
                CURSOR = position;
                StudyItem studyItem = (StudyItem) getItem(position);
                studyItem.setTargetStopWatch((TextView) v);

                StopWatch stopWatch = studyItem.getStopWatch();
                stopWatch.setmTimer(studyItem.getTimerHandler());

                stopWatch.toggle();


            }
        });


        return convertView;
    }

    public void removeCursor() {

        if (CURSOR != -1) {
            StudyItem studyItem = (StudyItem) getItem(CURSOR);
            StopWatch stopWatch = studyItem.getStopWatch();
            stopWatch.toggle();
        }

    }


    public void addItem(){
        studyItems.add(new StudyItem());
        //list item 변경을 알려줌.
        notifyDataSetChanged();
    }

    public void addItem(StudyItem item){
        studyItems.add(item);
        //list item 변경을 알려줌.
        notifyDataSetChanged();
    }


    //리스트를 초기화할때 사용.
    public void initialize() {
        studyItems.clear();

        CURSOR = -1;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {

        studyItems.remove(position);
        //CURSOR도 변경해줘야함.
        if(CURSOR == position){
            CURSOR = -1;
        }else if(CURSOR > position)
            CURSOR--;

        notifyDataSetChanged();
    }


}
