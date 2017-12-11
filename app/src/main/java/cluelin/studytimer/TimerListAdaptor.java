package cluelin.studytimer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by cluel on 2017-12-10.
 */

public class TimerListAdaptor extends BaseAdapter {


    final static int IDLE = 0;

    final static int RUNNING = 1;

    final static int PAUSE = 2;

    int mStatus = IDLE;//처음 상태는 IDLE

    long mBaseTime;
    long mPauseTime;

    Context context;
    int layout;
    LayoutInflater inf;
    ArrayList<StudyItem> studyItems;
    TextView stopWatch;





    public TimerListAdaptor(Context context, int layout, ArrayList<StudyItem> studyItems) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        EditText itemName = (EditText) convertView.findViewById(R.id.itemName);
        stopWatch = (TextView) convertView.findViewById(R.id.recordingTime);

        StudyItem studyItem = (StudyItem)getItem(position);

        itemName.setHint(studyItem.getItemName());
        stopWatch.setText(studyItem.getRecordingTime());




        // 스톱워치 부분을 터치 했을 때 이벤트 발생
        stopWatch.setOnClickListener(new View.OnClickListener() {

            StudyItem studyItem = (StudyItem)getItem(position);
            TextView stopText;
            TextView cursor = null;

            Handler mTimer = new Handler(){



                //핸들러는 기본적으로 handleMessage에서 처리한다.

                public void handleMessage(android.os.Message msg) {

                    //텍스트뷰를 수정해준다.

                    stopText.setText(getEllapse());

                    //메시지를 다시 보낸다.

                    mTimer.sendEmptyMessage(0);//0은 메시지를 구분하기 위한 것

                };

            };


            String getEllapse(){

                long now = SystemClock.elapsedRealtime();

                long ell = now - mBaseTime;//현재 시간과 지난 시간을 빼서 ell값을 구하고

                //아래에서 포맷을 예쁘게 바꾼다음 리턴해준다.

                String sEll = String.format("%02d:%02d:%02d", ell / 1000 / 60, (ell/1000)%60, (ell %1000)/10);

                return sEll;

            }

            @Override
            public void onClick(View v) {

                stopText = (TextView)v;

                //cursor 가 현재 null 처음시작한상태. 이거나
                //커서와 현재 클릭한 뷰가 같으면 한개짜리 타이머로 보면되고..
                if(cursor == stopText || cursor == null){
                    cursor = stopText;
                    switch (mStatus){
                        case IDLE:

                            mStatus = RUNNING;
                            mBaseTime = SystemClock.elapsedRealtime();
                            mTimer.sendEmptyMessage(0);


                            break;
                        case RUNNING:
                            mTimer.removeMessages(0);
                            mPauseTime = SystemClock.elapsedRealtime();
                            mStatus = PAUSE;
                            break;
                        case PAUSE:
                            mBaseTime += (SystemClock.elapsedRealtime() - mPauseTime);
                            mTimer.sendEmptyMessage(0);
                            mStatus = RUNNING;
                            break;
                    }

                }else{
                    //그게 아니라 이전에 작동하고 잇던 타이머와는 다른 타이머를 선택햇다면.
                    //현재 작동하는 타이머는 일시 정지해두고.
                    //클릭된 타이머는 현재 시간 + 이제 앞으로 작동해가면된다.
                    mTimer.removeMessages(0);

                    cursor = stopText;
                    mStatus = RUNNING;

                    mBaseTime = SystemClock.elapsedRealtime();
                    mTimer.sendEmptyMessage(0);


                }






            }
        });




        return convertView;
    }
}
