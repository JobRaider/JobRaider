package com.example.jaimelopezviartola.controljob_alpha;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    TextView texto;
    NetworkInfo.State internet_movil;
    NetworkInfo.State wifi;
    String red = "JobRaider";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        texto = (TextView)findViewById(R.id.conexion);

        getConnectionService();
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
        Timer();
        //Miramos si el internet 3G está conectado o conectandose...
        if (internet_movil == NetworkInfo.State.CONNECTED || internet_movil == NetworkInfo.State.CONNECTING) {
            //El movil está conectado por 4G
            //En este ejemplo mostraríamos mensaje por pantalla
            texto.setText("Ausente / No conectado");
            Toast.makeText(getApplicationContext(),"No estás conectado al WiFi de la empresa", Toast.LENGTH_LONG).show();
            //Si no esta por 3G comprovamos si está conectado o conectandose al wifi...
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            //El movil está conectado por WIFI
            //En este ejemplo mostraríamos mensaje por pantalla
            String wifiSSID = getWifiName(MainActivity.this);
            if(wifiSSID.equals(red)) {
                texto.setText("Trabajando");
                Toast.makeText(getApplicationContext(),"Conectado a la red WiFi de la Empresa: " + wifiSSID, Toast.LENGTH_SHORT).show();
            }else {
                texto.setText("Ausente / WiFi ERROR");
                Toast.makeText(getApplicationContext(),"Conectado a otra red WiFi, comprueba tu conexión", Toast.LENGTH_LONG).show();
            }
        }else{
            texto.setText("Red desconectada");
            Toast.makeText(getApplicationContext(),"Activa el WiFi de tu dispositivo", Toast.LENGTH_LONG).show();
        }
    }

    private void Timer(){
        //Thread este es el timer que ejecutará periódicamente la prueba de conexión
        //Cada 5 segundos hay un tick y el timer dura 10 segundos
        CountDownTimer contador = new CountDownTimer(10000, 5000) {
            //Esta función ejecuta un comando cada tick del timer
            public void onTick(long millisUntilFinished) {
                //Toast.makeText(getApplicationContext(),"Thread Ejecutado", Toast.LENGTH_LONG).show();
            }
            //Esta función ejecuta un comando al terminar el timer, en nuestro caso vuelve a lanzar la prueba de conexión
            public void onFinish() {
                getConnectionService();
            }
        }.start();
    }

    private String getWifiName(Context context) {
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
