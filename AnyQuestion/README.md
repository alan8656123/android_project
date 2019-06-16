RoomWordsWithDelete
================

A question-answer app. 
Start with only questions and let users add new questions and answers.

Introduction
------------
### Starter code :
    1. If there is no database, the starter code will create two Questions (Question 1, Question 2) without answers.
    2. Clicking Floating Button (+) leads to NewWordActivity with request code NEW_WORD_ACTIVITY_REQUEST_CODE to insert new data.
    3. Clicking Recycler Item leads to NewWordActivity with request code UPDATE_WORD_ACTIVITY_REQUEST_CODE to update data.

### Reminder
    1. Please do not modify any id used in starter code


Document
------
## Documentation

### `@RunWith(AndroidJUnit4.class) public class RoomDatabaseTest`

Tests for the Homework "Build and run an app" in Android fundamentals 10.1 Create a question-answer app. 

Start with only questions and let users add new questions and answers. 

 * **See also:**
   * <a href="https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/#18">10.1 Part A: Room, LiveData, and ViewModel</a>
   * <a href="https://codelabs.developers.google.com/codelabs/android-training-room-delete-data/index.html?index=..%2F..%2Fandroid-training#12">10.1 Part B: Deleting data from a Room database</a>

### `@Test public void testAnswerLayout()`

TODO: 
<br> 1. add new TextView in recyclerview_item to display answers. <br> 2. add new EditText in activity_new_word to insert/update answer.

### `@Test public void testInsertData()`

TODO: 
<br>1. use Room to preserve answers in database 
<br>2. use LiveData, ViewModel to display inserted answers on UI

### `@Test public void testUpdateAnswer()`

TODO: 
<br>1. implement the mapping between SQL and function in DAO to update questions and answers 
<br>2. display updated data correctly

### `@Test public void testRestoreData()`

TODO: 
<br>1. Make sure the app work fine with insert/delete operations 
<br>2. When restarting the app, the UI display the updated data from last modification.