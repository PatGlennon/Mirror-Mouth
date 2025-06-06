package com.p2.mirrormouth.classes;

import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class CabinItem {
    private LinearLayout layout;
    private int rowNum;
    private int rowId, buttonLayoutId, wordId, recordId, playId, reverseId, lockId;
    private int backwardsLength, forwardLength, backwardsGuessLength, forwardsGuessLength;
    private String word, guessWord;
    private String filePath, forwardsFileName, backwardsFileName, forwardsGuessFileName, backwardsGuessFileName;
    private boolean readyToRecord = true;
    private boolean readyToRecordGuess = true;
    private boolean readyToPlayGuess = false;
    private boolean readyToPlay = false;
    private boolean lockedIn = false;

    public CabinItem(){

    }

    public String logOutput(){
        return "LayoutID: " + rowId + "\nWord: " + word + "\nreadyToPlay: " + readyToPlay +"\nreadytorecord: " + readyToRecord
                +"\nButtonLayoutID: " + buttonLayoutId+"\nwordId: " + wordId+"\nrecordId: " + recordId
                +"\nplayId: " + playId+"\nreverseId: " + reverseId+"\nlockid: " + lockId;
    }

    public CabinItem(LinearLayout layout, int rowNum, String filePath, String forwardsFileName, String backwardsFileName){
        this.layout = layout;
        this.rowNum = rowNum;
        this.filePath = filePath;
        this.forwardsFileName = forwardsFileName;
        this.backwardsFileName = backwardsFileName;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void setLayout(LinearLayout layout) {
        this.layout = layout;
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

    public int getBackwardsLength() {
        return backwardsLength;
    }

    public void setBackwardsLength(int backwardsLength) {
        this.backwardsLength = backwardsLength;
    }

    public int getForwardLength() {
        return forwardLength;
    }

    public void setForwardLength(int forwardLength) {
        this.forwardLength = forwardLength;
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
    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getPlayId() {
        return playId;
    }

    public void setPlayId(int playId) {
        this.playId = playId;
    }

    public int getReverseId() {
        return reverseId;
    }

    public void setReverseId(int reverseId) {
        this.reverseId = reverseId;
    }

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public int getButtonLayoutId() {
        return buttonLayoutId;
    }

    public void setButtonLayoutId(int buttonLayoutId) {
        this.buttonLayoutId = buttonLayoutId;
    }
    public int getBackwardsGuessLength() {
        return backwardsGuessLength;
    }

    public void setBackwardsGuessLength(int backwardsGuessLength) {
        this.backwardsGuessLength = backwardsGuessLength;
    }

    public int getForwardsGuessLength() {
        return forwardsGuessLength;
    }

    public void setForwardsGuessLength(int forwardsGuessLength) {
        this.forwardsGuessLength = forwardsGuessLength;
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
