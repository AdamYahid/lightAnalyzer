package com.yahid.lightanalyzer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yahid.lightanalyzer.model.RoadDataVO;

public class Activity_SetRoadData extends Activity {

    TextWatcher validator;
    EditText streetName;
    EditText laneCount;
    EditText laneLength;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_road_data);

        streetName = (EditText)findViewById(R.id.streetNameET);
        laneCount = (EditText)findViewById(R.id.laneCountET);
        laneLength = (EditText)findViewById(R.id.laneLengthET);

        nextBtn = (Button)findViewById(R.id.setDataNextBTN);

        validator = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (streetName.getText().length() > 0 && laneCount.getText().length() > 0 && laneLength.getText().length() > 0) {
                    nextBtn.setEnabled(true);
                }
                else
                {
                    nextBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        streetName.addTextChangedListener(validator);
        laneCount.addTextChangedListener(validator);
        laneLength.addTextChangedListener(validator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity__set_road_data, menu);
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
    protected void onDestroy() {
        super.onDestroy();
        streetName.removeTextChangedListener(validator);
        laneCount.removeTextChangedListener(validator);
        laneLength.removeTextChangedListener(validator);
    }

    private Intent getFormIntent() {
        String streetNameValue = streetName.getText().toString();
        int laneCountValue = Integer.parseInt(laneCount.getText().toString());
        int laneLengthValue = Integer.parseInt(laneLength.getText().toString());
        if (streetNameValue.length() > 0 && laneCountValue > 0 && laneLengthValue > 0) {

            SharedData.activeRoad = new RoadDataVO(streetNameValue,laneCountValue,laneLengthValue,10);

            Intent res = new Intent(this,Activity_MeasureRoad.class);
            res.putExtra(LightAnalyzerExtras.EXTRA_STREET_NAME,streetNameValue);
            res.putExtra(LightAnalyzerExtras.EXTRA_POLE_HEIGHT, 10);
            res.putExtra(LightAnalyzerExtras.EXTRA_LANE_COUNT,laneCountValue);
            res.putExtra(LightAnalyzerExtras.EXTRA_POLE_COUNT,laneLengthValue);
            return res;
        }
        return null;
    }

    public void nextBTN_clickHandler(View view) {
        Intent intent = getFormIntent();
        if (intent != null){
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Missing form data",Toast.LENGTH_SHORT).show();
        }
    }
}
