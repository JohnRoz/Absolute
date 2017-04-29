package com.example.user1.absolute;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import static com.example.user1.absolute.MainActivity.ACTION_GOTO;
import static com.example.user1.absolute.MainActivity.ACTION_SAVE;
import static com.example.user1.absolute.MainActivity.getRawResourcesListByType;
import static com.example.user1.absolute.PlayChordsActivity.dontBeGreedyMsg;
import static com.example.user1.absolute.PlayChordsActivity.dontLoseMoreLifeMsg;
import static com.example.user1.absolute.PlayChordsActivity.playCorrectSoundEffect;
import static com.example.user1.absolute.PlayChordsActivity.playWrongSoundEffect;

public class PlayNotesActivity extends AppCompatActivity {

    @BindView(R.id.a_note)
    Button aNote;
    @BindView(R.id.b_bemolle_note)
    Button bBemolleNote;
    @BindView(R.id.b_note)
    Button bNote;
    @BindView(R.id.c_note)
    Button cNote;
    @BindView(R.id.c_diese__note)
    Button cDieseNote;
    @BindView(R.id.d_note)
    Button dNote;
    @BindView(R.id.e_bemmole_note)
    Button eBemolleNote;
    @BindView(R.id.e_note)
    Button eNote;
    @BindView(R.id.f_note)
    Button fNote;
    @BindView(R.id.f_diese_note)
    Button fDieseNote;
    @BindView(R.id.g_note)
    Button gNote;
    @BindView(R.id.g_diese_note)
    Button gDieseNote;
    @BindView(R.id.playNoteFab)
    FloatingActionButton playNoteFab;
    @BindView(R.id.replayNoteFab)
    FloatingActionButton replayNoteFab;
    @BindView(R.id.notesStrikeOne)
    ImageView notesStrike1;
    @BindView(R.id.notesStrikeTwo)
    ImageView notesStrike2;
    @BindView(R.id.notesStrikeThree)
    ImageView notesStrike3;
    @BindView(R.id.notesScore)
    TextView textViewScore;
    @BindView(R.id.notesQuitBtn)
    Button quitBtn;

    NoteType noteType;
    int wrongAnswersCounter;
    Context context;
    boolean didUserAnswerCorrectly = true;
    int score;
    boolean isGameOver = false;

    static Integer noteResId;
    static MediaPlayer note;
    static String notesContextStr;

    ArrayList<Integer> aNotesList;
    ArrayList<Integer> bBemolleNotesList;
    ArrayList<Integer> bNotesList;
    ArrayList<Integer> cNotesList;
    ArrayList<Integer> cDieseNotesList;
    ArrayList<Integer> dNotesList;
    ArrayList<Integer> eBemolleNotesList;
    ArrayList<Integer> eNotesList;
    ArrayList<Integer> fNotesList;
    ArrayList<Integer> fDieseNotesList;
    ArrayList<Integer> gNotesList;
    ArrayList<Integer> gDieseNotesList;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_notes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        aNotesList = getRawResourcesListByType(this, "_a_");
        bBemolleNotesList = getRawResourcesListByType(this, "_bbemolle_");
        bNotesList = getRawResourcesListByType(this, "_b_");
        cNotesList = getRawResourcesListByType(this, "_c_");
        cDieseNotesList = getRawResourcesListByType(this, "_cdiese_");
        dNotesList = getRawResourcesListByType(this, "_d_");
        eBemolleNotesList = getRawResourcesListByType(this, "_ebemolle_");
        eNotesList = getRawResourcesListByType(this, "_e_");
        fNotesList = getRawResourcesListByType(this, "_f_");
        fDieseNotesList = getRawResourcesListByType(this, "_fdiese_");
        gNotesList = getRawResourcesListByType(this, "_g_");
        gDieseNotesList = getRawResourcesListByType(this, "_gdiese_");

        context = PlayNotesActivity.this;
        notesContextStr = context.toString();

        wrongAnswersCounter = 0;
        score = 0;

        noteType = setNoteType();

        initPlayButton();

        initReplayButton();

        initQuitButton();

        if (noteType != null)
            initCheckAnswers();
        else
            Toast.makeText(context, "NoteType is null", Toast.LENGTH_LONG).show();

    }

    /**
     * This method replays the note that was played.
     */
    private void initReplayButton() {

        replayNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGameOver) {
                    if (noteResId != null)
                        playNote();
                    else
                        Toast.makeText(context, "You have to play something first in order to replay it.", Toast.LENGTH_SHORT).show();
                } else
                    gameOver();
            }
        });
    }

    /**
     * This method initiates the clickListener of the playNoteFab Button.
     */
    private void initPlayButton() {
        playNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGameOver) {
                    if (didUserAnswerCorrectly) {
                        noteType = setNoteType();
                        playRandomNote(noteType);
                        Toast.makeText(context, "noteType: " + noteType, Toast.LENGTH_LONG).show();
                        didUserAnswerCorrectly = false;
                        playNoteFab.setAlpha(0.5f);
                    } else
                        Toast.makeText(context, "You have to answer correctly first.", Toast.LENGTH_SHORT).show();
                } else
                    gameOver();
            }
        });
    }


    /**
     * This method is in charge of activating the method that sets a new random noteResId value, and then to play the note chosen.
     *
     * @param noteType The type of the note that should be played.
     */
    private void playRandomNote(NoteType noteType) {
        switch (noteType) {
            case A_Note:
                setRandomResIdFromList(aNotesList);
                break;
            case B_Bemolle_Note:
                setRandomResIdFromList(bBemolleNotesList);
                break;
            case B_Note:
                setRandomResIdFromList(bNotesList);
                break;
            case C_Note:
                setRandomResIdFromList(cNotesList);
                break;
            case C_Diese_Note:
                setRandomResIdFromList(cDieseNotesList);
                break;
            case D_Note:
                setRandomResIdFromList(dNotesList);
                break;
            case E_Bemolle_Note:
                setRandomResIdFromList(eBemolleNotesList);
                break;
            case E_Note:
                setRandomResIdFromList(eNotesList);
                break;
            case F_Note:
                setRandomResIdFromList(fNotesList);
                break;
            case F_Diese_Note:
                setRandomResIdFromList(fDieseNotesList);
                break;
            case G_Note:
                setRandomResIdFromList(gNotesList);
                break;
            case G_Diese_Note:
                setRandomResIdFromList(gDieseNotesList);
                break;
        }
        playNote();
    }


    /**
     * This method gets an ArrayList of Integers that represent resource IDs.
     * The method chooses one of those resource IDs randomly, and saves it into the global variable 'noteResId'.
     *
     * @param notes The ArrayList containing the resource IDs
     */
    private void setRandomResIdFromList(ArrayList<Integer> notes) {
        Random random = new Random();
        int index = random.nextInt(notes.size());
        noteResId = notes.get(index);
    }

    /**
     * This method is in charge of playing the note that is represented by the global variable 'noteResId'.
     */
    private void playNote() {

        // Play chosen note
        note = MediaPlayer.create(context, noteResId);
        note.start();

        // When done playing, release the MediaPlayer.
        note.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
                note = null;
            }
        });
    }

    private void initQuitButton() {
        quitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areYouSureYouWantToQuit();
            }
        });
    }

    /**
     * This method is responsible of the clickListeners of the answer buttons in this activity.
     */
    private void initCheckAnswers() {

        checkGenericAnswer(aNote, NoteType.A_Note);
        checkGenericAnswer(bBemolleNote, NoteType.B_Bemolle_Note);
        checkGenericAnswer(bNote, NoteType.B_Note);
        checkGenericAnswer(cNote, NoteType.C_Note);
        checkGenericAnswer(cDieseNote, NoteType.C_Diese_Note);
        checkGenericAnswer(dNote, NoteType.D_Note);
        checkGenericAnswer(eBemolleNote, NoteType.E_Bemolle_Note);
        checkGenericAnswer(eNote, NoteType.E_Note);
        checkGenericAnswer(fNote, NoteType.F_Note);
        checkGenericAnswer(fDieseNote, NoteType.F_Diese_Note);
        checkGenericAnswer(gNote, NoteType.G_Note);
        checkGenericAnswer(gDieseNote, NoteType.G_Diese_Note);

    }

    private void checkGenericAnswer(final Button btn, final NoteType typeOfNote) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isGameOver) {
                    stopNoteIfPlaying(note);
                    if (noteType.equals(typeOfNote)) {//CORRECT ANSWER
                        if (didUserAnswerCorrectly) {
                            dontBeGreedyMsg(context);
                        } else {
                            playCorrectSoundEffect(btn, context);
                            releasePlayBtnFromFreeze();
                            gainScore();
                        }
                    } else {//WRONG ANSWER
                        if (didUserAnswerCorrectly) {
                            dontLoseMoreLifeMsg(context);
                        } else {
                            playWrongSoundEffect(btn, context);
                            wrongAnswersCounter++;
                            changeLifeIcons();
                        }
                    }
                } else
                    gameOver();
            }
        });
    }

    /**
     * This method changes the life icons of the player to X'es when he gives the wrong answer.
     */
    private void changeLifeIcons() {
        switch (wrongAnswersCounter) {
            case 1:
                notesStrike1.setImageResource(R.drawable.x_icon);
                break;
            case 2:
                notesStrike2.setImageResource(R.drawable.x_icon);
                break;
            case 3:
                notesStrike3.setImageResource(R.drawable.x_icon);
                gameOver();
                break;
        }
    }


    /**
     * This method gives the user points for choosing the right answer.
     */
    private void gainScore() {
        score += 20;
        textViewScore.setText("Score: +" + score);
    }

    private void areYouSureYouWantToQuit() {
        if (isGameOver)
            gameOver();
        else {
            new AlertDialog.Builder(context)
                    .setTitle("Hold On!")
                    .setMessage("Are you sure you want to quit the game?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            gameOver();

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // DO NOTHING...
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void gameOver() {
        new AlertDialog.Builder(context)
                .setTitle("GAME OVER!")
                .setMessage("Would you like to save your score?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        saveScoreAlertDialog();

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(PlayNotesActivity.this, MainActivity.class);
                        intent.setAction(ACTION_GOTO);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
        isGameOver = true;
    }

    /**
     * This method activates the Alert
     */
    private void saveScoreAlertDialog() {

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
                        if (!input.getText().toString().equals("")) {
                            userName = input.getText().toString();
                            Intent intent = new Intent(PlayNotesActivity.this, HighScoresActivity.class);
                            intent.putExtra("USERNAME", userName);
                            intent.putExtra("SCORE", score);
                            intent.setAction(ACTION_SAVE);
                            startActivity(intent);
                        } else
                            Toast.makeText(context, "You have to enter a name first", Toast.LENGTH_SHORT).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    /**
     * This method runs when the user gives the answer before the track of the note ended.
     * This method stops the note from playing, and releases the mediaPlayer.
     *
     * @param note The MediaPlayer that plays the note.
     */
    private void stopNoteIfPlaying(MediaPlayer note) {
        if (note != null) {
            try {//THIS THROWS IllegalStateException SOMETIMES FOR AN UNKNOWN REASON!
                // TODO: FIND OUT WHY!
                if (note.isPlaying()) {
                    note.stop();
                    note.release();
                    note = null;
                }
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
                Log.d("EXCEPTION", "stopNoteIfPlaying: " + "SOMETHING WENT WRONG");
            }
        }
    }

    /**
     * This method returns the Play Button to normal after a correct answer was pressed.
     */
    private void releasePlayBtnFromFreeze() {
        didUserAnswerCorrectly = true;
        playNoteFab.setAlpha(1f);
    }

    private NoteType setNoteType() {

        NoteType[] noteTypes = NoteType.values();

        Random rnd = new Random();

        return noteTypes[rnd.nextInt(NoteType.values().length)];
    }

    enum NoteType {
        A_Note,
        B_Bemolle_Note,
        B_Note,
        C_Note,
        C_Diese_Note,
        D_Note,
        E_Bemolle_Note,
        E_Note,
        F_Note,
        F_Diese_Note,
        G_Note,
        G_Diese_Note
    }


}
