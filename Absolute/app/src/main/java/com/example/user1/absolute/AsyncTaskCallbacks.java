package com.example.user1.absolute;

import java.util.ArrayList;

/**
 * Created by USER1 on 23/04/2017.
 */

public interface AsyncTaskCallbacks {


    interface idCallback{
        void returnID(Integer id);
    }

    interface scoresCallback{
        void returnScoresArrayList(ArrayList<String> scores);
    }
}
