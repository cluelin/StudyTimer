package cluelin.studytimer;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //timer 객체 리스트.
    ArrayList<StudyItem> timerItems = new ArrayList<>();
    TimerListAdaptor timerListAdapter;   //타이머 리스트에 붙여줄 어댑터.

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


        timerListAdapter = new TimerListAdaptor(this, R.layout.new_timer_item, timerItems);

        ListView timerList = (ListView) findViewById(R.id.timer_list_view);
        timerList.setAdapter(timerListAdapter);


    }


    //on touch event Handler Add Item on XML.
    public void addTask(View v) {

        timerItems.add(new StudyItem());
        //list item 변경을 알려줌.
        timerListAdapter.notifyDataSetChanged();

    }

}

