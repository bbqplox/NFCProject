package com.example.josephwidjaja.nfcreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;


/**
 * Created by josephwidjaja on 4/19/17.
 */

public class HistoryActivity extends AppCompatActivity {

    private Button readbtn;
    private TextView message;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        readbtn = (Button) findViewById(R.id.readbtn);
        message = (TextView) findViewById(R.id.message);

        readbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fin = openFileInput("mydata");
                    int c;
                    String temp="";
                    while( (c = fin.read()) != -1){
                        temp = temp + Character.toString((char)c);
                    }
                    message.setText(temp);
                    Toast.makeText(getBaseContext(),"file read",Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                }
            }
        });





    }
}
