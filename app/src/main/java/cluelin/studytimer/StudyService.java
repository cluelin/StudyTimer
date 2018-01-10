package cluelin.studytimer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by cluel on 2017-12-24.
 */

public class StudyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.d("태그", "태스크 kill 감지 ");

        //종료되기전 가지고잇던 각 아이템의 이름과 스톱워치의 시간을 기록해서
        //파일에 저장해둔다.

        SingleTon SINGLE_TON = SingleTon.getInstance();
        SINGLE_TON.saveTask(SINGLE_TON.TEMP_FILE);
        super.onTaskRemoved(rootIntent);
    }
}
