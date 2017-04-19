package com.example.josephwidjaja.nfcreader;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;
    private LineChart mChart;
    private int count = 0;
    private Button histbtn;
    private Button savebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        mChart = (LineChart) findViewById(R.id.lineChart);

//        CHECKING IF CHART SHOWS
//        ArrayList<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(4f, 0));
//        entries.add(new Entry(8f, 1));
//        entries.add(new Entry(6f, 2));
//        entries.add(new Entry(2f, 3));
//        entries.add(new Entry(18f, 4));
//        entries.add(new Entry(9f, 5));
//
//        Collections.sort(entries, new EntryXComparator());
//
//        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(dataset);


//        LineData data = new LineData();

//        mChart.setData(data);

        Description description = new Description();
        description.setTextColor(ColorTemplate.VORDIPLOM_COLORS[2]);
        description.setText("Chart Data");
        mChart.setDescription(description);

        mChart.setNoDataText("No data at the moment");

        // value highlighting
        mChart.setHighlightPerDragEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // touch gestures
        mChart.setTouchEnabled(true);

        // scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // avoid scaling x and y separately
        mChart.setPinchZoom(true);

        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        Legend l = mChart.getLegend();

        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

        histbtn = (Button)findViewById(R.id.histbtn);

        histbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        savebtn = (Button)findViewById(R.id.savebtn);

        savebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String data=getEntries();
                try {
                    FileOutputStream fOut = openFileOutput("mydata", MODE_PRIVATE);
                    fOut.write(data.getBytes());
                    fOut.close();
                    Toast.makeText(getBaseContext(),"file saved",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });


    }

    private String getEntries() {
        // TODO parse LineData entries to format in file
        String result = "";

        LineData data = mChart.getData();

        if(data != null){
            ILineDataSet dataset = data.getDataSetByIndex(0);
            int entryCount = dataset.getEntryCount();

            if(dataset != null){
                for(int i = 0; i < entryCount; i++){
                    Entry e = dataset.getEntryForIndex(i);
                    result += String.valueOf(e.getX()) + ", " + String.valueOf(e.getY()) + "\n";
                }
            }
        }


        return result;
    }

    private void addEntry() {
        LineData data = mChart.getData();

        if (data != null) {
            ILineDataSet dataset = data.getDataSetByIndex(0);

            if(dataset == null){
                dataset = createSet();
                data.addDataSet(dataset);
            }

            data.addEntry(new Entry(dataset.getEntryCount(), (float)(Math.random() * 75) + 30f), 0);
            data.notifyDataChanged();

            mChart.notifyDataSetChanged();

            mChart.setVisibleXRangeMaximum(6);

            mChart.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                            Toast.makeText(getApplicationContext(), "HELLO " + count, Toast.LENGTH_SHORT).show();
                            count++;
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {

                    }

                }


            }
        }).start();
    }
}
