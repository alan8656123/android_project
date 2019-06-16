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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.example.roomwordssample.R;
import com.android.example.roomwordssample.Word;

import java.util.List;

/**
 * This is the adapter for the RecyclerView that displays
 * a list of words.
 */

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    private final LayoutInflater mInflater;
    private List<Word> mWords; // Cached copy of words
    private static ClickListener clickListener;

    WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WordViewHolder holder, final int position) {
        if (mWords != null) {
            Word current = mWords.get(position);
            holder.wordItemView.setText(current.getWord());
            holder.queItemView.setText(current.getQuestion());
            holder.likenumbers.setText(""+current.getLikenum());

            holder.numadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Word currenting = mWords.get(position);
                    int temp=currenting.getLikenum();
                    temp++;


                    MainActivity.client.send("\\U"+currenting.getWord()+"\\Q"+currenting.getQuestion()+"\\N"+temp);
                    //Word update_word=new Word(currenting.getWord(),currenting.getQuestion(),temp);
                    //MainActivity.mWordViewModel.updateWore(update_word);

                }
            });
            holder.numsub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Word currenting = mWords.get(position);
                    int temp=currenting.getLikenum();
                    temp--;

                    MainActivity.client.send("\\U"+currenting.getWord()+"\\Q"+currenting.getQuestion()+"\\N"+temp);
                    //Word update_word=new Word(currenting.getWord(),currenting.getQuestion(),temp);
                    //MainActivity.mWordViewModel.updateWore(update_word);
                }
            });
        } else {
            // Covers the case of data not being ready yet.
            // holder.wordItemView.setText("No Word");
            holder.wordItemView.setText(R.string.no_word);
        }
    }

    /**
     *     Associate a list of words with this adapter
    */

    void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mWords != null)
            return mWords.size();
        else return 0;
    }

    /**
     * Get the word at a given position.
     * This method is useful for identifying which word
     * was clicked or swiped in methods that handle user events.
     *
     * @param position
     * @return The word at the given position
     */
    public Word getWordAtPosition(int position) {
        return mWords.get(position);
    }

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        private final TextView queItemView;
        private final ImageButton numadd;
        private  final ImageButton numsub;
        private final TextView likenumbers;
        private  int like_nums;


        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            queItemView = itemView.findViewById(R.id.textView_q);
            numadd=itemView.findViewById(R.id.like_btn);
            numsub=itemView.findViewById(R.id.dislike_btn);
            likenumbers=itemView.findViewById(R.id.like_number);
            like_nums=0;


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        WordListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }
}