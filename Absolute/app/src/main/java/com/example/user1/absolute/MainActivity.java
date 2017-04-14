package com.example.user1.absolute;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.trainChordsBtn)
    Button trainChordsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);//ButterKnife is awesome!

        trainChordsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playChordsIntent = new Intent(MainActivity.this, PlayChordsActivity.class);
                startActivity(playChordsIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The sinner function (but it's a necessary sin).
     *
     * @return The ArrayList of the names of the resources in R.raw .
     */
    public static ArrayList<String> getRawResourcesNames(Context context) {

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
            if (context.getResources().getIdentifier(resourceName, "raw", "com.example.user1.absolute") != 0)
                //the loop adds each name of each of the resource of raw to an ArrayList
                rawResourcesNames.add(resourceName);


        }

        //The list of names of the resources of the raw class is being returned
        return rawResourcesNames;
    }

    /**
     * This method gets the IDs of the resources from the 'raw' directory,
     * using the sinner function {@link #getRawResourcesNames}().
     * @param context A Static method cannot use the getApplicationContext() method, so just insert a Context.
     * @return An ArrayList of the IDs of my raw resource files.
     */
    public static ArrayList<Integer> getRawResourcesIds(Context context) {
        ArrayList<Integer> IDs = new ArrayList<>();
        for (String name : getRawResourcesNames(context)) {
            int id = context.getResources().getIdentifier(name, "raw", "com.example.user1.absolute");
            IDs.add(id);
        }
        return IDs;
    }


    /**
     * This method creates an arraylist of IDs of the notes files ONLY. It filters all other files.
     * @param context A Static method cannot use the getApplicationContext() method, so just insert a Context.
     * @return An ArrayList of the IDs of the note sound files in the raw resources directory.
     */
    public static ArrayList<Integer> getNotesList(Context context){
        // Gets an arrayList of strings containing the names of all the notes resources.
        ArrayList<String> allRawResourcesNames = getRawResourcesNames(context);
        ArrayList<String> unwantedRawResourcesNames = new ArrayList<>();
        for(String name : allRawResourcesNames)
            if (!name.contains("_note_"))
                unwantedRawResourcesNames.add(name);

        allRawResourcesNames.removeAll(unwantedRawResourcesNames);

        // The arrayList to be returned.
        // This arrayList holds the IDs of all the notes resources
        ArrayList<Integer> NotesList = new ArrayList<>();
        for (String name : allRawResourcesNames) {
            int id = context.getResources().getIdentifier(name, "raw", "com.example.user1.absolute");
            NotesList.add(id);
        }

        return NotesList;
    }

}
