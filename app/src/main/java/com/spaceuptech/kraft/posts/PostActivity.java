package com.spaceuptech.kraft.posts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.MainActivity;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.login.LoginActivity;

public class PostActivity extends AppCompatActivity {
    private EditText editTextPost;
    private Button buttonPost;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(DataService.REQUEST_CREATE_POST));

        editTextPost = (EditText) findViewById(R.id.txtPost);
        buttonPost = (Button) findViewById(R.id.btnPost);

        editTextPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editTextPost.getText().length() > 0) buttonPost.setTextColor(getResources().getColor(R.color.white));
                else buttonPost.setTextColor(getResources().getColor(R.color.colorWhiteDisabled));
            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPost.getText().length() > 0) {
                    SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_user), Context.MODE_PRIVATE);
                    String name = sharedPref.getString("name", "user");
                    String img = sharedPref.getString("img", "");

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("sessionId", DataService.SESSION_ID);
                    jsonObject.addProperty("userName", name);
                    jsonObject.addProperty("userImg", img);
                    jsonObject.addProperty("content", editTextPost.getText().toString());
                    ClientApi.call(view.getContext(), DataService.ENGINE_POST, DataService.REQUEST_CREATE_POST, gson.toJson(jsonObject).getBytes());

                } else {
                    Toast.makeText(getApplicationContext(), "Cannot submit an empty post", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            byte[] array = intent.getByteArrayExtra("args");
            JsonObject jsonObject = gson.fromJson(new String(array), JsonElement.class).getAsJsonObject();
            if (jsonObject.get("ack").getAsBoolean()){
                finish();
                Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), jsonObject.get("err").getAsString(), Toast.LENGTH_SHORT).show();
        }
    };
}
