package com.shability.modbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    /* The important instances of the classes mentioned before */
    modBusUSB modbus;

    TextView status, device;

    Button connect, getVolt, getDeviceTemp;

    byte[] res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modbus = new modBusUSB(this);

        status = (TextView)findViewById(R.id.status);
        device = (TextView)findViewById(R.id.connect_device);

        connect = (Button) findViewById(R.id.btn_connect);
        getVolt = (Button)findViewById(R.id.btn_getVoltage);
        getDeviceTemp = (Button)findViewById(R.id.btn_getDeviceTemp);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setModbus();
            }
        });

        getVolt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadVoltage();
            }
        });

        getDeviceTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDeviceTemp();
            }
        });

    }



    public void setModbus(){

        if (modbus.Connected()){ //연결된 상태에서 연결 해제
            try {
                modbus.Disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if(modbus.Connect()){
                    status.setText(R.string.main_modbus_connect_ok);
                } else {
                    status.setText(R.string.main_modbus_connect_fail);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void LoadVoltage(){

        try {
            modbus.ReadInputRegisters(1,0x331A,0x0001);
            res = modbus.ReadRegPDU();

            String resualt = null;
            for (int i=0 ; i<res.length; i++){
                resualt = resualt+" "+Integer.toHexString(res[i]);
            }

            Toast.makeText(this, resualt, Toast.LENGTH_SHORT).show();

            device.setText(resualt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadDeviceTemp(){

        try {
            modbus.ReadInputRegisters(1,0x3111,0x0001);
            res = modbus.ReadRegPDU();

            String resualt = null;
            for (int i=0 ; i<res.length; i++){
                resualt = resualt+" "+Integer.toHexString(res[i]);
            }

            Toast.makeText(this, resualt, Toast.LENGTH_SHORT).show();

            device.setText(resualt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
