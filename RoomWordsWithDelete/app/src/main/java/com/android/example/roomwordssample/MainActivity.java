/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.roomwordssample;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.roomwordssample.NewWordActivity;
import com.android.example.roomwordssample.R;
import com.android.example.roomwordssample.Word;
import com.android.example.roomwordssample.WordListAdapter;
import com.android.example.roomwordssample.WordViewModel;

import java.util.List;

/**
 * This class displays a list of words in a RecyclerView.
 * The words are saved in a Room database.
 * The layout for this activity also displays an FAB that
 * allows users to start the NewWordActivity to add new words.
 * Users can delete a word by swiping it away, or delete all words
 * through the Options menu.
 * Whenever a new word is added, deleted, or updated, the RecyclerView
 * showing the list of words automatically updates.
 */
public class MainActivity extends AppCompatActivity {

    public static WordViewModel mWordViewModel;//////////////////////////////////////////////////////////////////////////////////////private to public static

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_WORD_ACTIVITY_REQUEST_CODE = 2;
    private static final int msgKey1 = 1;


    private EditText messageField;
    private static TextView textArea ;
    private Button btnSend;

    public static AlertDialog.Builder builder ;
    public static boolean builder_is_show=false;


    public static String total_message="";

    public static Client client;


    private Word delet_word;

    public static final String EXTRA_DATA_UPDATE_WORD = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_UPDATE_WORD_QUE = "extra_word_to_be_updated_QUE";
    public static final String EXTRA_DATA_UPDATE_WORD_NUM = "extra_word_to_be_updated_NUM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder= new AlertDialog.Builder(this);
        client=new Client(StartActivity.name,"192.168.50.37",8657);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WordListAdapter adapter = new WordListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Setup the WordViewModel
        mWordViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        // Get all the words from the database
        // and associate them to the adapter
        mWordViewModel.getAllWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> words) {
                // Update the cached copy of the words in the adapter.
                adapter.setWords(words);
            }
        });

        // Floating action button setup
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        // Add the functionality to swipe items in the
        // recycler view to delete that item
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a word,
                    // delete that word from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Word myWord = adapter.getWordAtPosition(position);
                        Toast.makeText(MainActivity.this,
                                getString(R.string.delete_word_preamble) + " " +
                                myWord.getWord(), Toast.LENGTH_LONG).show();

                        // Delete the word
                        mWordViewModel.deleteWord(myWord);
                    }
                });
        // Attach the item touch helper to the recycler view
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new WordListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                Word word = adapter.getWordAtPosition(position);
                delet_word=word;
                launchUpdateWordActivity(word);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // The options menu has a single item "Clear all data now"
    // that deletes all the entries in the database
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.clear_data) {
            // Add a toast just for confirmation
            Toast.makeText(this, R.string.clear_data_toast_text, Toast.LENGTH_LONG).show();

            // Delete the existing data
            mWordViewModel.deleteAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** When the user enters a new word in the NewWordActivity,
     * that activity returns the result to this activity.
     * If the user entered a new word, save it in the database.

     * @param requestCode -- ID for the request
     * @param resultCode -- indicates success or failure
     * @param data -- The Intent sent back from the NewWordActivity,
     *             which includes the word that the user entered
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY),data.getStringExtra(NewWordActivity.EXTRA_REPLY_QUE),data.getIntExtra(NewWordActivity.EXTRA_REPLY_NUM,0));
            // Save the data
            mWordViewModel.insert(word);
        } else if (requestCode == UPDATE_WORD_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK) {
            Word word = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY),data.getStringExtra(NewWordActivity.EXTRA_REPLY_QUE),data.getIntExtra(NewWordActivity.EXTRA_REPLY_NUM,0));


           // Word word_delet = new Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY),data.getStringExtra(NewWordActivity.EXTRA_REPLY_QUE));
            mWordViewModel.updateWore(word);
            //mWordViewModel.deleteWord(delet_word);

        }else {
            Toast.makeText(
                    this, R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }
    public void launchUpdateWordActivity( Word word) {
        Intent intent = new Intent(this, NewWordActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_WORD, word.getWord());
        intent.putExtra(EXTRA_DATA_UPDATE_WORD_QUE, word.getQuestion());
        intent.putExtra(EXTRA_DATA_UPDATE_WORD_NUM, word.getLikenum());
        startActivityForResult(intent, UPDATE_WORD_ACTIVITY_REQUEST_CODE);
    }

    public void open_dialog(View view){
        /*MessageDialog mes=new MessageDialog();
        mes.show(getSupportFragmentManager(),"");*/

        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();
        final View textEntryView = inflater.inflate(R.layout.message_layout, null);
        builder.setView(textEntryView);

       messageField=(EditText) textEntryView.findViewById(R.id.messageField);
        textArea=(TextView) textEntryView.findViewById(R.id.textArea);
        btnSend=(Button) textEntryView.findViewById(R.id.btnSend);

        textArea.setText(MainActivity.total_message);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!messageField.getText().equals("")){
                    MainActivity.client.send(messageField.getText().toString());
                    messageField.setText("");

                }

            }
        });

        builder.setMessage(R.string.message_room)
                /*.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                })*/
                .setPositiveButton(R.string.leave, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
        builder_is_show=true;

        Thread t = new Thread(runnable);
        t.start();
    }

    public static void printToconsole(String message) {
        MainActivity.total_message+=message+"\n";
        //textArea.setText(MainActivity.total_message);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            do{
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while (true);
        }
    };
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case msgKey1:
                    textArea.setText(MainActivity.total_message);
                    break;
                default:
                    break;
            }
        }
    };
}