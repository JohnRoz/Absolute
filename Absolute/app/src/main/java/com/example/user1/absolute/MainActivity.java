package com.example.user1.absolute;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.trainChordsBtn)
    Button trainChordsBtn;
    @BindView(R.id.trainNotesBtn)
    Button trainNotesBtn;
    @BindView(R.id.highScores)
    Button highScores;

    final static String ACTION_SAVE = "com.example.user1.absolute.action.SAVE";
    final static String ACTION_GOTO = "com.example.user1.absolute.action.GOTO";

    // CONSTANTS
    public final static int PERMISSION_READ_PHONE_STATE = 20;

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
                playChordsIntent.setAction(ACTION_GOTO);
                playChordsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(playChordsIntent);
            }
        });

        trainNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playNotesIntent = new Intent(MainActivity.this, PlayNotesActivity.class);
                playNotesIntent.setAction(ACTION_GOTO);
                playNotesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(playNotesIntent);
            }
        });

        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent highScoresIntent = new Intent(MainActivity.this, HighScoresActivity.class);
                highScoresIntent.setAction(ACTION_GOTO);
                highScoresIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(highScoresIntent);
            }
        });

        handlePhoneCallPermission();
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

        switch(id){
            case R.id.action_hello:
                Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void handlePhoneCallPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSION_READ_PHONE_STATE);
        }
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
     *
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
     *
     * @param context A Static method cannot use the getApplicationContext() method, so just insert a Context.
     * @return An ArrayList of the IDs of the note sound files in the raw resources directory.
     */
    public static ArrayList<Integer> getNotesList(Context context) {
        // Gets an arrayList of strings containing the names of all the notes resources.
        ArrayList<String> allRawResourcesNames = getRawResourcesNames(context);
        ArrayList<String> unwantedRawResourcesNames = new ArrayList<>();
        for (String name : allRawResourcesNames)
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

    /**
     * This method creates an arrayList of IDs of the type of files inserted ONLY. It filters all other files.
     *
     * @param context A Static method cannot use the getApplicationContext() method, so just insert a Context.
     * @param type    A string of the form "_A_" where A is the type of raw resource files wanted.
     * @return An ArrayList of the IDs of the augmented chords sound files in the raw resources directory.
     */
    public static ArrayList<Integer> getRawResourcesListByType(Context context, String type) {
        // Gets an arrayList of strings containing the names of all the raw resources.
        ArrayList<String> allRawResourcesNames = getRawResourcesNames(context);
        ArrayList<String> unwantedRawResourcesNames = new ArrayList<>();
        for (String name : allRawResourcesNames)
            if (!name.contains(type))
                unwantedRawResourcesNames.add(name);

        // Filtering any raw resource that is not an augmented chord
        allRawResourcesNames.removeAll(unwantedRawResourcesNames);

        // The arrayList to be returned.
        // This arrayList holds the IDs of all the notes resources
        ArrayList<Integer> augmentedChordsList = new ArrayList<>();
        for (String name : allRawResourcesNames) {
            int id = context.getResources().getIdentifier(name, "raw", "com.example.user1.absolute");
            augmentedChordsList.add(id);
        }

        return augmentedChordsList;
    }

    public static ArrayList<Integer> getOrdinaryNotes(Context context, String type) {
        //This contains, for example all the C notes and the C# notes
        ArrayList<Integer> sameToneNotes = getRawResourcesListByType(context, type);

        //Unwanted types:
        ArrayList<Integer> unwantedDieses = getRawResourcesListByType(context, type + "diese_");
        ArrayList<Integer> unwantedBemolles = getRawResourcesListByType(context, type + "bemolle_");

        sameToneNotes.removeAll(unwantedDieses);
        sameToneNotes.removeAll(unwantedBemolles);

        return sameToneNotes;
    }


}
