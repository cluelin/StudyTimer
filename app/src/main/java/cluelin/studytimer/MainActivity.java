package cluelin.studytimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //timer 객체 리스트.
    TimerListAdaptor stopWatchListAdapter;   //타이머 리스트에 붙여줄 어댑터.


    //onSaveInstanceState restoreInstanceState용 상수.
    final String CURSOR = "CURSOR";
    final String COUNT = "COUNT";
    final String BASE_TIME = "BASE_TIME";
    final String ITEM_NAME = "itemName";


    //로그 파일 저장 경로
    final String LOG_DIR = "log";

    final SingleTon SINGLE_TON = SingleTon.getInstance();


    boolean saveInstanceState = false;
    boolean ON_STOP = false;
    int cursor;

    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("태그", "생명주기 : onCreate");


        setListView();


        //서비스가 동작되어야지 어플 kill되는거 탐지 가능.
        startService(new Intent(this, StudyService.class));

        //싱글톤 클래스에 필요한 정보 전달.
        SINGLE_TON.setContext(this);
        SINGLE_TON.setStopWatchListAdapter(stopWatchListAdapter);


        makeLogDir();
        loadTempFile();


    }

    void makeLogDir() {
        //로그파일을 저장할 폴더 만들기.
        File logFile = new File(this.getFilesDir() + "/" + LOG_DIR);
        if (!logFile.exists())
            logFile.mkdir();
    }

    void loadTempFile() {
        //앱이 종료되면서 없어진 StopWatch랑 시간을
        //임시저장해둔 파일에서 가져온다.
        File file = new File(this.getFilesDir(), SINGLE_TON.FILE_NAME);
        SINGLE_TON.loadWatchFromFile(file, stopWatchListAdapter);

        file.delete();
    }

    void setListView() {

        //리스트 어댑터 만들어서 리스트에 할당.
        ArrayList<StudyItem> stopWatchItems = new ArrayList<>();
        stopWatchListAdapter = TimerListAdaptor.getInstance(this, R.layout.new_stopwatch_item, stopWatchItems);

        ListView stopWatchListView = (ListView) findViewById(R.id.stopwatch_list_view);
        stopWatchListView.setAdapter(stopWatchListAdapter);


        //리스트뷰의 각 행을 롱클릭하면 삭제할수있도록..
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

        handler = stopWatchListAdapter.getTimerHandler();
    }

    @Override
    public void onBackPressed() {
        Log.d("태그", "onBackPressed");

        //타이머가 동작중이면 종료 안하게끔.
        if (StopWatch.STATUS == StopWatch.RUNNING) {
            //종료 안하고 홈으로 가기.
            Log.d("태그", "스톱워치 동작중 진행.");
            moveTaskToBack(true);
        } else {

            //타이머가 멈춘 상태이면 상태만 저장시키고 종료 되게끔.
            Log.d("태그", "동작안함 중지");

            SINGLE_TON.saveTask(SINGLE_TON.FILE_NAME);
            stopWatchListAdapter.clearAdaptor();

            super.onBackPressed();
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
    protected void onPause() {
        super.onPause();

        Log.d("태그", "생명주기 : onPause");
    }

    @Override
    protected void onStop() {

        Log.d("태그", "생명주기 : onStop");

        Log.d("태그", "StopWatch.STATUS : " + StopWatch.STATUS);
        Log.d("태그", "stopWatchListAdapter.CURSOR : " + stopWatchListAdapter.CURSOR);



        if(StopWatch.STATUS == StopWatch.RUNNING){
            stopWatchListAdapter.getItem(stopWatchListAdapter.CURSOR).getStopWatch().pauseWrite();
            ON_STOP = true;
        }


        super.onStop();


    }

    @Override
    protected void onResume(){

        Log.d("태그", "생명주기 : onResume");

        if(ON_STOP){

            Log.d("태그", "onRestoreInstanceState 호출된 후에 오류날걸?");
            stopWatchListAdapter.getItem(stopWatchListAdapter.CURSOR).getStopWatch().restartWrite();
            ON_STOP=false;
        }


        super.onResume();
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

        Log.d("태그", "cursor : "+savedInstanceState.getString(CURSOR));


//        int count;
//        ArrayList<String> baseTimes;
//        ArrayList<String> itemNames;
//
//        TimerListAdaptor.CURSOR = savedInstanceState.getInt(CURSOR);
//        count = savedInstanceState.getInt(COUNT);
//        baseTimes = savedInstanceState.getStringArrayList(BASE_TIME);
//        itemNames = savedInstanceState.getStringArrayList(ITEM_NAME);
//
//
//        Log.d("태그", "count : " + count);
//
//        for (int i = 0; i < count; i++) {
//            StudyItem studyItem = new StudyItem();
//
//            Log.d("태그", "basetime.get(i) : " + baseTimes.get(i));
//
//            studyItem.getStopWatch().setInitialTime(
//                    SystemClock.elapsedRealtime() - Long.parseLong(baseTimes.get(i)));
//            studyItem.setItemName(itemNames.get(i));
//            stopWatchListAdapter.addItem(studyItem);
//            stopWatchListAdapter.notifyDataSetChanged();
//        }


        Log.d("태그", "onRestoreInstanceState 호출 마지막 시점");
//        stopWatchListAdapter.getItem(TimerListAdaptor.CURSOR).getStopWatch().setmTimer(stopWatchListAdapter.timerHandler);
//        stopWatchListAdapter.getItem(TimerListAdaptor.CURSOR).getStopWatch().toggle();



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.d("태그", "onsaveInstance");

//        int itemCount = stopWatchListAdapter.getCount();
//
//        if(itemCount == 0)
//            super.onSaveInstanceState(outState);
//
//        ArrayList<String> baseTimes = new ArrayList<>();
//        ArrayList<String> itemNames = new ArrayList<>();
//        ArrayList<String> recordedTimes = new ArrayList<>();
//
//
//        for (int i = 0; i < itemCount; i++) {
//            recordedTimes.add(""+stopWatchListAdapter.getItem(i).getStopWatch().getEllapse());
//            baseTimes.add(((Long) stopWatchListAdapter.getItem(i).getStopWatch().getBaseTime()).toString());
//            itemNames.add(stopWatchListAdapter.getItem(i).getItemName());
//        }
//
//        outState.putInt(CURSOR, TimerListAdaptor.CURSOR);
//        outState.putInt(COUNT, itemCount);
//        outState.putStringArrayList(BASE_TIME, baseTimes);
//        outState.putStringArrayList(ITEM_NAME, itemNames);

        SINGLE_TON.saveTask(SingleTon.FILE_NAME);

        outState.putInt(CURSOR, TimerListAdaptor.CURSOR);

        super.onSaveInstanceState(outState);
    }


    //on touch event Handler Add Item on XML.
    public void onClickAddItem(View v) {

        stopWatchListAdapter.addItem();

    }


    //하루가 끝나고 그날 공부한 목록과 시간을 저장하는 기능.
    public void onClickSave(View v) {


        //파일 저장 형태 정하기.
        long now = System.currentTimeMillis();

        Date date = new Date(now);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");

        String getTime = "/log/" + sdf.format(date);

        //1. 저장.
        SINGLE_TON.saveTask(getTime);


        //2. 삭제 (초기화)
        stopWatchListAdapter.clearAdaptor();


    }

    public void showLog(View v) {
        Intent intent = new Intent(MainActivity.this, LogPastDay.class);
        startActivityForResult(intent, SingleTon.LOAD_PREVIOUS_STOP_WATCH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == SingleTon.LOAD_PREVIOUS_STOP_WATCH && requestCode == SingleTon.LOAD_PREVIOUS_STOP_WATCH) {

            Log.d("태그", "파일 경로 : " + data.getStringExtra(SingleTon.TARGET_FILE));

            //기존 항목들을 정리하고
            stopWatchListAdapter.clearAdaptor();

            //저장되어있던 내용들을 가져온다.
            SINGLE_TON.loadWatchFromFile(new File(data.getStringExtra(SingleTon.TARGET_FILE)),
                    stopWatchListAdapter);

        }
    }

}

