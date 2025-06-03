package com.p2.mirrormouth.ui.game;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.health.connect.datatypes.Record;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p2.mirrormouth.R;
import com.p2.mirrormouth.databinding.FragmentGameBinding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.arthenica.ffmpegkit.FFmpegKit;

public class GameFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";

    private FragmentGameBinding binding;
    private MediaRecorder recorder = null;
    private String fileName = null;
    private String revFileName = null;
    private MediaPlayer player = null;
    private Context thisContext = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel =
                new ViewModelProvider(this).get(GameViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        thisContext = getContext();

        ConstraintLayout layout = binding.layout;

        fileName = thisContext.getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.wav";

        revFileName = thisContext.getExternalCacheDir().getAbsolutePath();
        revFileName += "/revtest.wav";


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = new MediaRecorder(requireContext());
        }

        createWordRow(1,1,layout);

        return root;
    }

    private void createWordRow(int team, int rowNum, ConstraintLayout layout){
        TextView word = new TextView(thisContext);
        RecordButton recordButton = new RecordButton(thisContext);
        PlayButton playButton = new PlayButton(thisContext);
        int rowId = 100*rowNum;
        int teamId = 1000*team;

        ConstraintLayout.LayoutParams wordParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        ConstraintLayout.LayoutParams recordParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        ConstraintLayout.LayoutParams playParams = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        //Set layout params for Word TextArea
        word.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        word.setTextSize(20);

        //set layout params for record button
        recordButton.setImageResource(R.drawable.mic_24px);
        recordButton.setBackgroundResource(R.drawable.roundcorner);
        recordButton.setMinimumWidth(48);
        recordButton.setMinimumHeight(48);

        //set layout params for play button
        playButton.setImageResource(R.drawable.play_circle_24px);
        playButton.setBackgroundResource(R.drawable.roundcorner);
        playButton.setMinimumWidth(48);
        playButton.setMinimumHeight(48);

        //Set Unique IDs for the word rows
        int wordId = teamId+rowId+1;
        int recordId = teamId+rowId+2;
        int playId = teamId+rowId+3;

        word.setId(wordId);
        recordButton.setId(recordId);
        playButton.setId(playId);

        //set params for word
        wordParams.topMargin = 8;
        wordParams.leftMargin = 64;
        wordParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        wordParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        wordParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        wordParams.verticalBias = .2F;

        //set params for record button
        recordParams.bottomToBottom = wordId;
        recordParams.endToStart = playId;
        recordParams.startToEnd = wordId;
        recordParams.topToTop = wordId;
        recordParams.leftMargin = 20;
        recordParams.verticalBias = 1.0F;

        //set params for play button
        playParams.bottomToBottom = recordId;
        playParams.startToEnd = recordId;
        playParams.topToTop = recordId;
        playParams.leftMargin = 20;
        playParams.verticalBias = 0.0F;


        word.setText("Program Word");

        layout.addView(
            word,
            wordParams
        );

        layout.addView(
                recordButton,
                recordParams
        );

        layout.addView(
                playButton,
                playParams
        );

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }
    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(revFileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatImageButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    setImageResource(R.drawable.stop_circle_24px);
                } else {
                    setImageResource(R.drawable.recordbutton);
                    //after recording, reverse sound clip, maybe not best to do here?
                    try {
                        reverseSound();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                mStartRecording = !mStartRecording;
            }
        };

        public RecordButton(Context ctx) {
            super(ctx);
            setImageResource(R.drawable.recordbutton);

            setOnClickListener(clicker);
        }
    }

    class PlayButton extends androidx.appcompat.widget.AppCompatImageButton {
        boolean mStartPlaying = true;

        OnClickListener clicker = new OnClickListener() {
            public void onClick(View v) {
                onPlay(mStartPlaying);
                if (mStartPlaying) {
                    setImageResource(R.drawable.stop_circle_24px);
                } else {
                    setImageResource(R.drawable.play_circle_24px);
                }
                mStartPlaying = !mStartPlaying;
            }
        };

        public PlayButton(Context ctx) {
            super(ctx);
            setImageResource(R.drawable.play_circle_24px);
            setOnClickListener(clicker);
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void reverseSound() throws InterruptedException{
        try {

            //Reading file to main array  bytes 'music'
            InputStream in = new FileInputStream(fileName);
            byte[] music = new byte[in.available()];
            BufferedInputStream bis = new BufferedInputStream(in, 8000);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                music[i] = dis.readByte();
                i++;
            }

            //create buffer array with bytes without files header information
            //inverse bytes in array
            int len = music.length;
            byte[] buff = new byte[len];
            for (int y = 17; y < music.length - 1; y++) {
                buff[len - y - 1] = music[y];
            }

            //put inversed bytes in buffers array to main array 'music'
            for (int y = 17; y > music.length - 1; y++) {
                music[y] = buff[y];
            }
            dis.close();

            //write reversed sound to the new file
            OutputStream os = new FileOutputStream(revFileName);
            BufferedOutputStream bos = new BufferedOutputStream(os, 8000);
            DataOutputStream dos = new DataOutputStream(bos);
            dos.write(music, 0, music.length - 1);
            dos.flush();


        } catch (IOException ioe) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}