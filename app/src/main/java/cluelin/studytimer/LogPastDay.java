package cluelin.studytimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class LogPastDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.logLayout);

        //파일이 저장되는지 알아보기 위한 코드

        File fileList[] = new File(this.getFilesDir(), "/log").listFiles();


        for (int i = 0; i < fileList.length; i++) {

            Log.d("태그", "파일 명 : " + fileList[i].getName());

            final File targetFile = fileList[i];
            final TextView textView = new TextView(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.height = 100;

            textView.setLayoutParams(params);

            Log.d("태그", "param 내용 : " +  params);



            textView.setText("" + fileList[i].getName());


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(SingleTon.TARGET_FILE, targetFile.getAbsolutePath());
                    setResult(SingleTon.LOAD_PREVIOUS_STOP_WATCH, returnIntent);
                    finish();

                }
            });

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {


                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(LogPastDay.this);
                    alert_confirm.setMessage("삭제?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    targetFile.delete();
                                    linearLayout.removeView(textView);
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

            linearLayout.addView(textView);

        }


    }
}
