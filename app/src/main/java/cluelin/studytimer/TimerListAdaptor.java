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

    Handler timerHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            super.handleMessage(msg);

            Log.d("핸들", "msg.what" + msg.what);

            int position = msg.what;

            StopWatch stopWatch = studyItems.get(position).getStopWatch();


            long ell = stopWatch.getEllapse();


            //경과한 시간을 포맷후 textview변경.
            String sEll = stopWatch.getStringTime(ell);
            studyItems.get(position).targetStopWatch.setText(sEll);


            //메시지를 다시 보낸다.
            timerHandler.sendEmptyMessage(position);//0은 메시지를 구분하기 위한 것

        }
    };

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


    public Handler getTimerHandler() {
        return timerHandler;
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

    public void stopCursor() {

        Log.d("태그", "request stop cursor : " + CURSOR);

        if (CURSOR != -1) {
            StudyItem studyItem = (StudyItem) getItem(CURSOR);
            StopWatch stopWatch = studyItem.getStopWatch();
            stopWatch.stop();

            CURSOR = -1;
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
    public void clearAdaptor() {
        studyItems.clear();

        stopCursor();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {


        //CURSOR도 변경해줘야함.
        if(CURSOR == position){
            CURSOR = -1;
        }else if(CURSOR > position)
            CURSOR--;

        studyItems.get(position).getStopWatch().stop();

        studyItems.remove(position);
        notifyDataSetChanged();
    }


    void initiateRow(View convertView, final int position){
        final EditText itemNameEditText = (EditText) convertView.findViewById(R.id.itemName);
        TextView stopWatchTextView = (TextView) convertView.findViewById(R.id.recordedTime);

        final StudyItem studyItem = (StudyItem) getItem(position);

        if(studyItem.getItemName() == null)
            itemNameEditText.setHint("새로운 항목을 입력해주세요.");
        else
            itemNameEditText.setText(studyItem.getItemName());

        long initialTime = studyItem.getStopWatch().getInitialTime();

        //시 분 초 로 나눠준다.
        String sEll = studyItem.getStopWatch().getStringTime(initialTime);
        stopWatchTextView.setText(sEll);



        studyItem.setTargetStopWatch(stopWatchTextView);

        StopWatch stopWatch = studyItem.getStopWatch();
        stopWatch.setPosition(position);
        stopWatch.setmTimer(timerHandler);

        itemNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                studyItem.setItemName(itemNameEditText.getText().toString());
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


                Log.d("태그", "onclick stopwatch position : " + position);
                Log.d("태그", "onclick stopwatch cursor : " + CURSOR);
                //이전에 작동하던 스톱워치가 아니면서
                //처음 시작하는 값이 아닌경우
                //이전 스톱워치를 정지시킨다.
                if (CURSOR != position) {
                    stopCursor();
                }


                //현재값을 커서로 지정해주고.
                //현재 타이머를 작동시킨다.
                CURSOR = position;


                StopWatch stopWatch = studyItem.getStopWatch();
                stopWatch.toggle();



            }
        });
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //실질적으로 보여지는 각 행은 convertView에 담겨진다.
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        Log.d("태그", "getview 호출 시점" );


        //각 뷰의 초기 설정을 해준다.
        //저장된것에서 불려져서 초기값이 잇을경우 초기값으로 보이게해준다.
        initiateRow(convertView, position);





        return convertView;
    }




}
