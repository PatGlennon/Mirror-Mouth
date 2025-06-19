package com.p2.mirrormouth.ui.game;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.p2.mirrormouth.MainActivityViewModel;
import com.p2.mirrormouth.R;
import com.p2.mirrormouth.classes.GameItem;
import com.p2.mirrormouth.classes.RecRev;
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
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class GameFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";
    private FragmentGameBinding binding;
    private String filePath = null;
    private MediaPlayer player = null;
    private RecRev recorder = null;
    private Context thisContext = null;
    private Activity thisActivity = null;
    private ArrayList<GameItem> rowList = new ArrayList<>();
    private LinearLayout layout = null;
    private MainActivityViewModel mainActivityViewModel;
    private int numOfWords;
    private View root;
    private Boolean gameStarted = false;
    private Integer gameState = 0;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        layout = binding.pageLayout;

        //set context and activity values
        thisContext = getContext();
        thisActivity = getActivity();

        filePath = Objects.requireNonNull(thisContext.getExternalCacheDir()).getAbsolutePath();

        numOfWords = mainActivityViewModel.getNumOfWords();
        playGame();


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

        playGame();

        super.onResume();
    }

    public void playGame(){
        gameState = mainActivityViewModel.getState();

        startGame(2, gameState);

        //startMultiLineGame(mainActivityViewModel.getNumOfWords(), gameState);
    }


    public void instantiateRows(){
        rowList = new ArrayList<>();

        for (int i = 0; i < 5; i++){

            GameItem rowItem = new GameItem();

            switch (i){
                case 0:
                    rowItem = new GameItem(binding.row0,i,filePath);
                    rowItem.setWordEditText(binding.word0);
                    rowItem.setButtonLayout(binding.buttonLayout0);
                    rowItem.setListenButton(binding.listen0);
                    rowItem.setRecordButton(binding.record0);
                    rowItem.setReverseButton(binding.playReverse0);
                    rowItem.setLockInButton(binding.lock0);
                    break;
                case 1:
                    rowItem = new GameItem(binding.row1,i,filePath);
                    rowItem.setWordEditText(binding.word1);
                    rowItem.setButtonLayout(binding.buttonLayout1);
                    rowItem.setListenButton(binding.listen1);
                    rowItem.setRecordButton(binding.record1);
                    rowItem.setReverseButton(binding.playReverse1);
                    rowItem.setLockInButton(binding.lock1);
                    break;
                case 2:
                    rowItem = new GameItem(binding.row2,i,filePath);
                    rowItem.setWordEditText(binding.word2);
                    rowItem.setButtonLayout(binding.buttonLayout2);
                    rowItem.setListenButton(binding.listen2);
                    rowItem.setRecordButton(binding.record2);
                    rowItem.setReverseButton(binding.playReverse2);
                    rowItem.setLockInButton(binding.lock2);
                    break;
                case 3:
                    rowItem = new GameItem(binding.row3,i,filePath);
                    rowItem.setWordEditText(binding.word3);
                    rowItem.setButtonLayout(binding.buttonLayout3);
                    rowItem.setListenButton(binding.listen3);
                    rowItem.setRecordButton(binding.record3);
                    rowItem.setReverseButton(binding.playReverse3);
                    rowItem.setLockInButton(binding.lock3);
                    break;
                case 4:
                    rowItem = new GameItem(binding.row4,i,filePath);
                    rowItem.setWordEditText(binding.word4);
                    rowItem.setButtonLayout(binding.buttonLayout4);
                    rowItem.setListenButton(binding.listen4);
                    rowItem.setRecordButton(binding.record4);
                    rowItem.setReverseButton(binding.playReverse4);
                    rowItem.setLockInButton(binding.lock4);
                    break;
            }

            rowList.add(rowItem);
            Log.e("Row", "Num = " + i);

        }

        mainActivityViewModel.setGameItemList(rowList);

    }

    public void startGame(int numOfRows, int gameState){

        if (mainActivityViewModel.getGameItemList().isEmpty()){
            instantiateRows();
        } else{
            rowList = new ArrayList<>(mainActivityViewModel.getGameItemList());
        }

        Log.e("Num of Rows",""+numOfRows);
        for (int i = 0; i < rowList.size(); i++){
            if (i < numOfRows){
                root.findViewById(rowList.get(i).getLayout().getId()).setVisibility(View.VISIBLE);
            } else{
                root.findViewById(rowList.get(i).getLayout().getId()).setVisibility(View.GONE);
            }
        }

        //if recording found, re-enable all buttons - necessary for state changes
        for (GameItem item : rowList){
            switch (gameState){
                case 0:
                    root.findViewById(item.getListenButton().getId()).setVisibility(View.GONE);
                    if (item.isReadyToPlay()){
                        root.findViewById(item.getReverseButton().getId()).setEnabled(true);
                        root.findViewById(item.getLockInButton().getId()).setEnabled(true);
                    }
                    if (item.isLockedIn()){
                        root.findViewById(item.getWordEditText().getId()).setEnabled(false);
                        root.findViewById(item.getButtonLayout().getId()).setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    //On guess round hide play and record buttons.
                    root.findViewById(item.getListenButton().getId()).setVisibility(View.VISIBLE);
                    root.findViewById(item.getListenButton().getId()).setEnabled(true);
                    root.findViewById(item.getLockInButton().getId()).setEnabled(false);
                    if (item.isReadyToPlayGuess()){
                        root.findViewById(item.getReverseButton().getId()).setEnabled(true);
                        root.findViewById(item.getLockInButton().getId()).setEnabled(true);
                    }



                    if (item.isLockedIn()) {
                        root.findViewById(item.getWordEditText().getId()).setEnabled(false);
                        root.findViewById(item.getButtonLayout().getId()).setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    break;

            }

        }

        setOnClickListeners();
        Button submit = binding.submit;
        if (submit.getVisibility() == View.GONE && gameState != 2){
            submit.setVisibility(View.VISIBLE);
        } else if (gameState == 2){
            submit.setVisibility(View.GONE);
        }
    }


    @Override
    public void onStart(){


        super.onStart();
    }

    private void addNewGameButton(){
        LinearLayout row = (LinearLayout) thisActivity.getLayoutInflater().inflate(R.layout.new_game_button,layout);
        Button newGameButton = row.findViewById(R.id.new_game_button);

        newGameButton.setOnClickListener(v -> resetGame());
    }

    private void resetGame(){
        gameState = 0;
        mainActivityViewModel.setState(0);
        rowList = new ArrayList<>();
        mainActivityViewModel.setGameItemList(new ArrayList<>());
        playGame();
    }


    private void setOnClickListeners(){
        for (GameItem item : rowList) {

            EditText wordEditText = root.findViewById(item.getWordEditText().getId());

            wordEditText.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

            Button recordButton = root.findViewById(item.getRecordButton().getId());
            Button listenButton = root.findViewById(item.getListenButton().getId());
            Button reverseButton = root.findViewById(item.getReverseButton().getId());
            Button lockInButton = root.findViewById(item.getLockInButton().getId());

            recordButton.setOnClickListener(new View.OnClickListener() {

                @RequiresPermission(Manifest.permission.RECORD_AUDIO)
                public void onClick(View v) {
                    if (gameState == 0){
                        onRecord(item.isReadyToRecord(), item.getFilePath(), item.getForwardsFileName());
                        if (item.isReadyToRecord()) {
                            //If button clicked and started recording do this
                            recordButton.setText(R.string.recording);

                            disableRecordButtons(item.getRowNum());

                            //set logic to disable all other record buttons - from array or button IDs?
                        } else {
                            //re-enable record buttons
                            enableRecordButtons();

                            recordButton.setText(R.string.record);
                            //after recording, reverse sound clip
                            //testing having the reverse be in a separate thread - helps dropped frames
                            new Thread(() -> {
                                try {
                                    reverseSound(item.getFilePath(), item.getForwardsFileName(), item.getBackwardsFileName());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();

                            //set logic to re-enable all other buttons - from array or button IDs?
                            item.setReadyToPlay(true);
                            root.findViewById(item.getListenButton().getId()).setEnabled(true);
                            root.findViewById(item.getReverseButton().getId()).setEnabled(true);
                            root.findViewById(item.getLockInButton().getId()).setEnabled(true);
                        }
                        item.setReadyToRecord(!item.isReadyToRecord());
                    }else{
                        //DO THIS IN STATE 1 WHICH IS GUESS MODE
                        onRecord(item.isReadyToRecordGuess(), item.getFilePath(), item.getForwardsGuessFileName());
                        if (item.isReadyToRecordGuess()) {
                            //If button clicked and started recording do this
                            recordButton.setText(R.string.recording);

                            //Disable other record buttons while recording to avoid errors
                            disableRecordButtons(item.getRowNum());
                        } else {
                            //re-enable record buttons
                            enableRecordButtons();

                            recordButton.setText(R.string.record);
                            //after recording, reverse sound clip
                            //testing having the reverse be in a separate thread - helps dropped frames
                            new Thread(() -> {
                                try {
                                    reverseSound(item.getFilePath(), item.getForwardsGuessFileName(), item.getBackwardsGuessFileName());
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();




                            //set logic to re-enable all other buttons - from array or button IDs?
                            item.setReadyToPlayGuess(true);
                            root.findViewById(item.getReverseButton().getId()).setEnabled(true);
                            root.findViewById(item.getLockInButton().getId()).setEnabled(true);
                        }
                        item.setReadyToRecordGuess(!item.isReadyToRecordGuess());
                    }


                }
            });
            listenButton.setOnClickListener(v -> {
                if (item.isReadyToPlay()) {
                    //Change Icon - set ready to false
                    listenButton.setText(R.string.listening);
                    item.setReadyToPlay(false);

                    //disable all other play buttons
                    disablePlayButtons(item.getRowNum());

                    //instantiate media player and set on completion listener to change icon back after file finishes playing
                    player = new MediaPlayer();
                    player.setOnCompletionListener(mediaPlayer -> {
                        player.release();
                        player = null;
                        listenButton.setText(R.string.listen);
                        item.setReadyToPlay(true);

                        //re-enable all play buttons with media files
                        enableAllPlayButtons();

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
                    listenButton.setText(R.string.listen);
                    item.setReadyToPlay(true);

                    //re-enable all play buttons with media files
                    enableAllPlayButtons();
                }
            });
            reverseButton.setOnClickListener(v -> {
                if (gameState == 0) {
                    if (item.isReadyToPlay()) {
                        //Change Icon - set ready to false
                        reverseButton.setText(R.string.gniyalp);
                        item.setReadyToPlay(false);

                        //disable all other play buttons
                        disableReverseButtons(item.getRowNum());

                        //instantiate media player and set on completion listener to change icon back after file finishes playing
                        player = new MediaPlayer();
                        player.setOnCompletionListener(mediaPlayer -> {
                            player.release();
                            player = null;
                            reverseButton.setText(R.string.reverse);
                            item.setReadyToPlay(true);

                            //re-enable all play buttons with media files
                            enableAllPlayButtons();

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
                        reverseButton.setText(R.string.reverse);
                        item.setReadyToPlay(true);

                        //re-enable all play buttons with media files
                        enableAllPlayButtons();
                    }
                } else {
                    if (item.isReadyToPlayGuess()) {
                        //Change Icon - set ready to false
                        reverseButton.setText(R.string.gniyalp);
                        item.setReadyToPlayGuess(false);

                        //disable all other play buttons
                        disableReverseButtons(item.getRowNum());

                        //instantiate media player and set on completion listener to change icon back after file finishes playing
                        player = new MediaPlayer();
                        player.setOnCompletionListener(mediaPlayer -> {
                            player.release();
                            player = null;
                            reverseButton.setText(R.string.reverse);
                            item.setReadyToPlayGuess(true);

                            //re-enable all play buttons with media files
                            enableAllPlayButtons();

                        });
                        try {
                            player.setDataSource(item.getFilePath() + item.getBackwardsGuessFileName());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "prepare() failed");
                        }

                    } else {
                        player.release();
                        player = null;
                        reverseButton.setText(R.string.reverse);
                        item.setReadyToPlayGuess(true);

                        //re-enable all play buttons with media files
                        enableAllPlayButtons();
                    }
                }
            });
            lockInButton.setOnClickListener(v -> {
                if (gameState == 0){
                    if (!wordEditText.getText().toString().isEmpty()) {
                        item.setWord(wordEditText.getText().toString());
                        wordEditText.setEnabled(false);
                        root.findViewById(item.getButtonLayout().getId()).setVisibility(View.GONE);

                        item.setLockedIn(true);

                        if (allLockedIn()) {
                            root.findViewById(R.id.submit).setEnabled(true);
                        }
                    }else{
                        Toast.makeText(thisContext,"Missing Word",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    if (!wordEditText.getText().toString().isEmpty()) {
                        item.setGuessWord(wordEditText.getText().toString());
                        wordEditText.setEnabled(false);
                        thisActivity.findViewById(item.getButtonLayout().getId()).setVisibility(View.GONE);

                        item.setLockedIn(true);

                        if (allLockedIn()) {
                            layout.findViewById(R.id.submit).setEnabled(true);
                        }
                    } else {
                        Toast.makeText(thisContext, "Missing Word", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        //set submit button onClickListener
        Button submitButton = binding.submit;
        submitButton.setOnClickListener(v -> {
            switch(gameState){
                case 0:
                    for (GameItem item : rowList){
                        //Hide answer and re-enable EditText
                        EditText word = root.findViewById(item.getWordEditText().getId());
                        word.setText("");
                        word.setEnabled(true);
                        //make the button layout visible again but show the Listen button and disable the LockIn button
                        root.findViewById(item.getButtonLayout().getId()).setVisibility(View.VISIBLE);
                        root.findViewById(item.getListenButton().getId()).setVisibility(View.VISIBLE);
                        root.findViewById(item.getReverseButton().getId()).setEnabled(false);
                        root.findViewById(item.getLockInButton().getId()).setEnabled(false);
                        //reset lockedIn and disable submit button
                        item.setLockedIn(false);
                        submitButton.setEnabled(false);
                    }
                    gameState = 1;
                    break;
                case 1:
                    for (GameItem item : rowList) {
                        //if row is visible, check answers
                        if (item.getLayout().getVisibility() == View.VISIBLE) {
                            EditText word = root.findViewById(item.getWordEditText().getId());
                            String correct = item.getWord() + "=" + item.getGuessWord();
                            String wrong = item.getWord() + "=/=" + item.getGuessWord();

                            if (item.getWord().equals(item.getGuessWord())) {
                                word.setText(correct);
                                word.setTextColor(getResources().getColor(R.color.play));
                            } else {
                                word.setText(wrong);
                                word.setTextColor(getResources().getColor(R.color.stop));
                            }
                        }
                    }
                    root.findViewById(R.id.submit).setVisibility(View.GONE);
                    addNewGameButton();
                    break;
                case 2:
                    gameState = 0;
            }
        });



    }

    private boolean allLockedIn(){
        boolean lockedIn = true;
        for (GameItem item : rowList){
            if (!item.isLockedIn() && item.getLayout().getVisibility() == View.VISIBLE){
                lockedIn = false;
                break;
            }
        }
        return lockedIn;

    }

    private void disableRecordButtons(int rowNum){
        for (GameItem item : rowList){
            if (item.getRowNum() != rowNum){
                root.findViewById(item.getRecordButton().getId()).setEnabled(false);
            }
        }
    }
    private void enableRecordButtons(){
        for (GameItem item : rowList){
            root.findViewById(item.getRecordButton().getId()).setEnabled(true);
        }
    }
    private void disablePlayButtons(int rowNum){
        for (GameItem item : rowList){
            if (item.getRowNum() != rowNum){
                root.findViewById(item.getListenButton().getId()).setEnabled(false);
            }
            root.findViewById(item.getReverseButton().getId()).setEnabled(false);
        }
    }
    private void disableReverseButtons(int rowNum){
        for (GameItem item : rowList){
            if (item.getRowNum() != rowNum){
                root.findViewById(item.getReverseButton().getId()).setEnabled(false);
            }
            root.findViewById(item.getListenButton().getId()).setEnabled(false);
        }
    }
    private void enableAllPlayButtons(){
        for (GameItem item : rowList){
            if (gameState == 0) {
                if (item.isReadyToPlay()) {
                    root.findViewById(item.getListenButton().getId()).setEnabled(true);
                    root.findViewById(item.getReverseButton().getId()).setEnabled(true);
                }
            }else{
                root.findViewById(item.getListenButton().getId()).setEnabled(true);
                if (item.isReadyToPlayGuess()) {
                    root.findViewById(item.getReverseButton().getId()).setEnabled(true);
                }
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
            Log.e(ioe.getMessage(),ioe.toString());
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

        mainActivityViewModel.setState(gameState);
        mainActivityViewModel.setGameItemList(rowList);
        mainActivityViewModel.setIsStarted(gameStarted);

        Log.println(Log.ERROR,"Destroyed","GameFragment Destroyed");

        binding = null;
    }

}
