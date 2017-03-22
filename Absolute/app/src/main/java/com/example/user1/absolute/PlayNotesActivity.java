package com.example.user1.absolute;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class PlayNotesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.playNotesFab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                Random random = new Random();
                int index = random.nextInt(getRawResourcesIds().size());
                int resId = getRawResourcesIds().get(index);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), resId);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                        mp = null;
                    }
                });
            }
        });
    }


    /**
     * The sinner function (but it's a necessary sin).
     *
     * @return The ArrayList of the names of the resources in R.raw .
     */
    private ArrayList<String> getRawResourcesNames() {

        //an ArrayList to contain the names of the resources of raw.
        //This is the ArrayList to be returned.
        ArrayList<String> rawResourcesNames = new ArrayList<>();

        // (I THINK! = NOT SURE) this is an array of copies of the resources of the raw class
        final Field[] fields = R.raw.class.getDeclaredFields();

        //the loop runs as the length of the 'fields' array -
        // meaning, as the number of the resources raw class has in it
        for (Field field : fields) {
            final String resourceName;
            //the name of the current resource
            resourceName = field.getName();

            //if the id of the resource isn't 0 (if it is it brings up problems)
            if (getApplicationContext().getResources().getIdentifier(resourceName, "raw", "com.example.user1.absolute") != 0)
                //the loop adds each name of each of the resources of raw to an ArrayList
                rawResourcesNames.add(resourceName);


        }

        //The list of names of the resources of the raw class is being returned
        return rawResourcesNames;
    }

    private ArrayList<Integer> getRawResourcesIds() {
        ArrayList<Integer> IDs = new ArrayList<>();
        for (String name : getRawResourcesNames()) {
            int id = getApplicationContext().getResources().getIdentifier(name, "raw", "com.example.user1.absolute");
            IDs.add(id);
        }
        return IDs;
    }

}
