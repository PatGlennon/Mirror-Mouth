package com.p2.mirrormouth;

import com.p2.mirrormouth.classes.CabinItem;
import com.p2.mirrormouth.classes.WordRowItem;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<WordRowItem>> wordItemList;
    private final MutableLiveData<ArrayList<CabinItem>> cabinList;
    private final MutableLiveData<Boolean> isStarted;
    private final MutableLiveData<Integer> gameState;

    public MainActivityViewModel() {
        wordItemList = new MutableLiveData<>();
        wordItemList.setValue(new ArrayList<WordRowItem>());
        cabinList = new MutableLiveData<>();
        cabinList.setValue(new ArrayList<CabinItem>());
        isStarted = new MutableLiveData<>();
        isStarted.setValue(false);
        gameState = new MutableLiveData<>();
        gameState.setValue(0);
    }

    public Integer getState(){
        return gameState.getValue();
    }

    public void setState(Integer state){
        gameState.setValue(state);
    }

    public Boolean isStarted(){
        return isStarted.getValue();
    }

    public void setIsStarted(Boolean isGameStarted){
        isStarted.setValue(isGameStarted);
    }

    public void setArray(ArrayList<WordRowItem> array) {
        wordItemList.setValue(new ArrayList<WordRowItem>(array));
    }

    public LiveData<ArrayList<WordRowItem>> getArray() {
        return wordItemList;
    }

    public void setArrayList(ArrayList<WordRowItem> arrayList){
        wordItemList.getValue().clear();
        wordItemList.setValue(arrayList);
    }

    public ArrayList<WordRowItem> getArrayList() {
        return wordItemList.getValue();
    }
    public void setCabinArray(ArrayList<CabinItem> list){
        cabinList.setValue(new ArrayList<CabinItem>(list));
    }
    public ArrayList<CabinItem> getCabinList(){
        return cabinList.getValue();
    }
}