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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.p2.mirrormouth.MainActivityViewModel;
import com.p2.mirrormouth.R;
import com.p2.mirrormouth.classes.CabinItem;
import com.p2.mirrormouth.classes.RecRev;
import com.p2.mirrormouth.databinding.FragmentCabinBinding;

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

public class CabinFragment extends Fragment {

    private static final String LOG_TAG = "AudioRecordTest";
    private FragmentCabinBinding binding;
    private String filePath = null;
    private MediaPlayer player = null;
    private RecRev recorder = null;
    private Context thisContext = null;
    private Activity thisActivity = null;
    private ArrayList<CabinItem> rowList = new ArrayList<>();
    private LinearLayout layout = null;
    private MainActivityViewModel mainActivityViewModel;
    private View root;
    private Boolean gameStarted = false;
    private Integer gameState = 0;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        binding = FragmentCabinBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        layout = binding.layout;

        //set context and activity values
        thisContext = getContext();
        thisActivity = getActivity();

        filePath = Objects.requireNonNull(thisContext.getExternalCacheDir()).getAbsolutePath();

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


        super.onResume();
    }

    public void playGame(){
        gameState = mainActivityViewModel.getState();

        startMultiLineGame(2, gameState);
    }

    public void addRow(int rowNum){
        LinearLayout row = (LinearLayout) thisActivity.getLayoutInflater().inflate(R.layout.row_layout,layout);

        int rowId = 100 * rowNum;

        EditText word = row.findViewById(R.id.word);

        LinearLayout buttonLayout = row.findViewById(R.id.button_layout);

        Button listenButton = row.findViewById(R.id.listen);
        Button recordButton = row.findViewById(R.id.record);
        Button reverseButton = row.findViewById(R.id.playReverse);
        Button lockInButton = row.findViewById(R.id.lock);

        word.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        //Set Unique IDs for the items
        int buttonLayoutId = rowId + 1;
        int wordId = rowId + 2;
        int recordId = rowId + 3;
        int playId = rowId + 4;
        int reverseId = rowId + 5;
        int lockId = rowId + 6;

        String forwardsFileName = "/" + rowNum + "forwards.wav";
        String backwardsFileName = "/" + rowNum + "backwards.wav";
        String forwardsGuessFileName = "/" + rowNum + "forwardsguess.wav";
        String backwardsGuessFileName = "/" + rowNum + "backwardsguess.wav";

        CabinItem currentItem = new CabinItem(row, rowNum, filePath, forwardsFileName, backwardsFileName);

        //mainActivityViewModel.getCabinList().get(rowNum -1)

        currentItem.setForwardsGuessFileName(forwardsGuessFileName);
        currentItem.setBackwardsGuessFileName(backwardsGuessFileName);

        //Change IDs so there's no overlap
        row.setId(rowId);
        buttonLayout.setId(buttonLayoutId);
        word.setId(wordId);
        recordButton.setId(recordId);
        listenButton.setId(playId);
        reverseButton.setId(reverseId);
        lockInButton.setId(lockId);

        //Add IDs to CabinItem
        currentItem.setRowId(rowId);
        currentItem.setButtonLayoutId(buttonLayoutId);
        currentItem.setWordId(wordId);
        currentItem.setRecordId(recordId);
        currentItem.setPlayId(playId);
        currentItem.setReverseId(reverseId);
        currentItem.setLockId(lockId);

        rowList.add(currentItem);

    }

    @Override
    public void onStart(){
        

        super.onStart();
    }

    private void startMultiLineGame(int numOfRows, int gameState){
        //create the rows based on user input to be defined later

        if (gameState != 2) {
            for (int i = 1; i <= numOfRows; i++) {
                addRow(i);
            }
        }

        //Update recreated view with old array values
        if (!mainActivityViewModel.getCabinList().isEmpty()){
            int rowListSize = rowList.size();
            int fragmentListSize = mainActivityViewModel.getCabinList().size();

            int sizeDiff = rowListSize - fragmentListSize;

            Log.e("Sizes","row:"+rowListSize+"\nfrag:"+fragmentListSize+"\ndiff:"+sizeDiff);

            if (sizeDiff == 0){
                Log.e("0","no change");
                rowList = new ArrayList<>(mainActivityViewModel.getCabinList());
            }
            else if (sizeDiff > 0){
                //if game changed to have more items replace new items with old items
                ArrayList<CabinItem> tempList = new ArrayList<>(mainActivityViewModel.getCabinList());

                tempList.addAll(mainActivityViewModel.getCabinList());
                for (int i = fragmentListSize; i < rowListSize; i++){
                    tempList.add(i, rowList.get(i));
                }

                rowList = new ArrayList<>(tempList);

            }
            else {
                for (int i = 0; i < rowListSize; i++) {
                    Log.e("<0","index:"+i);
                    rowList.add(i, mainActivityViewModel.getCabinList().get(i));
                }
            }
        }

        //if recording found, re-enable all buttons - necessary for state changes
        for (CabinItem item : rowList){
            switch (gameState){
                case 0:
                    if (item.isReadyToPlay()){
                        root.findViewById(item.getPlayId()).setEnabled(true);
                        root.findViewById(item.getReverseId()).setEnabled(true);
                        root.findViewById(item.getLockId()).setEnabled(true);
                    }
                    if (item.isLockedIn()){
                        root.findViewById(item.getWordId()).setEnabled(false);
                        root.findViewById(item.getButtonLayoutId()).setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    //On guess round hide play and record buttons.
                    root.findViewById(item.getReverseId()).setEnabled(true);
                    root.findViewById(item.getLockId()).setEnabled(true);
                    root.findViewById(item.getPlayId()).setVisibility(View.GONE);
                    root.findViewById(item.getRecordId()).setVisibility(View.GONE);
                    if (item.isLockedIn()) {
                        root.findViewById(item.getWordId()).setEnabled(false);
                        root.findViewById(item.getButtonLayoutId()).setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    break;

            }

        }

        setOnClickListeners();
        addSubmitButton();
    }
    private void addNewGameButton(){
        LinearLayout row = (LinearLayout) thisActivity.getLayoutInflater().inflate(R.layout.new_game_button,layout);
        Button newGameButton = row.findViewById(R.id.new_game_button);

        newGameButton.setOnClickListener(v -> resetGame());
    }

    private void resetGame(){
        gameState = 0;
        mainActivityViewModel.setState(0);
        for (CabinItem item : rowList){
            item.getLayout().removeAllViewsInLayout();
        }

        mainActivityViewModel.setCabinArray(new ArrayList<>());
        rowList = new ArrayList<>();
        playGame();
    }

    private void addSubmitButton(){
        LinearLayout row = (LinearLayout) thisActivity.getLayoutInflater().inflate(R.layout.submit_button,layout);
        Button submitButton = row.findViewById(R.id.submit);

        submitButton.setOnClickListener(v -> {
            switch(gameState){
                case 0:
                    for (CabinItem item : rowList){
                        //Hide answer and re-enable EditText
                        EditText word = root.findViewById(item.getWordId());
                        word.setText("");
                        word.setEnabled(true);
                        //make the button layout visible again but hide the Play and Record buttons
                        root.findViewById(item.getButtonLayoutId()).setVisibility(View.VISIBLE);
                        root.findViewById(item.getPlayId()).setVisibility(View.VISIBLE);
                        root.findViewById(item.getReverseId()).setEnabled(false);
                        //reset lockedIn and disable submit button
                        item.setLockedIn(false);
                        submitButton.setEnabled(false);
                    }
                    gameState = 1;
                    break;
                case 1:
                    for (CabinItem item : rowList) {
                        EditText word = root.findViewById(item.getWordId());
                        String correct = item.getWord()+"="+item.getGuessWord();
                        String wrong = item.getWord()+"=/="+item.getGuessWord();

                        if (item.getWord().equals(item.getGuessWord())){
                            word.setText(correct);
                            word.setTextColor(getResources().getColor(R.color.play));
                        }else{
                            word.setText(wrong);
                            word.setTextColor(getResources().getColor(R.color.stop));
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

    private void setOnClickListeners(){
        for (CabinItem item : rowList) {
            //get the view with all new stuff added
            EditText word = root.findViewById(item.getWordId());

            Button recordButton = root.findViewById(item.getRecordId());
            Button listenButton = root.findViewById(item.getPlayId());
            Button reverseButton = root.findViewById(item.getReverseId());
            Button lockInButton = root.findViewById(item.getLockId());

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
                            listenButton.setEnabled(true);
                            reverseButton.setEnabled(true);
                            lockInButton.setEnabled(true);
                        }
                        item.setReadyToRecord(!item.isReadyToRecord());
                    }else{
                        //DO THIS IN STATE 1 WHICH IS GUESS MODE

                        onRecord(item.isReadyToRecordGuess(), item.getFilePath(), item.getForwardsGuessFileName());
                        if (item.isReadyToRecordGuess()) {
                            //If button clicked and started recording do this
                            recordButton.setText(R.string.recording);

                            disableRecordButtons(item.getRowNum());

                            //set logic to disable all other record buttons - from array or button IDs?
                        } else {
                            //re-enable record buttons
                            enableRecordButtons();

                            recordButton.setText(R.string.record);
                            //after recording, reverse sound clip, maybe not best to do here?
                            //testing having the reverse be in a separate thread - helps dropped frames
                            new Thread(() -> {
                                try {
                                    reverseSound(item.getFilePath(), item.getForwardsGuessFileName(), item.getBackwardsGuessFileName());
                                    //get audio file lengths
                                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();

                                    //Forward Length
                                    Uri uri = Uri.parse(item.getFilePath() + item.getForwardsGuessFileName());
                                    mmr.setDataSource(thisContext, uri);
                                    String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                    int forwardLength = Integer.parseInt(durationStr);
                                    item.setForwardsGuessLength(forwardLength);

                                    //Backward Length
                                    uri = Uri.parse(item.getFilePath() + item.getBackwardsGuessFileName());
                                    mmr.setDataSource(thisContext, uri);
                                    durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                    int backwardsLength = Integer.parseInt(durationStr);
                                    item.setBackwardsGuessLength(backwardsLength);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }).start();




                            //set logic to re-enable all other buttons - from array or button IDs?
                            item.setReadyToPlayGuess(true);
                            reverseButton.setEnabled(true);
                            lockInButton.setEnabled(true);
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
                    item.setWord(word.getText().toString());
                    if (item.getWord() != null && !item.getWord().isEmpty()) {
                        word.setEnabled(false);
                        root.findViewById(item.getButtonLayoutId()).setVisibility(View.GONE);

                        item.setLockedIn(true);

                        if (allLockedIn()) {
                            layout.findViewById(R.id.submit).setEnabled(true);
                        }
                    }else{
                        Toast.makeText(thisContext,"Missing Word",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    item.setGuessWord(word.getText().toString());
                    if (item.getGuessWord() != null && !item.getGuessWord().isEmpty()) {
                        word.setEnabled(false);
                        root.findViewById(item.getButtonLayoutId()).setVisibility(View.GONE);

                        item.setLockedIn(true);

                        if (allLockedIn()) {
                            layout.findViewById(R.id.submit).setEnabled(true);
                        }
                    }else{
                        Toast.makeText(thisContext,"Missing Word",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private boolean allLockedIn(){
        boolean lockedIn = true;
        for (CabinItem item : rowList){
            if (!item.isLockedIn()){
                lockedIn = false;
                break;
            }
        }
        return lockedIn;

    }

    private void disableRecordButtons(int rowNum){
        for (CabinItem item : rowList){
            if (item.getRowNum() != rowNum){
                root.findViewById(item.getRecordId()).setEnabled(false);
            }
        }
    }
    private void enableRecordButtons(){
        for (CabinItem item : rowList){
            item.getLayout().findViewById(item.getRecordId()).setEnabled(true);
        }
    }
    private void disablePlayButtons(int rowNum){
        for (CabinItem item : rowList){
            if (item.getRowNum() != rowNum){
                root.findViewById(item.getPlayId()).setEnabled(false);
            }
            root.findViewById(item.getReverseId()).setEnabled(false);
        }
    }
    private void disableReverseButtons(int rowNum){
        for (CabinItem item : rowList){
            if (item.getRowNum() != rowNum){
                root.findViewById(item.getReverseId()).setEnabled(false);
            }
            root.findViewById(item.getPlayId()).setEnabled(false);
        }
    }
    private void enableAllPlayButtons(){
        for (CabinItem item : rowList){
            if (gameState == 0) {
                if (item.getForwardLength() != 0) {
                    root.findViewById(item.getPlayId()).setEnabled(true);
                }
                if (item.getBackwardsLength() != 0) {
                    root.findViewById(item.getReverseId()).setEnabled(true);
                }
            }else{
                root.findViewById(item.getPlayId()).setEnabled(true);
                if (item.getBackwardsGuessLength() != 0) {
                    root.findViewById(item.getReverseId()).setEnabled(true);
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
        mainActivityViewModel.setCabinArray(rowList);
        mainActivityViewModel.setIsStarted(gameStarted);

        Log.println(Log.ERROR,"Destroyed","GameFragment Destroyed");

        binding = null;
    }

}
