package com.p2.mirrormouth.ui.cabin;

import com.p2.mirrormouth.classes.GameItem;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CabinViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<GameItem>> wordItemList;

    public CabinViewModel() {
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