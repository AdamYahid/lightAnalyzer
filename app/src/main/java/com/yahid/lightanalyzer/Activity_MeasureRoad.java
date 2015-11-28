package com.yahid.lightanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yahid.lightanalyzer.model.RoadDataVO;

import java.io.File;

public class Activity_MeasureRoad extends Activity implements View.OnClickListener {

    String streetName;
    int poleHeight;
    int rowCount;
    int colCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.measure_road);



        // Get the message from the intent
        Intent intent = getIntent();

        RoadDataVO activeRoad = SharedData.activeRoad;

        // Create the text view
        TextView streetTV = (TextView) findViewById(R.id.streetNameTV);
        streetTV.setText(activeRoad.getStreetName());

        TextView poleHeightTV = (TextView) findViewById(R.id.poleHeightTV);
        poleHeightTV.setText(poleHeightTV.getText() + ":" + Integer.toString(activeRoad.getLaneCount()));

        rowCount = activeRoad.getLaneCount();
        colCount = activeRoad.getLaneLength();

        LinearLayout body = (LinearLayout) findViewById(R.id.bodyLayout);
        for (int i = 0; i < rowCount; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < colCount; j++) {
                Button btn = new Button(this);
                btn.setOnClickListener(this);
                btn.setSelected(false);
                btn.setText(String.valueOf(activeRoad.getDataPoint(i,j)));
                btn.setId(i*10 + j);
                row.addView(btn);

            }
            body.addView(row);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__measure_road, menu);
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

    @Override
    public void onClick(View v) {
        Button b = (Button) v;
        double currentLightValue = YoctoProxy.getInstance(null).readCurrentValue();
        int laneId = b.getId()%10;
        int indexInLane = b.getId()/10;
        SharedData.activeRoad.setDataPoint(laneId, indexInLane, currentLightValue);
        b.setText(String.valueOf(SharedData.activeRoad.getDataPoint(laneId, indexInLane)));
    }

    public void exportHandler(View view) {
        String fileAsString = SharedData.activeRoad.writeJSON();
        String fileName = SharedData.activeRoad.getStreetName();

        LocalProxy.saveProject(this,fileName,fileAsString);
    }

    /*
    public void exportHandler(View view) {
        ProjectVO proj = new ProjectVO(streetName,rowCount,colCount,poleHeight);
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Button btn = (Button) findViewById(i*10+j);
                double val = Double.valueOf((String)btn.getText());
                proj.addValue(i, j, val);
            }
        }

        LocalProxy.createExcelSheet(this,proj);
        File f = new File (this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+proj.getStreetName()+".xls");
        String pathToFile = f.getAbsolutePath();
        String to[] = {"adam.yahid@gmail.com"};

        Intent i = new Intent(Intent.ACTION_SEND);

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String mimeTypeForXLSFile = mime.getMimeTypeFromExtension(".xls");

        i.putExtra(Intent.EXTRA_EMAIL,to);
        i.putExtra(Intent.EXTRA_SUBJECT, proj.getStreetName());
        i.putExtra(Intent.EXTRA_TEXT, "message");
        //i.setType("message/rfc822");
        i.setType(mimeTypeForXLSFile);
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + f.getAbsolutePath()));
        startActivity(Intent.createChooser(i,"send email:"));

    }
    */
}
