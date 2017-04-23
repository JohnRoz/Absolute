package com.example.user1.absolute;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    @BindView(R.id.playChordFab)
    FloatingActionButton playChordFab;
    @BindView(R.id.replayChordFab)
    FloatingActionButton replayChordFab;
    @BindView(R.id.chordsStrikeOne)
    ImageView strike1;
    @BindView(R.id.chordsStrikeTwo)
    ImageView strike2;
    @BindView(R.id.chordsStrikeThree)
    ImageView strike3;
    @BindView(R.id.Score)
    TextView textViewScore;
    @BindView(R.id.quitBtn)
    Button quitBtn;

    ChordType chordType;
    int wrongAnswersCounter;
    Context context;
    boolean didUserAnswerCorrectly = true;
    int score;

    Integer resId;
    MediaPlayer chord;

    ArrayList<Integer> augmentedChordsList;
    ArrayList<Integer> diminishedChordsList;
    ArrayList<Integer> majorChordsList;
    ArrayList<Integer> minorChordsList;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_chords);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        augmentedChordsList = MainActivity.getRawResourcesListByType(this, "_aug_");
        diminishedChordsList = MainActivity.getRawResourcesListByType(this, "_dim_");
        majorChordsList = MainActivity.getRawResourcesListByType(this, "_maj_");
        minorChordsList = MainActivity.getRawResourcesListByType(this, "_min_");

        context = PlayChordsActivity.this;

        wrongAnswersCounter = 0;
        score = 0;

        initPlayButton();

        initReplayButton();

        initQuitButton();

    }


    /**
     * This method replays the chord that was played.
     */
    private void initReplayButton() {

        replayChordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resId != null)
                    playChord();
                else
                    Toast.makeText(context, "You have to play something first in order to replay it.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method initiates the clickListener of the playChordFab Button.
     */
    private void initPlayButton() {
        playChordFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (didUserAnswerCorrectly) {
                    chordType = setChordType();
                    playRandomChord(chordType);
                    didUserAnswerCorrectly = false;
                    playChordFab.setAlpha(0.5f);
                    initCheckAnswers();
                } else
                    Toast.makeText(context, "You have to answer correctly first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initQuitButton(){
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver();
            }
        });
    }

    /**
     * This method is responsible of the clickListeners of the answer buttons in this activity.
     */
    private void initCheckAnswers() {

        majorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChordIfPlaying(chord);
                if (chordType.equals(ChordType.Major)) {
                    playCorrectSoundEffect(majorBtn, context);
                    releasePlayBtnFromFreeze();
                    gainScore();
                } else {
                    playWrongSoundEffect(majorBtn, context);
                    wrongAnswersCounter++;
                    changeLifeIcons();
                }
            }
        });

        minorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChordIfPlaying(chord);
                if (chordType.equals(ChordType.Minor)) {
                    playCorrectSoundEffect(minorBtn, context);
                    releasePlayBtnFromFreeze();
                    gainScore();
                } else {
                    playWrongSoundEffect(minorBtn, context);
                    wrongAnswersCounter++;
                    changeLifeIcons();
                }
            }
        });

        diminishedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChordIfPlaying(chord);
                if (chordType.equals(ChordType.Diminished)) {
                    playCorrectSoundEffect(diminishedBtn, context);
                    releasePlayBtnFromFreeze();
                    gainScore();
                } else {
                    playWrongSoundEffect(diminishedBtn, context);
                    wrongAnswersCounter++;
                    changeLifeIcons();
                }
            }
        });

        augmentedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopChordIfPlaying(chord);
                if (chordType.equals(ChordType.Augmented)) {
                    playCorrectSoundEffect(augmentedBtn, context);
                    releasePlayBtnFromFreeze();
                    gainScore();
                } else {
                    playWrongSoundEffect(augmentedBtn, context);
                    wrongAnswersCounter++;
                    changeLifeIcons();
                }
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
        Augmented
    }

    /**
     * This method is in charge of activating the method that sets a new random resId value, and then to play the chord chosen.
     *
     * @param chordType The type of the chord that should be played: Major, Minor, Diminished or Augmented.
     */
    private void playRandomChord(ChordType chordType) {
        switch (chordType) {
            case Major:
                setRandomResIdFromList(majorChordsList);
                break;
            case Minor:
                setRandomResIdFromList(minorChordsList);
                break;
            case Diminished:
                setRandomResIdFromList(diminishedChordsList);
                break;
            case Augmented:
                setRandomResIdFromList(augmentedChordsList);
                break;
        }
        playChord();
    }

    /**
     * This method gets an ArrayList of Integers that represent resource IDs.
     * The method chooses one of those resource IDs randomly, and saves it into the global variable 'resId'.
     *
     * @param chords The ArrayList containing the resource IDs
     */
    private void setRandomResIdFromList(ArrayList<Integer> chords) {
        Random random = new Random();
        int index = random.nextInt(chords.size());
        this.resId = chords.get(index);
    }

    /**
     * This method is in charge of playing the chord that is represented by the global variable 'resId'.
     */
    private void playChord() {

        // Play chosen chord
        chord = MediaPlayer.create(context, resId);
        chord.start();

        // When done playing, release the MediaPlayer.
        chord.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
                chord = null;
            }
        });
    }

    /**
     * This method runs when the user gives the answer before the track of the chord ended.
     * This method stops the chord from playing, and releases the mediaPlayer.
     *
     * @param chord The MediaPlayer that plays the chord.
     */
    private void stopChordIfPlaying(MediaPlayer chord) {
        if (chord != null) {
            try {//THIS THROWS IllegalStateException SOMETIMES FOR AN UNKNOWN REASON!
                // TODO: FIND OUT WHY!
                if (chord.isPlaying()) {
                    chord.stop();
                    chord.release();
                    chord = null;
                }
            } catch (IllegalStateException ex) {
                //Toast.makeText(context, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
                Log.d("EXCEPTION", "stopChordIfPlaying: " + "SOMETHING WENT WRONG");
            }
        }
    }

    /**
     * This method returns the Play Button to normal after a correct answer was pressed.
     */
    private void releasePlayBtnFromFreeze() {
        didUserAnswerCorrectly = true;
        playChordFab.setAlpha(1f);
    }

    /**
     * This method changes the life icons of the player to X'es when he gives the wrong answer.
     */
    private void changeLifeIcons() {
        switch (wrongAnswersCounter) {
            case 1:
                strike1.setImageResource(R.drawable.x_icon);
                break;
            case 2:
                strike2.setImageResource(R.drawable.x_icon);
                break;
            case 3:
                strike3.setImageResource(R.drawable.x_icon);
                gameOver();
                break;
        }
    }

    /**
     * This method gives the user points for choosing the right answer.
     */
    private void gainScore() {
        score += 5;
        textViewScore.setText("Score: +" + score);
    }

    private void gameOver(){
        new AlertDialog.Builder(context)
                .setTitle("GAME OVER!")
                .setMessage("Would you like to save your score?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: SAVE SCORE
                        saveScoreAlertDialog();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PlayChordsActivity.this , MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void saveScoreAlertDialog(){


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set up the input
        final EditText input = new EditText(context);
        // Specify the type of input expected;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setTitle("Enter Your Name!")
                .setNeutralButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userName = input.getText().toString();
                        Intent intent = new Intent (PlayChordsActivity.this , HighScoresActivity.class);
                        intent.putExtra("USERNAME", userName);
                        intent.putExtra("SCORE", score);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();


    }

    /**
     * Plays a "Correct" sound effect whenever the user gives a correct answer.
     * This method also turns the button green while the sound effect is playing.
     */
    public static void playCorrectSoundEffect(final Button btn, final Context context) {
        btn.setBackgroundColor(Color.GREEN);
        final MediaPlayer player = MediaPlayer.create(context, R.raw.piano_note_64_c_oct6);
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
