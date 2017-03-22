package com.example.user1.absolute;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayChordsActivity extends AppCompatActivity {

    @BindView(R.id.majorBtn)
    Button majorBtn;
    @BindView(R.id.minorBtn)
    Button minorBtn;
    @BindView(R.id.diminishedBtn)
    Button diminishedBtn;
    @BindView(R.id.augmentedBtn)
    Button augmentedBtn;
    @BindView(R.id.playNotesFab)
    FloatingActionButton playNotesFab;
    ChordType chordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_chords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);



        playNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chordType = setChordType();
                switch (chordType) {
                    case Major:
                        playRandomMajorChord();
                        //Toast.makeText(PlayChordsActivity.this, "Major", Toast.LENGTH_SHORT).show();
                        break;
                    case Minor:
                        playRandomMinorChord();
                        //Toast.makeText(PlayChordsActivity.this, "Minor", Toast.LENGTH_SHORT).show();
                        break;
                    case Diminished:
                        playRandomDimORAugChord();
                        break;
                    case Augmented:
                        playRandomDimORAugChord();
                        break;
                }
            }
        });

        initiateCheckAnswers();


    }

    private void initiateCheckAnswers() {
        majorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chordType == ChordType.Major)
                    Toast.makeText(PlayChordsActivity.this, "CORRECT!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PlayChordsActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
            }
        });

        minorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chordType == ChordType.Minor)
                    Toast.makeText(PlayChordsActivity.this, "CORRECT!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PlayChordsActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
            }
        });

        diminishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chordType == ChordType.Diminished)
                    Toast.makeText(PlayChordsActivity.this, "CORRECT!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PlayChordsActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
            }
        });

        augmentedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chordType == ChordType.Augmented)
                    Toast.makeText(PlayChordsActivity.this, "CORRECT!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(PlayChordsActivity.this, "WRONG!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This Method sets the type of the chord that the app is going to play, using the chordType enum.
     *
     * @return A random chord type from the 4 possibilities: Major, Minor, Diminished and Augmented.
     */
    private ChordType setChordType() {

        ChordType[] chordTypes = ChordType.values();

        Random rnd = new Random();

        return chordTypes[rnd.nextInt(ChordType.values().length)];
    }

    enum ChordType {
        Major,
        Minor,
        Diminished,
        Augmented;
    }

    private void playRandomMajorChord() {
        ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);

        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - 7)
            index -= 7;

        final int resId = notesList.get(index);
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        mpNote1.start();

        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + 4);
        mpNote2.start();

        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + 7);
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);

    }

    private void playRandomMinorChord() {
        ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);

        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - 7)
            index -= 7;

        final int resId = notesList.get(index);
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        mpNote1.start();

        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + 3);
        mpNote2.start();

        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + 7);
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);
    }

    private void releaseTheMediaPlayers(MediaPlayer mpNote1, MediaPlayer mpNote2, MediaPlayer mpNote3) {
        mpNote1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });

        mpNote2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });

        mpNote3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
            }
        });
    }

    private void playRandomDiminishedChord() {
        ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);

        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - 6)
            index -= 6;

        final int resId = notesList.get(index);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), resId);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp = MediaPlayer.create(getApplicationContext(), resId + 3);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp = MediaPlayer.create(getApplicationContext(), resId + 6);
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
        });
    }

    private void playRandomAugmentedChord() {
        ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);

        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - 8)
            index -= 8;

        final int resId = notesList.get(index);
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), resId);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp = MediaPlayer.create(getApplicationContext(), resId + 4);
                mp.start();
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp = MediaPlayer.create(getApplicationContext(), resId + 8);
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
        });
    }

    private void playRandomDimORAugChord(){
        ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);

        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - (chordType.ordinal() + 1) * 2)
            index -= (chordType.ordinal() + 1) * 2;

        final int resId = notesList.get(index);
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + (chordType.ordinal() + 1));
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + (chordType.ordinal() + 1) * 2);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);

    }

    private void playRandomDimORAugChord2(){
        ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);

        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - (chordType.ordinal() + 1) * 2)
            index -= (chordType.ordinal() + 1) * 2;

        int resId = notesList.get(index);

        for(int i = 0; i < 3; i++, resId += (chordType.ordinal() + 1)){
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), resId);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                    mp = null;
                }
            });
        }

    }
}
