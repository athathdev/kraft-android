package com.spaceuptech.kraft.signup;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.spaceuptech.kraft.DataService;
import com.spaceuptech.kraft.MainActivity;
import com.spaceuptech.kraft.R;

public class SignUpActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = (Toolbar)findViewById(R.id.toolbarSignUp);
        btnDone = (Button)findViewById(R.id.btnDoneSignUp);

        startService(new Intent(this, DataService.class));

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
