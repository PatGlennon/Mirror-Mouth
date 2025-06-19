package com.p2.mirrormouth.ui.rules;

import com.p2.mirrormouth.classes.GameItem;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RulesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<ArrayList<GameItem>> wordItemList;


    public RulesViewModel() {
        wordItemList = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        mText.setValue("Test");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void addItemToArray(ArrayList<GameItem> items){
        wordItemList.setValue(items);
    }

    public ArrayList<GameItem> returnArray(){
        return wordItemList.getValue();
    }
}