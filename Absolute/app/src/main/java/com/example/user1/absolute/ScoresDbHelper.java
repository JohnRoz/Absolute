package com.example.user1.absolute;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//CONSTANTS:
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.COLUMN_NAME_POINTS;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.COLUMN_NAME_USERS;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants.TABLE_NAME;
import static com.example.user1.absolute.ScoresDatabaseContract.ScoresConstants._ID;
/**
 * Created by USER1 on 23/04/2017.
 */

public class ScoresDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AbsoluteScore.db";


    public ScoresDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SCORES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_SCORES_TABLE);//DELETE THE DATABASE AND START OVER
        onCreate(db);
    }

    // SQL COMMANDS:
    private static final String CREATE_SCORES_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INT PRIMARY KEY," +
                    COLUMN_NAME_USERS + " VARCHAR," +
                    COLUMN_NAME_POINTS + " INT);";

    private static final String DELETE_SCORES_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
}
