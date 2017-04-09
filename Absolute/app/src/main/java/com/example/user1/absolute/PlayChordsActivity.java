package com.example.user1.absolute;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    @BindView(R.id.replayNotesFab)
    FloatingActionButton replayNotesFab;

    ChordType chordType;
    int wrongAnswersCounter;//TODO: <== This
    Context context;
    boolean didUserAnswerCorrectly = true;

    int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_chords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        final ArrayList<Integer> notesList = MainActivity.getRawResourcesIds(this);
        context = PlayChordsActivity.this;


        initPlayButton(notesList);

        initReplayButton();
    }

    /**
     * This method is responsible of the clickListeners of the answer buttons in this activity.
     */
    private void initiateCheckAnswers() {

        majorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chordType == ChordType.Major) {
                    playCorrectSoundEffect(majorBtn, context);
                    releasePlayBtnFromFreeze();
                } else
                    playWrongSoundEffect(majorBtn, context);
            }
        });

        minorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chordType == ChordType.Minor) {
                    playCorrectSoundEffect(minorBtn, context);
                    releasePlayBtnFromFreeze();
                } else
                    playWrongSoundEffect(minorBtn, context);
            }
        });

        diminishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chordType == ChordType.Diminished) {
                    playCorrectSoundEffect(diminishedBtn, context);
                    releasePlayBtnFromFreeze();
                } else
                    playWrongSoundEffect(diminishedBtn, context);
            }
        });

        augmentedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chordType == ChordType.Augmented) {
                    playCorrectSoundEffect(augmentedBtn, context);
                    releasePlayBtnFromFreeze();
                } else
                    playWrongSoundEffect(augmentedBtn, context);
            }
        });
    }

    /**
     * This Method sets the type of the chord that the app is going to play, using the {@link ChordType} enum.
     *
     * @return A random chord type from the 4 possibilities: Major, Minor, Diminished and Augmented.
     */
    private ChordType setChordType() {

        ChordType[] chordTypes = ChordType.values();

        Random rnd = new Random();

        return chordTypes[rnd.nextInt(ChordType.values().length)];
    }

    /**
     * This is an enum that is used to arrange the constants symbolizing the 'types' of chords: Major, Minor, Diminished and Augmented.
     */
    enum ChordType {
        Major,
        Minor,
        Diminished,
        Augmented;
    }

    /**
     * This method is in charge of activating the right 'playRandom...Chord()' methods according to the ChordType it gets as a parameter.
     *
     * @param notesList An ArrayList of the resource IDs of the raw audio files.
     * @param chordType The type of the chord that should be played: Major, Minor, Diminished or Augmented.
     */
    private void playChord(ArrayList<Integer> notesList, ChordType chordType) {
        switch (chordType) {
            case Major:
                playRandomMajorChord(notesList);
                break;
            case Minor:
                playRandomMinorChord(notesList);
                break;
            default://PAY ATTENTION! If you get Dim/Aug chords **ALL THE TIME**, then this is probably the problem!
                playRandomDimORAugChord(notesList);
                break;
        }
    }

    /**
     * This method is in charge of activating 3 MediaPlayers simultaneously, in order to create a Major chord.
     *
     * @param notesList An ArrayList of the resource IDs of the raw audio files.
     */
    private void playRandomMajorChord(ArrayList<Integer> notesList) {

        Random random = new Random();
        int index = random.nextInt(notesList.size());

        // 7 semitones is the distance between the first note of the chord and the last one in a Major/Minor chord
        // This is to prevent an indexOutOfBoundsException.
        // (If i don't do this, the MediaPlayer could try to access resources that don't exist.)
        if (index >= notesList.size() - 7)
            index -= 7;

        this.resId = notesList.get(index);
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + 4);// major terza
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + 7);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);

    }

    /**
     * This method is in charge of activating 3 MediaPlayers simultaneously, in order to create a Minor chord.
     *
     * @param notesList An ArrayList of the resource IDs of the raw audio files.
     */
    private void playRandomMinorChord(ArrayList<Integer> notesList) {

        Random random = new Random();

        // 7 semitones is the distance between the first note of the chord and the last one in a Major/Minor chord.
        // This is to prevent an indexOutOfBoundsException.
        // (If i don't do this, the MediaPlayer could try to access resources that don't exist.)
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - 7)
            index -= 7;

        this.resId = notesList.get(index);
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + 3);// minor terza
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + 7);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);
    }

    /**
     * This method is in charge of activating 3 MediaPlayers simultaneously, in order to create a Diminished chord, or an augmented chord, depends on the chordType that was chosen randomly.
     *
     * @param notesList An ArrayList of the resource IDs of the raw audio files.
     */
    private void playRandomDimORAugChord(ArrayList<Integer> notesList) {
        Random random = new Random();
        int index = random.nextInt(notesList.size());
        if (index >= notesList.size() - (chordType.ordinal() + 1) * 2)
            index -= (chordType.ordinal() + 1) * 2;

        this.resId = notesList.get(index);
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + (chordType.ordinal() + 1));
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + (chordType.ordinal() + 1) * 2);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);

    }

    /**
     * This method is responsible of releasing system resources, particularly the MediaPlayers, who should be released after usage.
     *
     * @param mpNote1 The first MediaPlayer to release.
     * @param mpNote2 The second MediaPlayer to release.
     * @param mpNote3 The third MediaPlayer to release.
     */
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

    /**
     * This method returns the Play Button to normal after a correct answer was pressed.
     */
    private void releasePlayBtnFromFreeze() {
        didUserAnswerCorrectly = true;
        playNotesFab.setAlpha(1f);
    }

    /**
     * This method is called if the user wants to hear again the *MAJOR* chord he heard when he pressed the Play Button.
     * @param resId The ID of the resource that contains the root note of the chord.
     */
    private void replayMajorChordById(int resId){
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + 4);// major terza
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + 7);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);
    }

    /**
     * This method is called if the user wants to hear again the *MINOR* chord he heard when he pressed the Play Button.
     * @param resId The ID of the resource that contains the root note of the chord.
     */
    private void replayMinorChordById(int resId){
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + 3);// minor terza
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + 7);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);
    }

    /**
     * This method is called if the user wants to hear again the *DIMINISHED/AUGMENTED* chord he heard
     * when he pressed the Play Button.
     * @param resId The ID of the resource that contains the root note of the chord.
     */
    private void replayDimORAugChordById(int resId){
        MediaPlayer mpNote1 = MediaPlayer.create(getApplicationContext(), resId);
        MediaPlayer mpNote2 = MediaPlayer.create(getApplicationContext(), resId + (chordType.ordinal() + 1));
        MediaPlayer mpNote3 = MediaPlayer.create(getApplicationContext(), resId + (chordType.ordinal() + 1) * 2);

        mpNote1.start();
        mpNote2.start();
        mpNote3.start();

        releaseTheMediaPlayers(mpNote1, mpNote2, mpNote3);
    }

    /**
     * This method replays the chord that was played
     */
    private void initReplayButton(){

        replayNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (chordType) {
                    case Major:
                        replayMajorChordById(resId);
                        break;
                    case Minor:
                        replayMinorChordById(resId);
                        break;
                    default://PAY ATTENTION! If you get Dim/Aug chords **ALL THE TIME**, then this is probably the problem!
                        replayDimORAugChordById(resId);
                        break;
                }
            }
        });
    }

    /**
     * This method initiates the clickListener of the playNotesFab Button.
     * @param notesList The list of the notes from which the app takes a random note to play.
     */
    private void initPlayButton(final ArrayList<Integer> notesList) {
        playNotesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (didUserAnswerCorrectly) {
                    chordType = setChordType();
                    playChord(notesList, chordType);
                    didUserAnswerCorrectly = false;
                    playNotesFab.setAlpha(0.5f);
                    initiateCheckAnswers();
                } else
                    Toast.makeText(context, "You have to answer correctly first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Plays a "Correct" sound effect whenever the user gives a correct answer.
     * This method also turns the button green while the sound effect is playing.
     */
    public static void playCorrectSoundEffect(final Button btn, final Context context) {
        btn.setBackgroundColor(Color.GREEN);
        final MediaPlayer player = MediaPlayer.create(context, R.raw.piano64_c_oct6);
        //TODO: ADD *REAL* 'CORRECT' SOUND ^^^^^
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
                btn.setBackgroundColor(0xFFBBDEFB);//returns the color of the button to the color it was before.
            }
        });
    }

    /**
     * Plays a "Wrong" buzzer sound effect whenever the user gives a wrong answer.
     * This method also turns the button red while the buzzer is playing.
     */
    public static void playWrongSoundEffect(final Button btn, final Context context) {
        btn.setBackgroundColor(Color.RED);
        final MediaPlayer player = MediaPlayer.create(context, R.raw.wrong_sound);
        player.start();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
                btn.setBackgroundColor(0xFFBBDEFB);
            }
        });

        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(600);
    }

}
