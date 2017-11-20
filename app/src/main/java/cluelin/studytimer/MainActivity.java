package cluelin.studytimer;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<StudyItem> studyItems = new ArrayList<>();
    ListAdaptor adapter;

    final static int Init = 0;
    final static int Run = 1;
    final static int Pause = 2;

    int cur_Status = Init; //현재의 상태를 저장할변수를 초기화함.
    int myCount = 1;
    long myBaseTime;
    long myPauseTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new ListAdaptor(this, R.layout.new_item, studyItems);

        ListView itemList = (ListView) findViewById(R.id.itemList);
        itemList.setAdapter(adapter);


    }


    public void addTask(View v) {

        studyItems.add(new StudyItem());
        adapter.notifyDataSetChanged();

    }

    public void toggleStopWatch(View v) {

        Log.d("LogD", "터치이벤트");

        switch (v.getId()) {
            case R.id.textView: //시작버튼을 클릭했을때 현재 상태값에 따라 다른 동작을 할수있게끔 구현.
                switch (cur_Status) {
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        System.out.println(myBaseTime);
                        //myTimer이라는 핸들러를 빈 메세지를 보내서 호출
                        myTimer.sendEmptyMessage(0);
//                        myBtnRec.setEnabled(true); //기록버튼 활성
                        cur_Status = Run; //현재상태를 런상태로 변경
                        break;
                    case Run:
                        myTimer.removeMessages(0); //핸들러 메세지 제거
                        myPauseTime = SystemClock.elapsedRealtime();
                        cur_Status = Pause;
                        break;
                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        myBaseTime += (now - myPauseTime);
                        cur_Status = Run;
                        Log.d("LogD", "" + myBaseTime);
                        break;


                }
                break;

            //기록버튼 기능.
//            case R.id.btn_rec:
//                switch (cur_Status) {
//                    case Run:
//
//                        String str = myRec.getText().toString();
//                        str += String.format("%d. %s\n", myCount, getTimeOut());
//                        myRec.setText(str);
//                        myCount++; //카운트 증가
//
//                        break;
//                    case Pause:
//                        //핸들러를 멈춤
//                        myTimer.removeMessages(0);
//
//                        myBtnStart.setText("시작");
//                        myBtnRec.setText("기록");
//                        myOutput.setText("00:00:00");
//
//                        cur_Status = Init;
//                        myCount = 1;
//                        myRec.setText("");
//                        myBtnRec.setEnabled(false);
//                        break;
//
//
//                }
//                break;

        }
    }

    Handler myTimer = new Handler() {
        public void handleMessage(Notification.MessagingStyle.Message msg) {

            TextView stopWatch = (TextView)findViewById(R.id.stopWatch);

            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText(getTimeOut());

            stopWatch.setText(getTimeOut());

            Log.d("LogD", "" + getTimeOut());

            //sendEmptyMessage 는 비어있는 메세지를 Handler 에게 전송하는겁니다.
            myTimer.sendEmptyMessage(0);
        }
    };

    //현재시간을 계속 구해서 출력하는 메소드
    String getTimeOut() {
        long now = SystemClock.elapsedRealtime(); //애플리케이션이 실행되고나서 실제로 경과된 시간(??)^^;
        long outTime = now - myBaseTime;
        String easy_outTime = String.format("%02d:%02d:%02d", outTime / 1000 / 60, (outTime / 1000) % 60, (outTime % 1000) / 10);
        return easy_outTime;

    }
}

class ListAdaptor extends BaseAdapter {

    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<StudyItem> studyItems;

    public ListAdaptor(Context context, int layout, ArrayList<StudyItem> studyItems) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        EditText itemName = (EditText) convertView.findViewById(R.id.itemName);
        TextView stopWatch = (TextView) convertView.findViewById(R.id.stopWatch);

        StudyItem studyItem = (StudyItem)getItem(position);

        itemName.setHint(studyItem.getItemName());
        stopWatch.setText(studyItem.getStopWatch());


        return convertView;
    }

}
