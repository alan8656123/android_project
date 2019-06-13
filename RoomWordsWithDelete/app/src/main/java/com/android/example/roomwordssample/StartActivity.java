package com.android.example.roomwordssample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.android.example.roomwordssample.MainActivity.NEW_WORD_ACTIVITY_REQUEST_CODE;

import com.android.example.roomwordssample.NewWordActivity;
import com.android.example.roomwordssample.R;
import com.android.example.roomwordssample.Word;
import com.android.example.roomwordssample.WordListAdapter;
import com.android.example.roomwordssample.WordViewModel;
public class StartActivity extends AppCompatActivity {
    public static final int START_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }

    public void start_main(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, START_ACTIVITY_REQUEST_CODE);

    }
}
