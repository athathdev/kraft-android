package com.spaceuptech.kraft.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.spaceuptech.clientapi.ClientApi;
import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.MainActivity;
import com.spaceuptech.kraft.R;
import com.spaceuptech.kraft.data.LoginResponse;
import com.spaceuptech.kraft.login.LoginActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private String name, email, img, userId, bio, accType;
    private RadioGroup radioGroup;
    private Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(DataService.REQUEST_SIGNUP));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarSignUp);
        Button btnDone = (Button)findViewById(R.id.btnDoneSignUp);
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.imgUserSignUp);
        TextView textViewName = (TextView) findViewById(R.id.lblNameSignUp);
        final EditText editTextUserId = (EditText) findViewById(R.id.txtUserIdSignUp);
        final EditText editTextDescription = (EditText) findViewById(R.id.txtDescriptionSignUp);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupAccountTypeSignUp);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = editTextUserId.getText().toString();
                bio = editTextDescription.getText().toString();
                if(radioGroup.getCheckedRadioButtonId()!=-1){
                    int id= radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(id);
                    int radioId = radioGroup.indexOfChild(radioButton);
                    RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
                    accType = (String) btn.getText();
                }
                if (userId.length() == 0) {
                    Toast.makeText(view.getContext(), "Please enter User ID", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (accType.length() == 0) {
                    Toast.makeText(view.getContext(), "Please select Account Type", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bio.length() == 0) {
                    Toast.makeText(view.getContext(), "Please enter your Bio", Toast.LENGTH_SHORT).show();
                    return;
                }

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("sessionId", DataService.SESSION_ID);
                jsonObject.addProperty("userId", userId);
                jsonObject.addProperty("name", name);
                jsonObject.addProperty("bio", bio);
                jsonObject.addProperty("accType", accType.toLowerCase());
                jsonObject.addProperty("img", img);
                ClientApi.call(view.getContext(), "kraft-login", DataService.REQUEST_SIGNUP, gson.toJson(jsonObject).getBytes());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_user), Context.MODE_PRIVATE);
        name = sharedPref.getString("name", "user");
        email = sharedPref.getString("email", "user@gmail.com");
        img = sharedPref.getString("img", "img");

        textViewName.setText("Hi, " + name);
        Glide.with(this).load(img).into(circleImageView);


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
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return;
            }
            Toast.makeText(getApplicationContext(), jsonObject.get("err").getAsString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    };
}
