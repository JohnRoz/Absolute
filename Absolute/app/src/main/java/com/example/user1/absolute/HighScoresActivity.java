package com.example.user1.absolute;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//CONSTANTS:
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.user1.absolute.MainActivity.ACTION_GOTO;
import static com.example.user1.absolute.MainActivity.ACTION_SAVE;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.COLUMN_NAME_POINTS;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.COLUMN_NAME_USERS;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.TABLE_NAME;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants._ID;

public class HighScoresActivity extends AppCompatActivity implements AsyncTaskCallbacks {

    final static String SEPARATOR = " : ";

    @BindView(R.id.highScoresListView)
    ListView listView;

    String username;
    int score;
    ArrayList<String> scores;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        final ScoresDbHelper dbHelper = new ScoresDbHelper(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        switch (action) {
            case ACTION_SAVE:
                saveInDb(dbHelper, intent);
                break;
        }

        initScoresList(dbHelper);

        initDeleteItem(dbHelper);
    }

    /**
     * This method gets the ListView of the scores ready and selects the scores from the Database.
     *
     * @param dbHelper A helper object to get the Database.
     */
    private void initScoresList(ScoresDbHelper dbHelper) {
        scores = new ArrayList<String>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scores);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);

        getScoresFromDb(dbHelper, new scoresCallback() {
            @Override
            public void returnScoresArrayList(ArrayList<String> scoresTable) {
                adapter.addAll(scoresTable);

            }
        });
    }

    /**
     * This method is to organize the code in the onCreate() and make it more readable.
     *
     * @param dbHelper A helper object to get the Database.
     * @param intent   The Intent that moved the user into this activity.
     */
    private void saveInDb(ScoresDbHelper dbHelper, Intent intent) {
        username = intent.getStringExtra("USERNAME");
        score = intent.getIntExtra("SCORE", score);
        saveScoreInDb(dbHelper, this, username, score);
    }

    /**
     * This method saves the Score a user got under his name. It also returns the ID column value of the player inserted.
     *
     * @param dbHelper A helper object to get the Database.
     * @param context  The context of where this static method was called from.
     * @param userName The name under which the score will be saved.
     * @param points   The score the player got in the game.
     * @param c        A callback method to run as soon as the INSERT command is finished.
     */
    public static void saveScoreInDb(final ScoresDbHelper dbHelper, Context context, final String userName, final int points, final AsyncTaskCallbacks.idCallback c) {

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {

                // Gets the data repository in write mode
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(COLUMN_NAME_USERS, userName);
                values.put(COLUMN_NAME_POINTS, points);

                // Insert the new row, returning the primary key value of the new row
                Integer newRowId = (int) db.insert(TABLE_NAME, null, values);

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
     *
     * @param dbHelper A helper object to get the Database.
     * @param context  The context of where this static method was called from.
     * @param userName The name under which the score will be saved.
     * @param points   The score the player got in the game.
     */
    public static void saveScoreInDb(final ScoresDbHelper dbHelper, Context context, final String userName, final int points) {


        new AsyncTask<Void, Void, Void>() {
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
     * (SELECT SQL COMMAND)
     *
     * @param dbHelper A helper object to get the Database.
     * @param c        A callback method to run as soon as the SELECT command is finished.
     */
    public static void getScoresFromDb(final ScoresDbHelper dbHelper, final AsyncTaskCallbacks.scoresCallback c) {

        new AsyncTask<Void, Void, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(Void... params) {

                ArrayList<String> scores = new ArrayList<>();

                SQLiteDatabase db = dbHelper.getReadableDatabase();//THIS REQUIRES ASYNCTASK!!!
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_NAME_POINTS + " DESC;", null);

                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        String username = cursor.getString(1);
                        int points = cursor.getInt(2);

                        scores.add(username + SEPARATOR + points);
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

    /**
     * This method deletes all the values in the Scores Table, using the DELETE SQL Command.
     *
     * @param dbHelper A helper object to get the Database.
     */
    private void clearScoresTable(final SQLiteOpenHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " ;");

    }

    /**
     * This method deleted an Item with a specific position in the ListView from the Scores Table.
     *
     * @param dbHelper A helper object to get the Database.
     * @param username The value of column number 1 in the Database.
     * @param points   The value of column number 2 in the Database.
     */
    private void deleteItemFromScoresTable(final SQLiteOpenHelper dbHelper, final String username, final int points) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_USERS + " = '" + username + "' AND " + COLUMN_NAME_POINTS + " = " + points + " ;");

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                refreshAdapter();
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HighScoresActivity.this, MainActivity.class);
        intent.setAction(ACTION_GOTO);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Initiate the onItemLongClickListener of the Listviw in order to delete items.
     *
     * @param dbHelper A helper object to get the Database.
     */
    private void initDeleteItem(final SQLiteOpenHelper dbHelper) {
        if (listView != null) {
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    Snackbar.make(view, "Are you SURE you want to delete that Score ?", Snackbar.LENGTH_LONG)
                            .setAction("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // If the user pressed "YES" in the Snackbar, the score will be deleted:

                                    //Get the username & the points of the specific player that was pressed.
                                    String item = listView.getItemAtPosition(position).toString();
                                    String username = item.split(SEPARATOR)[0];
                                    int points = Integer.parseInt(item.split(SEPARATOR)[1]);

                                    deleteItemFromScoresTable(dbHelper, username, points);
                                    adapter.remove(adapter.getItem(position));

                                }
                            }).show();
                    return true;
                }
            });
        }
    }

    private void refreshAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
