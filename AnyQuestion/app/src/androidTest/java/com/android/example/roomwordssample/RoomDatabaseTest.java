package com.android.example.roomwordssample;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.android.example.roomwordssample.RoomDatabaseTest.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.fail;

/**
 * Tests for the Homework "Build and run an app" in Android fundamentals 10.1
 * Create a question-answer app. Start with only questions and let users add new questions and answers.
 * Starter Code:
 * 1. If there is no database, the starter code will create two Questions (Question 1, Question 2) without answers.
 * 2. Clicking Floating Button (+) leads to NewWordActivity with request code NEW_WORD_ACTIVITY_REQUEST_CODE to insert new data.
 * 3. Clicking Recycler Item leads to NewWordActivity with request code UPDATE_WORD_ACTIVITY_REQUEST_CODE to update data.
 * Reminder:
 * 1. Please do not modify any id used in starter code
 * @see <a href="https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/#18">10.1 Part A: Room, LiveData, and ViewModel</a>
 * @see <a href="https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html?index=..%2F..%2Fandroid-training#12">10.1 Part B: Deleting data from a Room database</a>
 */

@RunWith(AndroidJUnit4.class)
public class RoomDatabaseTest {

    static private boolean clearFlag = true;
    private static int DEFAULT_ITEM_NUM = 2;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class){

        @Override
        protected void beforeActivityLaunched() {
            if(clearFlag) {
                // clear database
                String[] databaseList = InstrumentationRegistry.getTargetContext().databaseList();
                while(databaseList.length!=0) {
                    InstrumentationRegistry.getTargetContext().deleteDatabase(databaseList[0]);
                    databaseList = InstrumentationRegistry.getTargetContext().databaseList();
                }
                WordRoomDatabase.deleteInstance();
            }
            super.beforeActivityLaunched();
        }
    };
    private static int listSize = 0;

    /**
     * TODO:
     * 1. add new TextView in recyclerview_item to display answers.
     * 2. add new EditText in activity_new_word to insert/update answer.
     */
    @Test
    public void testAnswerLayout(){

        // waiting for launch activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // === MainActivity ===
        // check whether recycleView exist
        onView(Matchers.<View>instanceOf(RecyclerView.class)).check(matches(isDisplayed()));
        // check item number is greater than 0
        onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(greaterThan(0)));
        // check item layout hierarchy
        onView(allOf(instanceOf(TextView.class), not(withText("Question 1")), hasSibling(withText(equalToIgnoringCase("Question 1"))), withParent(Matchers.<View>instanceOf(LinearLayout.class)))).check(matches(isDisplayed()));

        // === Click Floating Button to NewWordActivity ===
        onView(withId(R.id.fab)).perform(click());
        mActivityRule.getActivity();

        // === NewWordActivity ===
        // Check whether new EditText exist
        onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).check(matches(isDisplayed()));
        onView(withId(R.id.button_save)).perform(click());
        mActivityRule.getActivity();


        // === UpdateActivity ===
        // generate the item number randomly
        Random random = new Random();
        int selected = random.nextInt(listSize);
        // click an item randomly
        onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.actionOnItemAtPosition(selected, click()));
        mActivityRule.getActivity();
        // check whether clicked position is delivered correctly
        onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).check(matches(isDisplayed()));
        onView(withId(R.id.button_save)).perform(click());
    }

    /**
     * TODO:
     * 1. use Room to preserve answers in database
     * 2. use LiveData, ViewModel to display inserted answers on UI
     */
    @Test
    public void testInsertData(){
        // waiting for launch activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // generate random repeat time
        Random random = new Random();
        int repeated = random.nextInt(3)+1;
        for(int i=0;i<repeated;++i) {
            // check number of list
            onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(i+DEFAULT_ITEM_NUM)));
            // generate random number to insert
            int selected = random.nextInt(10)+10*i;
            String questionStr = "Q " + Integer.toString(selected);
            String answerStr = "A " + Integer.toString(selected);
            // enter NewWordActivity
            onView(withId(R.id.fab)).perform(click());
            mActivityRule.getActivity();
            // type data
            onView(withId(R.id.edit_word)).perform(clearText());
            onView(withId(R.id.edit_word)).perform(typeText(questionStr), closeSoftKeyboard());
            onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).perform(clearText());
            onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).perform(typeText(answerStr), closeSoftKeyboard());
            // click save button
            onView(withId(R.id.button_save)).perform(click());
            mActivityRule.getActivity();

            // check whether one data display
            onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(i+DEFAULT_ITEM_NUM+1)));
            onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(equalToIgnoringCase(questionStr)))));
            onView(allOf(instanceOf(TextView.class), not(withText(questionStr)), hasSibling(withText(equalToIgnoringCase(questionStr))), withParent(Matchers.<View>instanceOf(LinearLayout.class)))).check(matches(withText(answerStr)));
        }
    }

    /**
     * TODO:
     * 1. implement the mapping between SQL and function in DAO to update questions and answers
     * 2. display updated data correctly
     */
    @Test
    public void testUpdateAnswer(){
        // waiting for launch activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // check default data
        onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(DEFAULT_ITEM_NUM)));

        // generate random data
        Random random = new Random();
        int selected = random.nextInt(10);
        String questionStr = "Q " + Integer.toString(selected);
        String answerStr = "A " + Integer.toString(selected);

        // random choose one item to update
        selected = random.nextInt(DEFAULT_ITEM_NUM)+1;
        String dieQuestionStr = "Question " + Integer.toString(selected);
        String liveQuestionStr = "Question " + Integer.toString(selected==1?2:1);
        onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.actionOnItemAtPosition(selected-1, click()));
        mActivityRule.getActivity();
        // update data
        onView(withId(R.id.edit_word)).perform(clearText());
        onView(withId(R.id.edit_word)).perform(typeText(questionStr), closeSoftKeyboard());
        onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).perform(clearText());
        onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).perform(typeText(answerStr), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
        mActivityRule.getActivity();

        // check
        onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(DEFAULT_ITEM_NUM)));
        onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(equalToIgnoringCase(questionStr)))));
        onView(allOf(instanceOf(TextView.class), not(withText(questionStr)), hasSibling(withText(equalToIgnoringCase(questionStr))), withParent(Matchers.<View>instanceOf(LinearLayout.class)))).check(matches(withText(answerStr)));
        try {
            onView(withText(dieQuestionStr)).check(matches(isDisplayed()));
            fail();
        }catch(NoMatchingViewException e){

        }
        onView(withText(liveQuestionStr)).check(matches(isDisplayed()));
    }

    /**
     * TODO:
     * 1. Make sure the app work fine with insert/delete operations
     * 2. When restarting the app, the UI display the updated data from last modification.
     */
    @Test
    public void testRestoreData(){
        // waiting for launch activity
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // insert new data
        List<QA> QAList = new ArrayList<>();
        // generate random repeat time
        Random random = new Random();
        int repeated = random.nextInt(2)+2;
        for(int i=0;i<repeated;++i) {
            // check number of list
            onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(i+DEFAULT_ITEM_NUM)));
            // generate random number to insert
            int selected = random.nextInt(10)+10*i;
            String questionStr = "Q " + Integer.toString(selected);
            String answerStr = "A " + Integer.toString(selected);
            QAList.add(new QA(questionStr, answerStr));
            // enter NewWordActivity
            onView(withId(R.id.fab)).perform(click());
            mActivityRule.getActivity();
            // type data
            onView(withId(R.id.edit_word)).perform(clearText());
            onView(withId(R.id.edit_word)).perform(typeText(questionStr), closeSoftKeyboard());
            onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).perform(clearText());
            onView(allOf(instanceOf(EditText.class), not(withId(R.id.edit_word)), hasSibling(withId(R.id.edit_word)))).perform(typeText(answerStr), closeSoftKeyboard());
            // click save button
            onView(withId(R.id.button_save)).perform(click());
            mActivityRule.getActivity();
        }

        mActivityRule.finishActivity();
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        clearFlag = false;
        mActivityRule.launchActivity(startIntent);
        clearFlag = true;

        onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(QAList.size()+DEFAULT_ITEM_NUM)));
        int deleteSelect = random.nextInt(QAList.size());
        for(int i=0;i<QAList.size();++i){
            // check whether all data display
            onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(equalToIgnoringCase(QAList.get(i).qStr)))));
            onView(allOf(instanceOf(TextView.class), not(withText(QAList.get(i).qStr)), hasSibling(withText(equalToIgnoringCase(QAList.get(i).qStr))), withParent(Matchers.<View>instanceOf(LinearLayout.class)))).check(matches(withText(QAList.get(i).aStr)));
            if(i==deleteSelect){
                onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(QAList.get(deleteSelect).qStr)), swipeLeft()));
            }
        }
        QAList.remove(deleteSelect);

        mActivityRule.finishActivity();
        startIntent = new Intent(Intent.ACTION_MAIN);
        clearFlag = false;
        mActivityRule.launchActivity(startIntent);
        clearFlag = true;

        onView(Matchers.<View>instanceOf(RecyclerView.class)).check(withItemCount(equalTo(QAList.size()+DEFAULT_ITEM_NUM)));
        for(int i=0;i<QAList.size();++i){
            // check whether one data display
            onView(Matchers.<View>instanceOf(RecyclerView.class)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(equalToIgnoringCase(QAList.get(i).qStr)))));
            onView(allOf(instanceOf(TextView.class), not(withText(QAList.get(i).qStr)), hasSibling(withText(equalToIgnoringCase(QAList.get(i).qStr))), withParent(Matchers.<View>instanceOf(LinearLayout.class)))).check(matches(withText(QAList.get(i).aStr)));
        }
    }

    private class QA{
        String qStr="";
        String aStr="";
        QA(){}
        QA(String q, String a){
            this.qStr = q;
            this.aStr = a;
        }
    }

    public static class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final Matcher<Integer> matcher;

        public static RecyclerViewItemCountAssertion withItemCount(int expectedCount) {
            return withItemCount(is(expectedCount));
        }

        public static RecyclerViewItemCountAssertion withItemCount(Matcher<Integer> matcher) {
            return new RecyclerViewItemCountAssertion(matcher);
        }

        private RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
            this.matcher = matcher;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            RoomDatabaseTest.listSize = adapter.getItemCount();
            assertThat(adapter.getItemCount(), matcher);
        }
    }

}