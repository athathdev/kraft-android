package com.spaceuptech.kraft;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.spaceuptech.clientapi.ClientApi;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

public class DataService extends Service {
    public static String SESSION_ID = "";
    private ClientApi api;
    private DatabaseHelper dbHelper;

    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!api.isConnected()) api.connect();
        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Connected", "Service started");

        dbHelper = new DatabaseHelper(getApplicationContext());

        api = new ClientApi("192.168.3.44", 11100, true, new ClientApi.OnMessageReceived() {
            @Override
            public void messageReceived(ClientApi.Request message) {
                switch (message.func) {
                    case "login":
                        System.out.println("Recieved: "+new String(message.args));
                        break;
                }
            }
        }, new ClientApi.OnConnected() {
            @Override
            public void connected() {
                Log.d("Connected", "Connected to server");
                ClientApi.call(getApplicationContext(), "chat-engine", "login", "{\"Chatroom\":\"123\"}".getBytes());
            }
        }, new ClientApi.OnDisconnected() {
            @Override
            public void disconnected() {
                if (api.isNetworkConnected(getApplicationContext())) api.connect();
            }
        });
        api.setSslAuthenticateHost(false);


        LocalBroadcastManager.getInstance(this).registerReceiver(api.requestReceiver, new IntentFilter(ClientApi.CALL));
        registerReceiver(api.networkChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(api.requestReceiver);
        unregisterReceiver(api.networkChangeReceiver);
        dbHelper.close();
        super.onDestroy();
    }
}
