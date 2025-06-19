package com.p2.mirrormouth;

import com.p2.mirrormouth.classes.CabinItem;
import com.p2.mirrormouth.classes.GameItem;

import java.util.ArrayList;
import java.util.Objects;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<GameItem>> gameItemList;
    private final MutableLiveData<ArrayList<CabinItem>> cabinList;
    private final MutableLiveData<Boolean> isStarted;
    private final MutableLiveData<Integer> gameState;
    private final MutableLiveData<Integer> numOfWords;

    public MainActivityViewModel() {
        gameItemList = new MutableLiveData<>();
        gameItemList.setValue(new ArrayList<>());
        cabinList = new MutableLiveData<>();
        cabinList.setValue(new ArrayList<>());
        isStarted = new MutableLiveData<>();
        isStarted.setValue(false);
        gameState = new MutableLiveData<>();
        gameState.setValue(0);
        numOfWords = new MutableLiveData<>();
        numOfWords.setValue(2);
    }

    public Integer getState(){
        return gameState.getValue();
    }

    public void setState(Integer state){
        gameState.setValue(state);
    }

    public Integer getNumOfWords(){
        return numOfWords.getValue();
    }

    public void setNumOfWords(Integer words){
        numOfWords.setValue(words);
    }

    public Boolean isStarted(){
        return isStarted.getValue();
    }

    public void setIsStarted(Boolean isGameStarted){
        isStarted.setValue(isGameStarted);
    }

    public void setArray(ArrayList<GameItem> array) {
        gameItemList.setValue(new ArrayList<>(array));
    }

    public ArrayList<GameItem> getGameItemList() {
        return gameItemList.getValue();
    }
    public GameItem getRowItem (int rowNum){
        return Objects.requireNonNull(gameItemList.getValue()).get(rowNum);
    }
    public void updateRowItem(int rowNum, GameItem gameItem){
        Objects.requireNonNull(gameItemList.getValue()).set(rowNum, gameItem);
    }

    public void setGameItemList(ArrayList<GameItem> gameItemArrayList){
        gameItemList.setValue(new ArrayList<>(gameItemArrayList));
    }
    public void setCabinArray(ArrayList<CabinItem> list){
        cabinList.setValue(new ArrayList<>(list));
    }
    public ArrayList<CabinItem> getCabinList(){
        return cabinList.getValue();
    }
}