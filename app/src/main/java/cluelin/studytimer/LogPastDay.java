package cluelin.studytimer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LogPastDay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);


        final File fileList[] = new File(this.getFilesDir(), "/log").listFiles();

        final ListView logListView = (ListView) findViewById(R.id.logList);

        final ArrayList<String> listItems = new ArrayList<String>();

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                listItems);


        logListView.setAdapter(adapter);




        for (int i = 0; i < fileList.length; i++) {

            listItems.add(fileList[i].getName());

            logListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.d("태그", "Log onClick");

                    File targetFile = fileList[position];

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(SingleTon.TARGET_FILE, targetFile.getAbsolutePath());
                    setResult(SingleTon.LOAD_PREVIOUS_STOP_WATCH, returnIntent);
                    finish();
                }
            });

            logListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    Log.d("태그", "Log onLongCLick");

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(LogPastDay.this);
                    alert_confirm.setMessage("삭제?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    File targetFile = fileList[position];

                                    targetFile.delete();
                                    listItems.remove(position);
                                    adapter.notifyDataSetChanged();
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

                    return true;
                }
            });




        }

        adapter.notifyDataSetChanged();

    }
}
