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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //timer 객체 리스트.
    ArrayList<StudyItem> timerItems = new ArrayList<>();
    TimerListAdaptor timerListAdapter;   //타이머 리스트에 붙여줄 어댑터.

    final String COUNT = "COUNT";
    final String RECORD_TIME = "RECORD_TIME";
    final String ITEM_NAME = "itemName";

    final String FILE_NAME = "savefile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerListAdapter = new TimerListAdaptor(this, R.layout.new_timer_item, timerItems);

        ListView timerList = (ListView) findViewById(R.id.timer_list_view);
        timerList.setAdapter(timerListAdapter);




        if (savedInstanceState != null) {

            int count;
            ArrayList<String> recordTimes = new ArrayList<>();
            ArrayList<String> itemNames = new ArrayList<>();

            count = savedInstanceState.getInt(COUNT);

            for (int i = 0; i < count; i++) {
                StudyItem studyItem = new StudyItem();
                studyItem.setRecordingTime(Long.parseLong(recordTimes.get(i)));
                studyItem.setItemName(itemNames.get(i));
                timerItems.add(studyItem);
            }

        }else{
            try {
                BufferedReader in = new BufferedReader(new FileReader(new File(this.getFilesDir(), FILE_NAME)));
                int count = in.read();



                for (int i = 0; i < count; i++) {
                    StudyItem studyItem = new StudyItem();

                    studyItem.setItemName(in.readLine());

                    long initialTime = Long.parseLong(in.readLine());
                    studyItem.setRecordingTime(initialTime);
                    studyItem.getStopWatch().setInitialTime(initialTime);

                    timerItems.add(studyItem);
                }

                TimerListAdaptor.setCursor(-1);
                timerListAdapter.notifyDataSetChanged();


            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();

            } catch (IOException ioexception) {
                ioexception.printStackTrace();

            }
        }




    }


    protected void onDestroy() {

        Log.d("태그", "ondestroy");
        timerListAdapter.removeCursor();

        int itemCount = timerItems.size();
        ArrayList<String> RecordingTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            itemNames.add(timerItems.get(i).getItemName());
            RecordingTimes.add(((Long)timerItems.get(i).getRecordingTime()).toString());
        }



        try {
            PrintWriter out = new PrintWriter(new File(this.getFilesDir(), FILE_NAME));

            out.write(itemCount);

            for (int i = 0; i < itemCount; i++) {
                out.println(itemNames.get(i));
                out.println(RecordingTimes.get(i));
            }

            out.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }


        super.onDestroy();

    }

    @Override
    protected void onStop() {



        super.onStop();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d("태그", "onsaveInstance");
        int itemCount = timerItems.size();
        ArrayList<String> RecordingTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            RecordingTimes.add(((Long)timerItems.get(i).getRecordingTime()).toString());
            itemNames.add(timerItems.get(i).getItemName());
        }

        outState.putInt(COUNT, itemCount);
        outState.putStringArrayList(RECORD_TIME, RecordingTimes);
        outState.putStringArrayList(ITEM_NAME, itemNames);

        super.onSaveInstanceState(outState);
    }


    //on touch event Handler Add Item on XML.
    public void addTask(View v) {

        timerItems.add(new StudyItem());
        //list item 변경을 알려줌.
        timerListAdapter.notifyDataSetChanged();

    }

}

