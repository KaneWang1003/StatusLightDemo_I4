package com.elotouch.statuslightdemo_E466847;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.elotouch.library.EloPeripheralManager;
import com.elotouch.library.EloPeripheralManager.ExtLed;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CheckBox checkBox_Exled_left ;
    private CheckBox checkBox_Exled_right ;
    private CheckBox checkBox_Exled_buttom ;
    private CheckBox checkBox_Exled_top ;

    private   int portsMask_left ;
    private   int portsMask_right ;
    private   int portsMask_top ;
    private   int portsMask_buttom ;

    private ExtLed extLed;

    //set Default
    private int power = ExtLed.PORT_STATUS_ON;
    private int color = ExtLed.EXT_LED_COLOR_RED;
    private int port = portsMask_right| portsMask_left;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.MODEL.equals("Elo-i2-10Std")
                || Build.MODEL.equals("Elo-i2-15Std")
                || Build.MODEL.equals("Elo-i2-22Std")) {
             portsMask_left = ExtLed.MASK_USB_PORT2;
             portsMask_right = ExtLed.MASK_USB_PORT4;
             portsMask_top = ExtLed.MASK_USB_PORT3;
             portsMask_buttom = ExtLed.MASK_USB_PORT5;
        } else {

             portsMask_left = ExtLed.MASK_USB_PORT3;
             portsMask_right = ExtLed.MASK_USB_PORT5;
             portsMask_top = ExtLed.MASK_USB_PORT4;
             portsMask_buttom = ExtLed.MASK_USB_PORT6;
        }

        checkBox_Exled_left = findViewById(R.id.checkbox_Exled_left);
        checkBox_Exled_right = findViewById(R.id.checkBox_Exled_right);
        checkBox_Exled_buttom = findViewById(R.id.checkBox_Exled_buttom);
        checkBox_Exled_top = findViewById(R.id.checkBox_Exled_top);

        Log.d(TAG , "Port: " + port  + " power: " + power + " color: " + color);

        getSelectedExledPort();

        init_Exled();

        findViewById(R.id.button_on).setOnClickListener(v -> {getSelectedExledPort();power =ExtLed.PORT_STATUS_ON;setPower(port, power);});

        findViewById(R.id.button_off).setOnClickListener(v -> {getSelectedExledPort();power = ExtLed.PORT_STATUS_OFF; setPower(port, power);});

        findViewById(R.id.button_red).setOnClickListener(v -> { color = ExtLed.EXT_LED_COLOR_RED; getSelectedExledPort(); setColor(port, color);});

        findViewById(R.id.button_green).setOnClickListener(v -> {color = ExtLed.EXT_LED_COLOR_GREEN; getSelectedExledPort();setColor(port, color);});

        checkBox_Exled_buttom.setOnClickListener(v -> getSelectedExledPort());
        checkBox_Exled_left.setOnClickListener(v -> getSelectedExledPort());
        checkBox_Exled_right.setOnClickListener(v -> getSelectedExledPort());
        checkBox_Exled_top.setOnClickListener(v -> getSelectedExledPort());



    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private void setPower(int port, int power_status) {
        Log.d(TAG , "update power--------- port: " + port  + " power: " + power );
        extLed.setPower(port, power_status == 0 ? power_status: port , null);

    }

    private void setColor(int port, int color) {
        Log.d(TAG , "update Color--------port: " + port  + "  color: " + color);
        extLed.setColor(port, color);
    }


    private void  init_Exled(){


        extLed = new EloPeripheralManager(this, null).getExtLedCtl();

        if (extLed == null) {
            Log.e(TAG,"not find exled service , please check the OS version, support from MR6 or MR7....");
            return;
        }

        if (!extLed.isEnabled()) {
            Log.d(TAG,"Enable SLK");
            extLed.enableSlk(true);
        }


        setPower(port, power);
        setColor(port, color);

    }

    private int getSelectedExledPort() {
        port = 0;

        if (checkBox_Exled_buttom.isChecked()) {
            port |= portsMask_buttom;
        }
        if (checkBox_Exled_top.isChecked()) {
            port |= portsMask_top ;
        }

        if (checkBox_Exled_left.isChecked()) {
            port |= portsMask_left;
        }

        if (checkBox_Exled_right.isChecked()) {
            port |= portsMask_right;
        }
        return port;
    }
}

