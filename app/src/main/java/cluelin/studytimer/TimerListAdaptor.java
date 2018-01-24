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

    //현재 실행되고있는 행의 위치를 보여줌.
    static int CURSOR = -1;
    static boolean RESTORE = false;

    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<StudyItem> studyItems;

    //스톱워치를 변환시키기 위한 핸들러
    Handler timerHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            super.handleMessage(msg);

            int position = msg.what;

            StopWatch stopWatch = studyItems.get(position).getStopWatch();
            TextView targetStopWatch = studyItems.get(position).targetStopWatch;

            long ell = stopWatch.getEllapse();

            String sEll = getStringTime(ell);

            if (sEll.compareTo(targetStopWatch.getText().toString()) != 0) {
                targetStopWatch.setText(sEll);
                SingleTon.getInstance().updateNotification(studyItems.get(position).itemName, sEll);
                Log.d("태그", "핸들러 작동중 시간 sEll : " + sEll);
                Log.d("태그", "핸들러 작동중 시간 targetStopWatch.getText() : " + targetStopWatch.getText());
            }

            timerHandler.sendEmptyMessage(position);//0은 메시지를 구분하기 위한 것


        }
    };


    static public TimerListAdaptor getInstance() {
        if (instance != null)
            return instance;

        return null;
    }

    static public TimerListAdaptor getInstance(Context context, int layout, ArrayList<StudyItem> studyItems) {
        if (instance == null)
            instance = new TimerListAdaptor(context, layout, studyItems);

        return instance;
    }

    private TimerListAdaptor(Context context, int layout, ArrayList<StudyItem> studyItems) {
        this.context = context;
        this.layout = layout;
        this.studyItems = studyItems;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public String getStringTime(long longTime) {
        return String.format("%02d:%02d:%02d",
                longTime / 1000 / 60 / 60, longTime / 1000 / 60 % 60, (longTime / 1000) % 60);
    }

    public Handler getTimerHandler() {
        return timerHandler;
    }

    public static void setCURSOR(int cursor) {
        CURSOR = cursor;
        Log.d("태그", "커서 값 : " + CURSOR);
    }

    public static boolean getTimerRun(){
        if(CURSOR != -1)
            return true;
        else
            return false;
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


    //현재 동작하고있는 스톱워치를 중단시키는 작업.
    public void stopCursor() {

        Log.d("태그", "request stop cursor : " + CURSOR);

        if (getTimerRun()) {
            StudyItem studyItem = (StudyItem) getItem(CURSOR);
            StopWatch stopWatch = studyItem.getStopWatch();
            stopWatch.stop();

            setCURSOR(-1);
        }

    }

    public void startCursor() {
        Log.d("태그", "request start cursor : " + CURSOR);

        if (CURSOR != -1) {
            StudyItem studyItem = (StudyItem) getItem(CURSOR);
            StopWatch stopWatch = studyItem.getStopWatch();
            stopWatch.start();

        }
    }


    public void addItem() {
        studyItems.add(new StudyItem());
        //list item 변경을 알려줌.
        notifyDataSetChanged();
    }

    public void addItem(StudyItem item) {
        studyItems.add(item);
        //list item 변경을 알려줌.
        notifyDataSetChanged();
    }


    //리스트를 초기화할때 사용.
    public void clearAdaptor() {

        stopCursor();

        studyItems.clear();

        notifyDataSetChanged();
    }


    //특정 행을 제거함. 동작하고있는 경우 cursor도 변경해주는 작업을 해줘야함.
    public void removeItem(int position) {

        int temp = CURSOR;
        stopCursor();


        if (temp > position)
            temp--;
        else if (temp == position)
            temp = -1;

        setCURSOR(temp);
        startCursor();

        studyItems.remove(position);
        notifyDataSetChanged();

        //start cursor를 해주고싶은데 notifyDataSetChange가 더 늦게 호출되어버려서
        //꼬이는 문제 발생. 지우고 나서 수동으로 다시 재 시작 해줘야한다는게 좀 에바다..

    }


    //각 행을 적어줄때 기본적인 사항을 여기서 세팅해준다.
    void initiateRow(View convertView, final int position) {
        final EditText itemNameEditText = (EditText) convertView.findViewById(R.id.itemName);
        TextView stopWatchTextView = (TextView) convertView.findViewById(R.id.recordedTime);

        final StudyItem studyItem = (StudyItem) getItem(position);


        //studyItem을 불러오기 해서 가져와잇는 경우 가지고있는 이름을 할당해주고.
        //아닐 경우 새로운 입력을 요구.
        if (studyItem.getItemName() == null)
            itemNameEditText.setHint("새로운 항목을 입력해주세요.");
        else
            itemNameEditText.setText(studyItem.getItemName());

        Log.d("태그", "상태 : " + StopWatch.STATUS);

        //position 이랑 커서가 다를때만 되게해놧는데.. 이거 어디서 에러처리한거더라.
        //나중에 필요할때 다시 가져다가 써야됨.
        if (position != CURSOR) {
            //각항목이 가지고있는 initial 타임을 설정해준다. 기본적으로는 0이고
            //불러오기해서 가져오는 경우 저장된 타임이 초기값으로 등록된다.
            long recordedTime = studyItem.getStopWatch().getRecordedTime();
            String sEll = getStringTime(recordedTime);
            stopWatchTextView.setText(sEll);
            Log.d("태그", "initial Time : " + sEll);
        }


        //각 studyItem 별로 시간이 표시될 StopWatch TextView를 지정해준다.
        studyItem.setTargetStopWatch(stopWatchTextView);

        //각 studyItem이 가지고있는 stopWatch에 위치를 지정해줌.
        //이 포지션을 이용해서 각 StopWatch가 핸들러에게 자신의 메세지를 보냄.
        StopWatch stopWatch = studyItem.getStopWatch();
        stopWatch.setPosition(position);


        //항목 명이 변경될때 studyItem의 name도 변경될 수있도록 조치.
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

        itemNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//
        //이거해야지 행 클릭했을때의 long click이 동작하더라.. 행 삭제용으로 만들어둠.
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

                onClickTimer(position);

            }


        });
    }

    public void onClickTimer(int position) {
        StopWatch stopWatch = ((StudyItem) getItem(position)).getStopWatch();

        Log.d("태그", "onclick stopwatch position : " + position);
        Log.d("태그", "onclick stopwatch cursor : " + CURSOR);

        //이전에 작동하던 스톱워치가 아니면서
        //처음 시작하는 값이 아닌경우
        //이전 스톱워치를 정지시킨다.
        if (CURSOR != position) {
            stopCursor();
            //현재값을 커서로 지정해주고.
            //현재 타이머를 작동시킨다.
            setCURSOR(position);
            stopWatch.setSTATUS(StopWatch.RUNNING);
            SingleTon.getInstance().notificationStopWatch();
        } else {
            //커서랑 포지션이 일치할때 정지.
            stopWatch.setSTATUS(StopWatch.PAUSE);
            setCURSOR(-1);
            SingleTon.getInstance().removeNotification();

        }

        //position이랑 일치하면 정지
        //position이랑 다르면 시작.
        stopWatch.toggle();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //실질적으로 보여지는 각 행은 convertView에 담겨진다.
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        Log.d("태그", "getview 호출 시점");

        //각 뷰의 초기 설정을 해준다.
        //저장된것에서 불려져서 초기값이 잇을경우 초기값으로 보이게해준다.
        initiateRow(convertView, position);


        //복구되면 다시 스톱워치 실행 실행해야함.
        //개 더러운 코드 ㅈㅅ.
        if (RESTORE && position + 1 == getCount()) {
            Log.d("태그", "리스토어됨.");

            StopWatch stopWatch = ((StudyItem) getItem(CURSOR)).getStopWatch();

            Log.d("태그", "base time :" + stopWatch.getBaseTime());
            timerHandler.sendEmptyMessage(CURSOR);
            stopWatch.setSTATUS(StopWatch.RUNNING);
            RESTORE = false;

        }

        return convertView;
    }


}
