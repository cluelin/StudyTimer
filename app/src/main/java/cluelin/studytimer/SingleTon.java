package cluelin.studytimer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.renderscript.RenderScript;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by cluel on 2017-12-24.
 */

public class SingleTon {

    public static final int LOAD_PREVIOUS_STOP_WATCH = 991;
    public static final String TARGET_FILE = "targetFile";
    public static final String TEMP_FILE = "temp.txt";


    //timer 객체 리스트.
    TimerListAdaptor stopWatchListAdapter;

    static final int NOTIFICATION_ID = 1234;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;


    final String LOG_DIR = "log";


    Context context;

    static SingleTon instance = null;


    private SingleTon() {

    }

    public static SingleTon getInstance() {
        if (instance == null)
            instance = new SingleTon();

        return instance;
    }

    void setContext(Context context) {
        this.context = context;

    }


    public void setStopWatchListAdapter(TimerListAdaptor stopWatchListAdapter) {
        this.stopWatchListAdapter = stopWatchListAdapter;
    }

    void saveTask(String fileName) {

        Log.d("태그", "saveTask");

        //저장하기전에는 항상 현재 동작중인 커서를 멈추고 작업을 진행한다.
        stopWatchListAdapter.stopCursor();

        int itemCount = stopWatchListAdapter.getCount();

        //저장할게없으면 종료
        if (itemCount == 0 )
            return;

        ArrayList<String> RecordingTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            itemNames.add("" + stopWatchListAdapter.getItem(i).getItemName());
            Log.d("태그", i + "번째 저장되는 타임 : " + (Long) stopWatchListAdapter.getItem(i).getStopWatch().getRecordedTime());
            RecordingTimes.add(((Long) stopWatchListAdapter.getItem(i).getStopWatch().getRecordedTime()).toString());
        }

        try {

            //항목 카운트
            //이름
            //기록된 시간.

            Log.d("태그", "경로 : " + context.getFilesDir() + fileName);
            PrintWriter out = new PrintWriter(new File(context.getFilesDir(), fileName));

            out.write(itemCount);

            for (int i = 0; i < itemCount; i++) {
                out.println(itemNames.get(i));
                out.println(RecordingTimes.get(i));
            }

            //저장완료후 기존리스트 지우기
            stopWatchListAdapter.clearAdaptor();

            out.close();

        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }


    void loadWatchFromFile(File file, TimerListAdaptor stopWatchListAdapter) {

        Log.d("태그", "loadWatchFromFile");

        try {

            if (file.exists()) {

                stopWatchListAdapter.clearAdaptor();

                //항목 카운트
                //이름
                //기록된 시간.

                BufferedReader in = new BufferedReader(new FileReader(file));
                int count = in.read();

                for (int i = 0; i < count; i++) {
                    StudyItem studyItem = new StudyItem();

                    studyItem.setItemName(in.readLine());

                    Log.d("태그", "getItemName : " + studyItem.getItemName());

                    long initialTime = Long.parseLong(in.readLine());

                    Log.d("태그", "getItemTime : " + stopWatchListAdapter.getStringTime(initialTime));


                    studyItem.getStopWatch().setInitialTime(initialTime);

                    stopWatchListAdapter.addItem(studyItem);
                }

                TimerListAdaptor.setCURSOR(-1);
                stopWatchListAdapter.notifyDataSetChanged();

            } else
                Log.d("태그", "파일 존재 안함");


        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();

        } catch (IOException ioexception) {
            ioexception.printStackTrace();

        }

    }


    void notificationStopWatch() {
        Resources res = context.getResources();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("notificationId", 9999); //전달할 값
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_stat_stopwatch)
                .setContentIntent(contentIntent)
                .setOngoing(true);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    void updateNotification(String name, String time) {

        builder.setContentTitle(name)
                .setContentText(time);
        notificationManager.notify(NOTIFICATION_ID, builder.build());


    }

    void removeNotification(){
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
