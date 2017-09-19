package com.spaceuptech.kraft.posts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.spaceuptech.kraft.R;

public class PostActivity extends AppCompatActivity {
    EditText editTextPost;
    Button buttonPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

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
                    finish();
                    Toast.makeText(getApplicationContext(), "Posted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot submit an empty post", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
