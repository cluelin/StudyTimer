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



    void loadWatchFromFile(File file, ArrayList<StudyItem> stopWatchItems, TimerListAdaptor stopWatchListAdapter) {

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
                    studyItem.getStopWatch().setRecordingTime(initialTime);
                    studyItem.getStopWatch().setInitialTime(initialTime);

                    stopWatchItems.add(studyItem);
                }

                TimerListAdaptor.setCursor(-1);
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
