package com.p2.mirrormouth.ui.game;

import com.p2.mirrormouth.classes.WordRowItem;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<WordRowItem>> wordItemList;

    public GameViewModel() {
        wordItemList = new MutableLiveData<>();
        wordItemList.setValue(new ArrayList<WordRowItem>());
    }

    public void setArray(ArrayList<WordRowItem> array){
        wordItemList.setValue(new ArrayList<WordRowItem>(array));
    }
    public MutableLiveData<ArrayList<WordRowItem>> getArray() {
        return wordItemList;
    }
    public ArrayList<WordRowItem> getArrayList(){
        return wordItemList.getValue();
    }

}