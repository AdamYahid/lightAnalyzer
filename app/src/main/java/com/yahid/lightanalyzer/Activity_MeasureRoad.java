package com.yahid.lightanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yahid.lightanalyzer.model.RoadDataVO;

import java.io.File;
import java.net.URI;

import jxl.write.WritableWorkbook;

public class Activity_MeasureRoad extends Activity implements View.OnClickListener {

    String streetName;
    int poleHeight;
    int rowCount;
    int colCount;
    RoadDataVO activeRoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.measure_road);

        activeRoad = SharedData.activeRoad;

        // Create the text view
        TextView streetTV = (TextView) findViewById(R.id.streetNameTV);
        streetTV.setText(activeRoad.getStreetName());

        TextView poleHeightTV = (TextView) findViewById(R.id.poleHeightTV);
        poleHeightTV.setText(poleHeightTV.getText() + ":" + Integer.toString(activeRoad.getLaneCount()));

        rowCount = activeRoad.getLaneCount();
        colCount = activeRoad.getLaneLength();

        LinearLayout body = (LinearLayout) findViewById(R.id.bodyLayout);
        for (int i = 0; i < activeRoad.getLaneCount(); i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setId(i);
            for (int j = 0; j < activeRoad.getLaneLength(); j++) {
                Button btn = new Button(this);
                btn.setOnClickListener(this);
                btn.setSelected(false);
                btn.setText(String.valueOf(activeRoad.getDataPoint(i, j)));
                btn.setId(j);
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
        int laneId = ((View)b.getParent()).getId();
        int indexInLane = b.getId();
        SharedData.activeRoad.setDataPoint(laneId, indexInLane, currentLightValue);
        b.setText(String.valueOf(SharedData.activeRoad.getDataPoint(laneId, indexInLane)));
    }

    public void exportHandler(View view) {
        String fileAsString = SharedData.activeRoad.writeJSON();
        String fileName = SharedData.activeRoad.getStreetName();

        LocalProxy.saveProject(this, fileName, fileAsString);
    }

    public void emailHandler(View view) {

        //File sdcard = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        //Get the text file
        //File file = new File(sdcard,activeRoad.getStreetName() + ".lap");

        File file = LocalProxy.createExcelSheet(this,activeRoad);
        if (file == null) {
            return;
        }

        //String filelocation = file.getAbsolutePath();
        Uri uri = Uri.fromFile(file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent.setType("*/*");
        String to[] = {"adam.yahid@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, uri);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "I just shared " + activeRoad.getStreetName() + " measurement with you");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void newProjectHandler(View v) {
        Intent intent = new Intent();
        intent.setClass(this,Activity_SelectMeasureType.class);
        startActivity(intent);
    }

    /*
    public void exportHandler(View view) {
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
