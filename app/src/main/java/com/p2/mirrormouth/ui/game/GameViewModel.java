package com.p2.mirrormouth.ui.game;

import com.p2.mirrormouth.classes.GameItem;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GameViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<GameItem>> wordItemList;

    public GameViewModel() {
        wordItemList = new MutableLiveData<>();
        wordItemList.setValue(new ArrayList<GameItem>());
    }

    public void setArray(ArrayList<GameItem> array){
        wordItemList.setValue(new ArrayList<GameItem>(array));
    }
    public MutableLiveData<ArrayList<GameItem>> getArray() {
        return wordItemList;
    }
    public ArrayList<GameItem> getArrayList(){
        return wordItemList.getValue();
    }

}