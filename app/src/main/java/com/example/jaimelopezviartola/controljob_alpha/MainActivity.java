package com.example.jaimelopezviartola.controljob_alpha;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    TextView texto;
    NetworkInfo.State internet_movil;
    NetworkInfo.State wifi;
    String red = "MOVISTAR_PLUS_1901";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto = (TextView)findViewById(R.id.conexion);
        Button boton = (Button)findViewById(R.id.boton);

        getConnectionService();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//Onclick
                getConnectionService();
            }
        });
    }

    private void getConnectionService(){
        //Recogemos el servicio ConnectivityManager
        //el cual se encarga de todas las conexiones del terminal
        ConnectivityManager conMan = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //Recogemos el estado del 3G
        //como vemos se recoge con el parámetro 0
        internet_movil = conMan.getNetworkInfo(0).getState();
        //Recogemos el estado del wifi
        //En este caso se recoge con el parámetro 1
        wifi = conMan.getNetworkInfo(1).getState();
        //Miramos si el internet 3G está conectado o conectandose...
        if (internet_movil == NetworkInfo.State.CONNECTED || internet_movil == NetworkInfo.State.CONNECTING) {
            //El movil está conectado por 4G
            //En este ejemplo mostraríamos mensaje por pantalla
            texto.setText("Conectado por 4G");
            //Si no esta por 3G comprovamos si está conectado o conectandose al wifi...
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            //El movil está conectado por WIFI
            //En este ejemplo mostraríamos mensaje por pantalla
            String wifiSSID = getWifiName(MainActivity.this);
            if(wifiSSID.equals(red)) {
                texto.setText("Conectado por red wifi: " + wifiSSID);
            }else {
                texto.setText("Conectado por WIFI");
            }
        }
    }

    public String getWifiName(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String SSID = "";
        if (wifiInfo != null) {
            NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
            if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                SSID = wifiInfo.getSSID().trim().replaceAll("\"","");
            }
        }
        return SSID;
    }
}
