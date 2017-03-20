package com.example.user1.absolute;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Random;

import butterknife.BindView;

public class PlayChordsActivity extends AppCompatActivity {

    @BindView(R.id.majorBtn) Button majorBtn;
    @BindView(R.id.minorBtn) Button minorBtn;
    @BindView(R.id.diminishedBtn) Button diminishedBtn;
    @BindView(R.id.augmentedBtn) Button augmentedBtn;
    @BindView(R.id.playNotesFab) FloatingActionButton playNotesFab;
    chordType chordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_chords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        chordType = setChordType();



    }

    /**
     * This Method sets the type of the chord that the app is going to play, using the chordType enum.
     * @return A random chord type from the 4 possibilities: Major, Minor, Diminished and Augmented.
     */
    private chordType setChordType() {

        chordType[] chordTypes = chordType.values();

        Random rnd = new Random();

        return chordTypes[rnd.nextInt(4)];
    }

    enum chordType{
        Major,
        Minor,
        Diminished,

        Augmented;
    }



}
