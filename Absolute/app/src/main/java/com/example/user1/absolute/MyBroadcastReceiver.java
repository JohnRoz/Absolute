package com.example.user1.absolute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import static com.example.user1.absolute.PlayChordsActivity.chord;
import static com.example.user1.absolute.PlayChordsActivity.chordResId;
import static com.example.user1.absolute.PlayChordsActivity.chordsContextStr;
import static com.example.user1.absolute.PlayNotesActivity.note;
import static com.example.user1.absolute.PlayNotesActivity.noteResId;
import static com.example.user1.absolute.PlayNotesActivity.notesContextStr;

/**
 * Created by USER1 on 27/04/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    String chordsContext;
    String notesContext;
    boolean wasStopped;
    MediaPlayer mediaPlayer;

    public MyBroadcastReceiver() {
        chordsContext = chordsContextStr;
        notesContext = notesContextStr;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String contextStr = context.toString();

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            if (contextStr.equals(chordsContext)) {
                if (chord.isPlaying())
                    stopPlaying(chord);
            } else if (contextStr.equals(notesContext)) {
                if (note.isPlaying())
                    stopPlaying(note);
            }

        }
    }

    private void stopPlaying(MediaPlayer mp) {
        mp.stop();
        mp.release();
        mp = null;
        wasStopped = true;
    }
}
