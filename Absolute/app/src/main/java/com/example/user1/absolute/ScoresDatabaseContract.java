package com.example.user1.absolute;

import android.provider.BaseColumns;

/**
 * Created by USER1 on 23/04/2017.
 */

public final class ScoresDatabaseContract {
    private ScoresDatabaseContract() {
    }

    public static class ScoresConstants implements BaseColumns {

        public static final String TABLE_NAME = "Scores";
        public static final String COLUMN_NAME_USERS = "Users";
        public static final String COLUMN_NAME_POINTS = "Points";

    }
}