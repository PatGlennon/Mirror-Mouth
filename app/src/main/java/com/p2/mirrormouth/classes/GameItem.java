package com.p2.mirrormouth.classes;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class GameItem {
    private LinearLayout rowLayout, buttonLayout;
    private int rowNum;
    private EditText wordEditText;
    private Button recordButton;
    private Button listenButton;
    private Button reverseButton;
    private Button lockInButton;
    private String word, guessWord;
    private String filePath, forwardsFileName, backwardsFileName, forwardsGuessFileName, backwardsGuessFileName;
    private boolean readyToRecord, readyToRecordGuess, readyToPlayGuess, readyToPlay, lockedIn;

    public GameItem(){
        readyToRecord = true;
        readyToRecordGuess = true;
        readyToPlay = false;
        readyToPlayGuess = false;
        lockedIn = false;
    }

    public String logOutput(){
        return "";
    }

    public GameItem(LinearLayout rowLayout, int rowNum, String filePath){
        //set Defaults for button logic
        readyToRecord = true;
        readyToRecordGuess = true;
        readyToPlay = false;
        readyToPlayGuess = false;
        lockedIn = false;

        this.rowLayout = rowLayout;
        this.rowNum = rowNum;
        this.filePath = filePath;

        forwardsFileName = "/" + rowNum + "forwards.wav";
        backwardsFileName = "/" + rowNum + "backwards.wav";
        forwardsGuessFileName = "/" + rowNum + "forwardsguess.wav";
        backwardsGuessFileName = "/" + rowNum + "backwardsguess.wav";
    }

    public LinearLayout getRowLayout() {
        return rowLayout;
    }

    public void setRowLayout(LinearLayout rowLayout) {
        this.rowLayout = rowLayout;
    }

    public LinearLayout getButtonLayout() {
        return buttonLayout;
    }

    public void setButtonLayout(LinearLayout buttonLayout) {
        this.buttonLayout = buttonLayout;
    }

    public EditText getWordEditText() {
        return wordEditText;
    }

    public void setWordEditText(EditText wordEditText) {
        this.wordEditText = wordEditText;
    }

    public Button getRecordButton() {
        return recordButton;
    }

    public void setRecordButton(Button recordButton) {
        this.recordButton = recordButton;
    }

    public Button getListenButton() {
        return listenButton;
    }

    public void setListenButton(Button listenButton) {
        this.listenButton = listenButton;
    }

    public Button getReverseButton() {
        return reverseButton;
    }

    public void setReverseButton(Button reverseButton) {
        this.reverseButton = reverseButton;
    }

    public Button getLockInButton() {
        return lockInButton;
    }

    public void setLockInButton(Button lockInButton) {
        this.lockInButton = lockInButton;
    }
    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public LinearLayout getLayout() {
        return rowLayout;
    }

    public void setLayout(LinearLayout layout) {
        this.rowLayout = layout;
    }

    public String getBackwardsFileName() {
        return backwardsFileName;
    }

    public void setBackwardsFileName(String backwardsFileName) {
        this.backwardsFileName = backwardsFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getForwardsFileName() {
        return forwardsFileName;
    }

    public void setForwardsFileName(String forwardsFileName) {
        this.forwardsFileName = forwardsFileName;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getGuessWord() {
        return guessWord;
    }

    public void setGuessWord(String guessWord) {
        this.guessWord = guessWord;
    }

    public boolean isReadyToRecord() {
        return readyToRecord;
    }

    public void setReadyToRecord(boolean recording) {
        readyToRecord = recording;
    }
    public boolean isLockedIn() {
        return lockedIn;
    }

    public void setLockedIn(boolean lockedIn) {
        this.lockedIn = lockedIn;
    }
    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(boolean playing) {
        readyToPlay = playing;
    }

    public String getForwardsGuessFileName() {
        return forwardsGuessFileName;
    }

    public void setForwardsGuessFileName(String forwardsGuessFileName) {
        this.forwardsGuessFileName = forwardsGuessFileName;
    }

    public String getBackwardsGuessFileName() {
        return backwardsGuessFileName;
    }

    public void setBackwardsGuessFileName(String backwardsGuessFileName) {
        this.backwardsGuessFileName = backwardsGuessFileName;
    }

    public boolean isReadyToRecordGuess() {
        return readyToRecordGuess;
    }

    public void setReadyToRecordGuess(boolean readyToRecordGuess) {
        this.readyToRecordGuess = readyToRecordGuess;
    }

    public boolean isReadyToPlayGuess() {
        return readyToPlayGuess;
    }

    public void setReadyToPlayGuess(boolean readyToPlayGuess) {
        this.readyToPlayGuess = readyToPlayGuess;
    }


}
