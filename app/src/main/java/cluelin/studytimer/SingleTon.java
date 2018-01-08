package cluelin.studytimer;

import android.content.Context;
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
    public static final String FILE_NAME = "savefile.txt";


    //timer 객체 리스트.
    TimerListAdaptor stopWatchListAdapter;



    final String LOG_DIR = "log";


    Context context;

    static SingleTon instance = null;




    private SingleTon(){

    }

    public static SingleTon getInstance(){
        if(instance == null)
            instance = new SingleTon();

        return instance;
    }

    void setContext(Context context){
        this.context = context;

    }


    public void setStopWatchListAdapter(TimerListAdaptor stopWatchListAdapter) {
        this.stopWatchListAdapter = stopWatchListAdapter;
    }

    void saveTask(String fileName) {

        Log.d("태그", "saveTask");


        int itemCount = stopWatchListAdapter.getCount();
        ArrayList<String> RecordingTimes = new ArrayList<>();
        ArrayList<String> itemNames = new ArrayList<>();

        for (int i = 0; i < itemCount; i++) {
            itemNames.add("" + stopWatchListAdapter.getItem(i).getItemName());
            RecordingTimes.add(((Long) stopWatchListAdapter.getItem(i).getStopWatch().getEllapse()).toString());
        }

        try {
            Log.d("태그", "경로 : " + context.getFilesDir() + fileName);
            PrintWriter out = new PrintWriter(new File(context.getFilesDir(), fileName));

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

    void loadWatchFromFile(File file, TimerListAdaptor stopWatchListAdapter) {

        Log.d("태그", "loadWatchFromFile");

        try {

            if(file.exists()){


                BufferedReader in = new BufferedReader(new FileReader(file));
                int count = in.read();


                for (int i = 0; i < count; i++) {
                    StudyItem studyItem = new StudyItem();

                    studyItem.setItemName(in.readLine());

                    Log.d("태그", "getItemName : " + studyItem.getItemName());

                    long initialTime = Long.parseLong(in.readLine());

                    Log.d("태그", "getItemTime : " + studyItem.getStopWatch().getStringTime(initialTime));


                    studyItem.getStopWatch().setInitialTime(initialTime);

                    stopWatchListAdapter.addItem(studyItem);
                }

                TimerListAdaptor.CURSOR = -1;
                stopWatchListAdapter.notifyDataSetChanged();


            }else
                Log.d("태그", "파일 존재 안함");



        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();

        } catch (IOException ioexception) {
            ioexception.printStackTrace();

        }
    }

}
