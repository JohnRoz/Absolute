package com.example.user1.absolute;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//CONSTANTS:
import java.util.ArrayList;

import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.COLUMN_NAME_POINTS;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.COLUMN_NAME_USERS;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.TABLE_NAME;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants._ID;

public class HighScoresActivity extends AppCompatActivity implements AsyncTaskCallbacks{

    String username;
    int score;
    ArrayList<String> scores;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ScoresDbHelper dbHelper = new ScoresDbHelper(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        score = intent.getIntExtra("SCORE", score);
        saveScoreInDb(dbHelper, this, username, score);

        scores = new ArrayList<String>();
        getScoresFromDb(dbHelper, new scoresCallback() {
            @Override
            public void returnScoresArrayList(ArrayList<String> scoresTable) {
                scores = scoresTable;
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scores);
                listView.setAdapter(adapter);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * This method saves the Score a user got under his name. It also returns the ID column value of the player inserted.
     * @param dbHelper A helper object to get the Database.
     * @param context The context of where this static method was called from.
     * @param userName The name under which the score will be saved.
     * @param points The score the player got in the game.
     * @param c A callback method to run as soon as the INSERT command is finished.
     */
    public static void saveScoreInDb(final ScoresDbHelper dbHelper, Context context, final String userName, final int points, final AsyncTaskCallbacks.idCallback c){

        new AsyncTask<Void, Void, Integer>(){
            @Override
            protected Integer doInBackground(Void... params) {

                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_USERS, userName);
                values.put(COLUMN_NAME_POINTS, points);

                // Insert the new row, returning the primary key value of the new row
                Integer newRowId = (int)db.insert(TABLE_NAME, null, values);

                return newRowId;
            }

            @Override
            protected void onPostExecute(Integer newRowId) {
                c.returnID(newRowId);
            }
        }.execute();

    }

    /**
     * This method saves the Score a user got under his name.
     * @param dbHelper A helper object to get the Database.
     * @param context The context of where this static method was called from.
     * @param userName The name under which the score will be saved.
     * @param points The score the player got in the game.
     */
    public static void saveScoreInDb(final ScoresDbHelper dbHelper, Context context, final String userName, final int points){


        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_USERS, userName);
                values.put(COLUMN_NAME_POINTS, points);

                // Insert the new row.
                db.insert(TABLE_NAME, null, values);

                return null;
            }
        }.execute();

    }


    /**
     * This method is to get the whole Scores table from the Database.
     * @param dbHelper A helper object to get the Database.
     * @param c A callback method to run as soon as the SELECT command is finished.
     */
    public static void getScoresFromDb(final ScoresDbHelper dbHelper, final AsyncTaskCallbacks.scoresCallback c){

        new AsyncTask<Void, Void, ArrayList<String>>(){
            @Override
            protected ArrayList<String> doInBackground(Void... params) {

                ArrayList<String> scores = new ArrayList<>();

                SQLiteDatabase db = dbHelper.getReadableDatabase();//THIS REQUIRES ASYNCTASK!!!
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ;", null);

                if(cursor != null){
                    while (cursor.moveToNext()){
                        String username = cursor.getString(1);
                        int  points = cursor.getInt(2);

                        scores.add(username + " - " + points);
                    }
                    cursor.close();
                }

                return scores;
            }

            @Override
            protected void onPostExecute(ArrayList<String> scoresTable) {
                c.returnScoresArrayList(scoresTable);
            }
        }.execute();

    }
}
