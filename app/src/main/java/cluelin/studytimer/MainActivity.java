package cluelin.studytimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    //timer 객체 리스트.
    ArrayList<StudyItem> stopWatchItems = new ArrayList<>();
    TimerListAdaptor stopWatchListAdapter;   //타이머 리스트에 붙여줄 어댑터.

    final String COUNT = "COUNT";
    final String RECORD_TIME = "RECORD_TIME";
    final String ITEM_NAME = "itemName";

    final String FILE_NAME = "savefile.txt";

    final String LOG_DIR = "log";

    final SingleTon SINGLE_TON = SingleTon.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SINGLE_TON.setContext(this);

        Log.d("태그", "생명주기 : onCreate");

        startService(new Intent(this, StudyService.class));
        setContentView(R.layout.activity_main);

        //리스트 어댑터 만들어서 리스트에 할당.
        stopWatchListAdapter = new TimerListAdaptor(this, R.layout.new_stopwatch_item, stopWatchItems);

        ListView stopWatchListView = (ListView) findViewById(R.id.stopwatch_list_view);
        stopWatchListView.setAdapter(stopWatchListAdapter);


        stopWatchListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Log.d("태그", "행에서 롱클릭");
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                alert_confirm.setMessage("삭제?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                stopWatchListAdapter.removeItem(position);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                return false;
            }
        });


        //로그파일을 저장할 폴더 만들기.
        File logFile = new File(this.getFilesDir() + "/" + LOG_DIR);
        if (!logFile.exists())
            logFile.mkdir();


        //앱이 종료되면서 없어진 StopWatch랑 시간을
        //임시저장해둔 파일에서 가져온다.
        File file = new File(this.getFilesDir(), FILE_NAME);
        SINGLE_TON.loadWatchFromFile(file, stopWatchItems, stopWatchListAdapter);

        file.delete();


    }




    @Override
    public void onBackPressed() {


        //두번 뒤로가기 누르면 종료되도록.
        {
//            long tempTime = System.currentTimeMillis();
//            long intervalTime = tempTime - backPressedTime;
//
//            if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
//            {
//                super.onBackPressed();
//            }
//            else
//            {
//                backPressedTime = tempTime;
//                Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누르면 꺼버린다.", Toast.LENGTH_SHORT).show();
//            }
        }

        //타이머가 동작중이면 종료 안하게끔.

        //종료 안하고 홈으로 가기.
        moveTaskToBack(true);

        //타이머가 멈춘 상태이면 상태만 저장시키고 종료 되게끔.


    }

    void saveTask(String fileName) {

        Log.d("태그", "saveTask");
        stopWatchListAdapter.removeCursor();

        int itemCount = stopWatchItems.size();
        ArrayList<String> RecordingTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            itemNames.add(stopWatchItems.get(i).getItemName());
            RecordingTimes.add(((Long) stopWatchItems.get(i).getStopWatch().getRecordingTime()).toString());
        }

        try {
            Log.d("태그", "경로 : " + this.getFilesDir() + fileName);
            PrintWriter out = new PrintWriter(new File(this.getFilesDir(), fileName));

            out.write(itemCount);

            for (int i = 0; i < itemCount; i++) {
                out.println(itemNames.get(i));
                out.println(RecordingTimes.get(i));
            }

            out.close();
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }


    protected void onDestroy() {



        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("태그", "생명주기 : onStart");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.d("태그", "생명주기 : onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("태그", "생명주기 : onPause");
    }

    @Override
    protected void onStop() {

        Log.d("태그", "생명주기 : onStop");


        super.onStop();


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.d("태그", "생명주기 : onRestart");
    }



    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        Log.d("태그", "onRestoreInstanceState");


        Log.d("태그", "기록되돌리기! 호출");

        int count;
        ArrayList<String> recordTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        count = savedInstanceState.getInt(COUNT);

        for (int i = 0; i < count; i++) {
            StudyItem studyItem = new StudyItem();

            Log.d("tag", "recordTimes.get(i) : " + recordTimes.get(i));
            studyItem.getStopWatch().setRecordingTime(Long.parseLong(recordTimes.get(i)));
            studyItem.setItemName(itemNames.get(i));
            stopWatchItems.add(studyItem);
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d("태그", "onsaveInstance");
        int itemCount = stopWatchItems.size();
        ArrayList<String> RecordingTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            RecordingTimes.add(((Long) stopWatchItems.get(i).getStopWatch().getRecordingTime()).toString());
            itemNames.add(stopWatchItems.get(i).getItemName());
        }

        outState.putInt(COUNT, itemCount);
        outState.putStringArrayList(RECORD_TIME, RecordingTimes);
        outState.putStringArrayList(ITEM_NAME, itemNames);

        super.onSaveInstanceState(outState);
    }


    //on touch event Handler Add Item on XML.
    public void addTask(View v) {

        stopWatchItems.add(new StudyItem());
        //list item 변경을 알려줌.
        stopWatchListAdapter.notifyDataSetChanged();

    }


    //하루가 끝나고 그날 공부한 목록과 시간을 저장하는 기능.
    public void endDay(View v) {


        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");

        String getTime = "/log/" + sdf.format(date);

        //1. 저장.
        saveTask(getTime);


        //2. 삭제 (초기화)
        stopWatchListAdapter.initialize();


    }

    public void showLog(View v) {
        Intent intent = new Intent(MainActivity.this, LogPastDay.class);
        startActivityForResult(intent, SINGLE_TON.LOAD_PREVIOUS_STOP_WATCH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == SINGLE_TON.LOAD_PREVIOUS_STOP_WATCH && requestCode == SINGLE_TON.LOAD_PREVIOUS_STOP_WATCH) {

            Log.d("태그", "파일 경로 : " + data.getStringExtra(SINGLE_TON.TARGET_FILE));

            //기존 항목들을 정리하고
            stopWatchListAdapter.initialize();

            //저장되어있던 내용들을 가져온다.
            SINGLE_TON.loadWatchFromFile(new File(data.getStringExtra(SINGLE_TON.TARGET_FILE)),
                    stopWatchItems, stopWatchListAdapter);

        }
    }
}

