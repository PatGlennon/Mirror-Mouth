package com.p2.mirrormouth;

import com.p2.mirrormouth.classes.WordRowItem;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<WordRowItem>> wordItemList;
    private final MutableLiveData<Boolean> isStarted;

    public MainActivityViewModel() {
        wordItemList = new MutableLiveData<>();
        wordItemList.setValue(new ArrayList<WordRowItem>());
        isStarted = new MutableLiveData<>();
        isStarted.setValue(false);
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
}