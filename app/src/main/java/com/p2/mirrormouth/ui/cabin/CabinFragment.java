package com.p2.mirrormouth.ui.cabin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.p2.mirrormouth.MainActivityViewModel;
import com.p2.mirrormouth.R;
import com.p2.mirrormouth.classes.RecRev;
import com.p2.mirrormouth.classes.WordRowItem;
import com.p2.mirrormouth.databinding.FragmentCabinBinding;
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
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CabinFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";
    private FragmentCabinBinding binding;
    private String filePath = null;
    private MediaPlayer player = null;
    private RecRev recorder = null;
    private Context thisContext = null;
    private Activity thisActivity = null;
    private ArrayList<WordRowItem> wordItemList = new ArrayList<WordRowItem>();
    private LinearLayout layout = null;
    private MainActivityViewModel mainActivityViewModel;
    private View root;
    private Boolean gameStarted = false;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        binding = FragmentCabinBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        layout = binding.layout;

        //set context and activity values
        thisContext = getContext();
        thisActivity = getActivity();

        filePath = thisContext.getExternalCacheDir().getAbsolutePath();




        return root;
    }
    @Override
    public void onPause(){
        Log.println(Log.ERROR,"Paused","GameFragment Paused");
        super.onPause();
    }

    @Override
    public void onResume(){
        Log.println(Log.ERROR,"Resumed","GameFragment Resumed");

        startNewMultiLineGame(5);

        super.onResume();
    }

    @Override
    public void onStart(){
        

        super.onStart();
    }

    private void newGame(){
        addNewGameButton();
    }

    private void generateNewGameSettingsLayout(){

    }

    private void startNewMultiLineGame(int numOfRows){
        //create the rows based on user input to be defined later
        for (int i = 1; i <= numOfRows; i++){
            createWordRow(1,i,layout);
        }

        //Update recreated view with old array values
        if (!mainActivityViewModel.getArrayList().isEmpty()){
            wordItemList = new ArrayList<WordRowItem>(mainActivityViewModel.getArrayList());
        }


        for (WordRowItem item : wordItemList){
            if (item.getForwardLength() != 0){
                root.findViewById(item.getPlayButtonID()).setEnabled(true);
            }
        }

        setOnClickListeners();

        //on rotate or change state, if recording exists - set play button to enabled
        for (WordRowItem item : wordItemList){
            Log.println(Log.ERROR,"Set Enabledses", item.toString());
            if (item.getForwardLength() != 0){
                root.findViewById(item.getPlayButtonID()).setEnabled(true);
            }
        }

        addSubmitButton();
    }

    private void addNewGameButton(){
        FrameLayout newGameFrame = new FrameLayout(thisContext);
        Button newGameButton = new Button(thisContext);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        newGameFrame.setForegroundGravity(Gravity.CENTER);

        frameParams.gravity = Gravity.CENTER;
        buttonParams.gravity = Gravity.CENTER;

        newGameButton.setBackgroundResource(R.drawable.new_game_background);
        newGameButton.setGravity(Gravity.CENTER);
        newGameButton.setTextSize(25);
        newGameButton.setElevation(dpToPx(2));
        newGameButton.setText("New Game");

        newGameButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                layout.removeAllViews();
                startNewMultiLineGame(5);
                gameStarted = true;
            }
        });

        layout.addView(newGameFrame,frameParams);
        newGameFrame.addView(newGameButton, buttonParams);

    }

    private void addSubmitButton(){

        Button submitButton = new Button(thisContext);


        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dpToPx(48)
        );

        //set params for container
        buttonParams.topMargin = dpToPx(5);
        buttonParams.leftMargin = dpToPx(2);
        buttonParams.rightMargin = dpToPx(2);

        int buttonId = 9999;

        submitButton.setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
        submitButton.setId(buttonId);
        submitButton.setBackgroundResource(R.drawable.new_game_background);
        submitButton.setText(R.string.submit);
        submitButton.setTextColor(thisActivity.getColor(R.color.white));
        submitButton.setTextSize(25);

        layout.addView(submitButton, buttonParams);

    }


    private void createWordRow(int team, int rowNum, LinearLayout layout){
        LinearLayout buttonLayout = new LinearLayout(thisContext);
        //TextView word = new TextView(thisContext);
        EditText word = new EditText(thisContext);

        ImageButton recordButton = new ImageButton(thisContext);
        ImageButton playButton = new ImageButton(thisContext);

        int rowId = 100*rowNum;
        int teamId = 1000*team;

        String forwardsFileName = "/" + teamId+rowId + "forwards.wav";
        String backwardsFileName = "/" + teamId+rowId + "backwards.wav";

        String rowWord = "Miller Lite";


        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                0,
                dpToPx(48),
                1
        );
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                dpToPx(48),
                dpToPx(48)
        );

        //set params for container
        linearLayoutParams.topMargin = dpToPx(5);

        //set params for word
        textParams.leftMargin = dpToPx(2);
        textParams.rightMargin = dpToPx(2);

        //set params for buttons
        buttonParams.leftMargin = dpToPx(2);
        buttonParams.rightMargin = dpToPx(2);


        //set layout params for the Linear Layout
        buttonLayout.setBackgroundResource(R.drawable.item_background);
        buttonLayout.setMinimumHeight(48);
        buttonLayout.setPadding(dpToPx(5),dpToPx(5),dpToPx(5),dpToPx(5));
        buttonLayout.setElevation(dpToPx(2));
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Set layout params for Word TextArea
        word.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        word.setTextColor(thisActivity.getColor(R.color.black));
        word.setPadding(dpToPx(5),0,0,0);
        word.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        word.setGravity(Gravity.CENTER);
        word.setTextSize(25);

        //set layout params for record button
        recordButton.setImageResource(R.drawable.mic_24px);
        recordButton.setBackgroundResource(R.drawable.roundcorner);

        //set layout params for play button
        playButton.setImageResource(R.drawable.play_circle_24px);
        playButton.setBackgroundResource(R.drawable.roundcorner);
        playButton.setEnabled(false);



        //Set Unique IDs for the items
        int layoutId = teamId + rowId + 1;
        int wordId = teamId + rowId + 2;
        int recordId = teamId + rowId + 3;
        int playId = teamId + rowId + 4;

        buttonLayout.setId(layoutId);
        word.setId(wordId);
        recordButton.setId(recordId);
        playButton.setId(playId);

        //word.setText(rowWord);
        word.setHint("Enter Word Here");
        word.setHintTextColor(thisActivity.getColor(R.color.gray));

        //Add to layout - will need more logic for later rows
        layout.addView(buttonLayout, linearLayoutParams);
        buttonLayout.addView(word, textParams);
        buttonLayout.addView(recordButton, buttonParams);
        buttonLayout.addView(playButton, buttonParams);

        //Add to ArrayList of WordRowItems
        WordRowItem currentItem = new WordRowItem(rowId, teamId, layoutId, wordId, playId, recordId, rowWord);
        currentItem.setFilePath(filePath);
        currentItem.setForwardsFileName(forwardsFileName);
        currentItem.setBackwardsFileName(backwardsFileName);

        wordItemList.add(currentItem);

    }

    private void setOnClickListeners(){
        for (WordRowItem item : wordItemList) {
            //get the view with all new stuff added
            View root = binding.getRoot();

            ImageButton recordButton = (ImageButton) root.findViewById(item.getRecordButtonID());
            ImageButton playButton = (ImageButton) root.findViewById(item.getPlayButtonID());

            recordButton.setOnClickListener(new View.OnClickListener() {

                @RequiresPermission(Manifest.permission.RECORD_AUDIO)
                public void onClick(View v) {
                    onRecord(item.isReadyToRecord(), item.getFilePath(), item.getForwardsFileName());
                    if (item.isReadyToRecord()) {
                        //If button clicked and started recording do this
                        recordButton.setImageResource(R.drawable.stop_circle_24px);

                        disableRecordButtons(item.getRowID());

                        //set logic to disable all other record buttons - from array or button IDs?
                    } else {
                        //re-enable record buttons
                        enableRecordButtons();

                        recordButton.setImageResource(R.drawable.recordbutton);
                        //after recording, reverse sound clip, maybe not best to do here?
                        //testing having the reverse be in a separate thread - helps dropped frames
                        new Thread(() -> {
                            try {
                                reverseSound(item.getFilePath(), item.getForwardsFileName(), item.getBackwardsFileName());
                                //get audio file lengths
                                MediaMetadataRetriever mmr = new MediaMetadataRetriever();

                                //Forward Length
                                Uri uri = Uri.parse(item.getFilePath() + item.getForwardsFileName());
                                mmr.setDataSource(thisContext, uri);
                                String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                int forwardLength = Integer.parseInt(durationStr);
                                item.setForwardLength(forwardLength);

                                //Backward Length
                                uri = Uri.parse(item.getFilePath() + item.getBackwardsFileName());
                                mmr.setDataSource(thisContext, uri);
                                durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                int backwardsLength = Integer.parseInt(durationStr);
                                item.setBackwardsLength(backwardsLength);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();




                        //set logic to re-enable all other buttons - from array or button IDs?
                        item.setReadyToPlay(true);
                        playButton.setEnabled(true);
                    }
                    item.setReadyToRecord(!item.isReadyToRecord());
                }
            });

            playButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (item.isReadyToPlay()) {
                        //Change Icon - set ready to false
                        playButton.setImageResource(R.drawable.stop_circle_24px);
                        item.setReadyToPlay(false);

                        //disable all other play buttons
                        disablePlayButtons(item.getRowID());

                        //instantiate media player and set on completion listener to change icon back after file finishes playing
                        player = new MediaPlayer();
                        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                player.release();
                                player = null;
                                playButton.setImageResource(R.drawable.play_circle_24px);
                                item.setReadyToPlay(true);

                                //re-enable all play buttons with media files
                                enablePlayButtons();

                            }
                        });
                        try {
                            player.setDataSource(item.getFilePath() + item.getBackwardsFileName());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "prepare() failed");
                        }

                    } else {
                        player.release();
                        player = null;
                        playButton.setImageResource(R.drawable.play_circle_24px);
                        item.setReadyToPlay(true);

                        //re-enable all play buttons with media files
                        enablePlayButtons();
                    }
                }
            });
        }

    }

    private void disableRecordButtons(int rowId){
        //get the view with all new stuff added
        View root = binding.getRoot();

        for (WordRowItem item : wordItemList){
            if (item.getRowID() != rowId){
                root.findViewById(item.getRecordButtonID()).setEnabled(false);
            }
        }
    }

    private void enableRecordButtons(){
        //get the view with all new stuff added
        View root = binding.getRoot();

        for (WordRowItem item : wordItemList){
            root.findViewById(item.getRecordButtonID()).setEnabled(true);
        }
    }

    private void disablePlayButtons(int rowId){
        //get the view with all new stuff added
        View root = binding.getRoot();

        for (WordRowItem item : wordItemList){
            if (item.getRowID() != rowId){
                root.findViewById(item.getPlayButtonID()).setEnabled(false);
            }
        }
    }
    private void enablePlayButtons(){
        //get the view with all new stuff added
        View root = binding.getRoot();

        for (WordRowItem item : wordItemList){
            if (item.getForwardLength() != 0) {
                root.findViewById(item.getPlayButtonID()).setEnabled(true);
            }
        }
    }


    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private void onRecord(boolean start, String wavFilePath, String wavFileName) {
        if (start) {
            startRecording(wavFilePath, wavFileName);
        } else {
            stopRecording();
        }
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private void startRecording(String wavFilePath, String wavFileName) {
        recorder = new RecRev(wavFilePath, wavFileName);
        recorder.startRecording();
    }

    private void stopRecording() {
        recorder.stopRecording();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }

    private void reverseSound(String filePath, String forwardsFileName, String backwardsFileName) throws InterruptedException{
        try {

            //Reading file to byte array wordForward
            InputStream in = new FileInputStream(filePath+forwardsFileName);
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
            byte[] forwardsAudio = new byte[len - headerLength];
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
            OutputStream os = new FileOutputStream(filePath+backwardsFileName);
            BufferedOutputStream bos = new BufferedOutputStream(os, 8000);
            DataOutputStream dos = new DataOutputStream(bos);
            dos.write(reversedAudioWithHeader, 0, reversedAudioWithHeader.length - 1);
            dos.flush();
            dos.close();

        } catch (IOException ioe) {

        }
    }
    public int dpToPx(int dp){
        float scale = thisContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        mainActivityViewModel.setArray(wordItemList);
        mainActivityViewModel.setIsStarted(gameStarted);

        Log.println(Log.ERROR,"Destroyed","GameFragment Destroyed");

        binding = null;
    }

}
