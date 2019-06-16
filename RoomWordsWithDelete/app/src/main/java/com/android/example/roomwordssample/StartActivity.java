package com.android.example.roomwordssample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static com.android.example.roomwordssample.MainActivity.NEW_WORD_ACTIVITY_REQUEST_CODE;

import com.android.example.roomwordssample.NewWordActivity;
import com.android.example.roomwordssample.R;
import com.android.example.roomwordssample.Word;
import com.android.example.roomwordssample.WordListAdapter;
import com.android.example.roomwordssample.WordViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
    public static final int START_ACTIVITY_REQUEST_CODE = 100;

    private static final int P_TEACHER = 1;
    private static final int P_STUDENT = 2;

    public static String name="";
    public static int Permission=0;

    private int time=0;
    private boolean first_clicked=true;
    private ImageView title_image;
    private ImageView que_image;
    private RadioButton R_teacher;
    private RadioButton R_student;
    private RadioGroup tea_or_std_group;
    private ImageView tea_image;
    private ImageView std_image;
    private TextView class_text;
    private TextView nickname_text;
    private EditText nick_edittext;
    private EditText class_edittext;
    private Activity this_act;
    private Button start_btn;
    private String nickname;
    Timer timer = new Timer(true);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this_act=this;
        timer.schedule(new MyTimerTask(), 2000, 1000);

        title_image=findViewById(R.id.title_image);
        que_image=findViewById(R.id.que_imageview);
        R_teacher=findViewById(R.id.radio_btn_teacher);
        R_student=findViewById(R.id.radio_btn_student);
        tea_or_std_group=findViewById(R.id.tea_std_group);
        tea_image=findViewById(R.id.teacher_img);
        std_image=findViewById(R.id.student_img);
        class_text=findViewById(R.id.class_textview);
        class_edittext=findViewById(R.id.class_editText);
        nickname_text=findViewById(R.id.nicktextView);
        nick_edittext=findViewById(R.id.nickname_editText);
        start_btn=findViewById(R.id.start_btn);


        Animation start_ami=AnimationUtils.loadAnimation(this_act,R.anim.start_anim);
        title_image.setAnimation(start_ami);
        Animation que_start_ami=AnimationUtils.loadAnimation(this_act,R.anim.question_rotate);
        que_image.setAnimation(que_start_ami);
        Animation boo2 =AnimationUtils.loadAnimation(this_act,R.anim.choose_anim);
        R_student.setAnimation(boo2);
        R_teacher.setAnimation(boo2);

        nickname_text.setVisibility(View.INVISIBLE);
        nick_edittext.setVisibility(View.INVISIBLE);
        class_text.setVisibility(View.INVISIBLE);
        class_edittext.setVisibility(View.INVISIBLE);

        tea_image.setVisibility(View.INVISIBLE);
        std_image.setVisibility(View.INVISIBLE);
        start_btn.setVisibility(View.INVISIBLE);
        tea_or_std_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /*class_text.setVisibility(View.VISIBLE);
                class_edittext.setVisibility(View.VISIBLE);*/
                Animation fadein =AnimationUtils.loadAnimation(this_act,R.anim.fadein);
                Animation fadeout =AnimationUtils.loadAnimation(this_act,R.anim.fadeout);

                if(first_clicked) {
                    class_text.setAnimation(fadein);
                    class_edittext.setAnimation(fadein);
                    class_edittext.setVisibility(View.VISIBLE);
                    start_btn.setAnimation(fadein);
                }

                switch (checkedId){
                    case R.id.radio_btn_teacher:
                        tea_image.setAnimation(fadein);
                        if(!first_clicked) {
                            std_image.setAnimation(fadeout);

                            nickname_text.setAnimation(fadeout);
                            nick_edittext.setAnimation(fadeout);
                            class_text.setText("創建新課");
                        }
                        Permission=P_TEACHER;
                        break;
                    case R.id.radio_btn_student:
                        if(!first_clicked)tea_image.setAnimation(fadeout);
                        std_image.setAnimation(fadein);

                        nickname_text.setAnimation(fadein);
                        nick_edittext.setAnimation(fadein);
                        nick_edittext.setVisibility(View.VISIBLE);
                        class_text.setText(R.string.class_name);
                        Permission=P_STUDENT;
                        break;

                }

                if(first_clicked)first_clicked=false;
            }
        });



    }

    public void start_main(View view){
        if( Permission==P_TEACHER){
            name="teacher";
        }else if(Permission==P_STUDENT){
            name=nick_edittext.getText().toString();
        }


        Intent intent = new Intent(this, MainActivity.class);
        startActivityForResult(intent, START_ACTIVITY_REQUEST_CODE);

    }
    public class MyTimerTask extends TimerTask
    {
        public void run()
        {
            time++;
            Log.d("cccc","time"+time);
            if(time==0){


            } else if (time == 5) {

            }
        }
    }

}
