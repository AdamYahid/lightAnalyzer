package com.yahid.lightanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yahid.lightanalyzer.model.RoadDataVO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Activity_ReadFiles extends Activity {

    private ListView lv;
    private Activity_ReadFiles thisRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_files);

        lv = (ListView) findViewById(R.id.fileListView);

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        final ArrayList<File> fileList = LocalProxy.getFileList(this);
        List<String> fileNames = new ArrayList<String>();
        for (File file : fileList) {
            fileNames.add(file.getName());
        }


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                fileNames );

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                File file = fileList.get(position);

                StringBuilder text = new StringBuilder();
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                        br.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                RoadDataVO rdv = new RoadDataVO();
                JSONObject obj = null;
                try {
                    obj = new JSONObject(text.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rdv.readJSON(obj);
                SharedData.activeRoad = rdv;
                intent.setClass(thisRef, Activity_MeasureRoad.class);
                startActivity(intent);
            }
        });
        thisRef = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__read_files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
