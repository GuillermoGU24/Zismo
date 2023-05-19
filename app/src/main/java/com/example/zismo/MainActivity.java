package com.example.zismo;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Switch;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.UUID;

import java.net.URI;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    HomeFragment home = new HomeFragment();
    FirstFragment primero = new FirstFragment();
    SecondFragment segundo = new SecondFragment();
    ThirdFragment tercero = new ThirdFragment();
    Switch aSwitch;

    //-------------------------------------------
    public static String vINFO = "device_info";
    Handler bluetoothIn;
    final int handlerState = 0;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder DataStringIN = new StringBuilder();
    private ConnectedThread MyConexionBT;
    // Identificador unico de servicio - SPP UUID
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String para la direccion MAC
    private static String address;
    String info;
    String direc;
    //--------------------------------------------
    public static final String ACCOUNT_SID = "ACb4d2803c7d3aa407f7421edf70dca480";
    public static final String AUTH_TOKEN = "fd341c307fc9e5924e99bc4d7979e5dd";
    //--------------------------------------------
    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Zimo");
        setContentView(R.layout.activity_main);
        aSwitch = findViewById(R.id.switch2);

        Bundle bundle = getIntent().getExtras();
        info = bundle.getString(DispositivosVinculados.EXTRA_DEVICE_INFO);
        direc = bundle.getString(DispositivosVinculados.EXTRA_DEVICE_ADDRESS);

        Bundle args = new Bundle();
        args.putString("Info", info);
        args.putString("Direc", direc);
        home.setArguments(args);
        BottomNavigationView navigation = findViewById(R.id.bottom_navi);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(home);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {
                    char MyCaracter = (char) msg.obj;

                    if (MyCaracter == 'C') {
                          SmsManager smsManager = SmsManager.getDefault();

                          smsManager.sendTextMessage(CargarPreferenciasNum("Credenciales0"), null, "Alerta de Posible choque, Comunicate con Guillermo", null, null);
                          smsManager.sendTextMessage(CargarPreferenciasNum("Credenciales1"), null, "Alerta de Posible choque, Comunicate con Guillermo", null, null);


                        }

                }
            }
        };
        aSwitch.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           if (aSwitch.isChecked()) {
                                               Cerrar();
                                               Intent intent = new Intent (view.getContext(), DispositivosVinculados.class);
                                               startActivityForResult(intent, 0);

                                           }
                                       }
                                   }
        );
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        VerificarEstadoBT();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        checkSMSStatePermission();
        return super.onCreateOptionsMenu(menu);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.primero:
                    loadFragment(primero);
                    return true;
                case R.id.segundo:

                    loadFragment(segundo);
                    return true;
                case R.id.tercero:

                    loadFragment(tercero);
                    return true;
            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.homes) {
            loadFragment(home);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        address = intent.getStringExtra(DispositivosVinculados.EXTRA_DEVICE_ADDRESS);

        //Setea la direccion MAC
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "La creacción del Socket fallo", Toast.LENGTH_LONG).show();
        }
        // Establece la conexión con el socket Bluetooth.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
            }
        }
        MyConexionBT = new ConnectedThread(btSocket);
        MyConexionBT.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            btSocket.close();
        } catch (IOException e2) {
        }
    }

    //Comprueba que el dispositivo Bluetooth
    //está disponible y solicita que se active si está desactivado
    private void VerificarEstadoBT() {

        if (btAdapter == null) {
            Toast.makeText(getBaseContext(), "El dispositivo no soporta bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //Crea la clase que permite crear el evento de conexión
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] byte_in = new byte[1];
            // Se mantiene en modo escucha para determinar el ingreso de datos
            while (true) {
                try {
                    mmInStream.read(byte_in);
                    char ch = (char) byte_in[0];
                    bluetoothIn.obtainMessage(handlerState, ch).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String input) {
            try {
                mmOutStream.write(input.getBytes());
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public void Cerrar() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
                ;
            }
        }
        Toast.makeText(getBaseContext(), "Dispositivo Desvinculado", Toast.LENGTH_LONG).show();
    }


    public String CargarPreferenciasNum(String crendenciale) {
        SharedPreferences preferencias2 = getSharedPreferences(crendenciale, Context.MODE_PRIVATE);
        String num = preferencias2.getString("Num", "");
        return num;
    }

    private void checkSMSStatePermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Mensaje", "No se tiene permiso para enviar SMS.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 225);
        } else {
            Log.i("Mensaje", "Se tiene permiso para enviar SMS!");
        }
    }


}