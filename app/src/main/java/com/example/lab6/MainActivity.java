package com.example.lab6;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    int enteredHour;
    int enteredMinute;
    TextView minuciuSkirtumoText;
    TextView tekstoSimboliuText;
    TextView justText;
    TextView letterOutput;
    TextView viewClicked;
    boolean repeat=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        minuciuSkirtumoText = (TextView) findViewById(R.id.minuciuSkirtumoText);
        tekstoSimboliuText = (TextView) findViewById(R.id.tekstoSimboliuText);
        justText = (TextView) findViewById(R.id.justText);
        letterOutput = (TextView) findViewById(R.id.letterOutput);
        registerForContextMenu(this.minuciuSkirtumoText);
        registerForContextMenu(this.tekstoSimboliuText);
        registerForContextMenu(this.justText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.baigti) {
            finish();
            return true;
        }
        if (id == R.id.skirtumas) {
            showHourPicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_context, menu);
        viewClicked = (TextView) v;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getItemId()==R.id.skaiciuoti){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Simbolių skaičius")
                    .setMessage("Tekste simbolių yra: " + viewClicked.length())
                    .show();
            tekstoSimboliuText.setText("Tekste simbolių yra: " + viewClicked.length());
        }
        else if(item.getItemId()==R.id.vardinti){
            Thread t = new Thread(new Runnable(){
                @Override
                public void run(){
                    repeat = true;
                    while(repeat){
                        for (char ch: viewClicked.getText().toString().toCharArray()) {
                            String tekstas =  String.valueOf(ch);
                            letterOutput.setText(tekstas);
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
            t.start();
        }else{
            return false;
        }
        return true;
    }

    public void showHourPicker() {
        final Calendar myCalender = Calendar.getInstance();
        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
        int minute = myCalender.get(Calendar.MINUTE);

        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    myCalender.set(Calendar.MINUTE, minute);
                    enteredHour = hourOfDay;
                    enteredMinute = minute;
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Skirtumas minutėmis")
                            .setMessage("Skirtumas tarp dabar ir nurodyto laiko yra: " + skirtumasMinutemis())
                            .show();
                    minuciuSkirtumoText.setText("Skirtumas tarp esamo ir nurodyto laiko yra: " + skirtumasMinutemis());
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, myTimeListener, hour, minute, false);
        timePickerDialog.setTitle("Choose hour:");
        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();
    }

    public String skirtumasMinutemis() {
        Calendar calendar = Calendar.getInstance();
        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinutes = calendar.get(Calendar.MINUTE);
        int skirtumas = abs((currentHours*60+currentMinutes)-(enteredHour*60+enteredMinute));
        return String.valueOf(skirtumas);
    }
}