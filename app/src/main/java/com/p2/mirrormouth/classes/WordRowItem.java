package com.p2.mirrormouth.classes;

public class WordRowItem {
    private int playButtonID, recordButtonID, wordID, layoutID, rowID, teamID;
    private int forwardLength = 0;
    private int backwardsLength = 0;
    private String word;
    private String filePath, forwardsFileName, backwardsFileName;
    private boolean readyToRecord = true;
    private boolean readyToPlay = false;

    public WordRowItem(){

    }

    public String toString(){
        return "RowID: " + rowID + "\nTeamID: " + teamID + "\nwordID: " + wordID + "\nplayButtonID: " + playButtonID + "\nrecordButtonID: " + recordButtonID + "\nWord: " + word +
                "\nreadyToPlay: " + readyToPlay +"\nreadytorecord: " + readyToRecord + "\nforwardlength: " + forwardLength +"\nreverselength: " + backwardsLength;
    }

    public WordRowItem(int rowID, int teamID, int layoutID, int wordID, int playButtonID, int recordButtonID, String word){
        this.rowID = rowID;
        this.teamID = teamID;
        this.layoutID = layoutID;
        this.wordID = wordID;
        this.playButtonID = playButtonID;
        this.recordButtonID = recordButtonID;
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

    public int getTeamID() {
        return teamID;
    }

    public void setTeamID(int teamID) {
        this.teamID = teamID;
    }

    public int getRowID() {
        return rowID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public int getLayoutID() {
        return layoutID;
    }

    public void setLayoutID(int layoutID) {
        this.layoutID = layoutID;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public int getRecordButtonID() {
        return recordButtonID;
    }

    public void setRecordButtonID(int recordButtonID) {
        this.recordButtonID = recordButtonID;
    }

    public int getPlayButtonID() {
        return playButtonID;
    }

    public void setPlayButtonID(int playButtonID) {
        this.playButtonID = playButtonID;
    }
    public boolean isReadyToRecord() {
        return readyToRecord;
    }

    public void setReadyToRecord(boolean recording) {
        readyToRecord = recording;
    }
    public boolean isReadyToPlay() {
        return readyToPlay;
    }

    public void setReadyToPlay(boolean playing) {
        readyToPlay = playing;
    }


}
