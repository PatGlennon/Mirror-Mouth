package com.p2.mirrormouth.ui.game;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p2.mirrormouth.R;
import com.p2.mirrormouth.databinding.FragmentGameBinding;
import com.p2.mirrormouth.classes.wavClass;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class GameFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";

    private FragmentGameBinding binding;
    private MediaRecorder recorder = null;
    private AudioRecord audioRecord = null;
    private String fileName = null;
    private String revFileName = null;
    private MediaPlayer player = null;
    private wavClass wavRecorder = null;
    private Context thisContext = null;
    private Activity thisActivity = null;
    private Calendar calendar;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel =
                new ViewModelProvider(this).get(GameViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ConstraintLayout layout = binding.layout;

        //set context and activity values
        thisContext = getContext();
        thisActivity = getActivity();
        String formattedDate;

        //get calendar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHHmmss");
            formattedDate = now.format(formatter);
        }else{
            formattedDate = String.valueOf(System.currentTimeMillis());
        }


        fileName = thisContext.getExternalCacheDir().getAbsolutePath();
        //fileName += "/audiorecordtest"+formattedDate+".wav";

        revFileName = thisContext.getExternalCacheDir().getAbsolutePath();
        revFileName += "/revtest.wav";

        //Check to make sure recording permissions are set again




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
        word.setBackgroundResource(R.drawable.roundcorner);
        word.setMinimumHeight(48);
        word.setMaxHeight(48);
        word.setMinimumWidth(pxToDp(100));
        word.setAllCaps(true);
        word.setGravity(Gravity.CENTER);
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

        //Add to layout - will need more logic for later rows
        layout.addView(word, wordParams);
        layout.addView(recordButton, recordParams);
        layout.addView(playButton, playParams);

    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
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


    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private void startRecording() {
        wavRecorder = new wavClass(fileName);
        wavRecorder.startRecording();
    }

    private void stopRecording() {
        wavRecorder.stopRecording();

        try {
            reverseSound();
        } catch (InterruptedException e) {
            Log.e("Reverse","Reverse Failed");
            throw new RuntimeException(e);
        }
    }

    class RecordButton extends androidx.appcompat.widget.AppCompatImageButton {
        boolean mStartRecording = true;

        OnClickListener clicker = new OnClickListener() {
            @RequiresPermission(Manifest.permission.RECORD_AUDIO)
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

            //Reading file to byte array wordForward
            InputStream in = new FileInputStream(fileName+"/final_record.wav");
            byte[] wordForward = new byte[in.available()];
            BufferedInputStream bis = new BufferedInputStream(in, 8000);
            DataInputStream dis = new DataInputStream(bis);
            int i = 0;
            while (dis.available() > 0) {
                wordForward[i] = dis.readByte();
                i++;
            }

            //create buffer array with bytes without files header information
            //inverse bytes in array
            int len = wordForward.length;
            int headerLength = 44;
            byte[] output = new byte[len];
            byte[] headers = new byte[44];
            byte[] forwardsAudio =  new byte[len - headerLength];
            byte[] reversedAudio = new byte[len - headerLength];
            byte[] reversedAudioWithHeader = new byte[len];
            int bytesPerSample = 2;
            int lengthWithoutHeader = forwardsAudio.length;

            //copy header info to header file
            System.arraycopy(wordForward, 0, headers, 0, headerLength);
            System.arraycopy(wordForward, headerLength, forwardsAudio,0, len - headerLength);


            int sampleIdentifier = 0;

            for (int k = 0; k < lengthWithoutHeader; k++)
            {
                if (k != 0 && k % bytesPerSample == 0)
                {
                    sampleIdentifier += 2 * bytesPerSample;
                }
                int index = lengthWithoutHeader - bytesPerSample - sampleIdentifier + k;
                reversedAudio[k] = forwardsAudio[index];
            }

            System.arraycopy(headers,0,reversedAudioWithHeader, 0, headerLength);
            System.arraycopy(reversedAudio, 0, reversedAudioWithHeader,headerLength, reversedAudio.length);



            dis.close();

            //write reversed sound to the new file
            OutputStream os = new FileOutputStream(revFileName);
            BufferedOutputStream bos = new BufferedOutputStream(os, 8000);
            DataOutputStream dos = new DataOutputStream(bos);
            dos.write(reversedAudioWithHeader, 0, reversedAudioWithHeader.length - 1);
            dos.flush();
            dos.close();

        } catch (IOException ioe) {

        }
    }
    public int pxToDp(int px){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,displaymetrics);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}