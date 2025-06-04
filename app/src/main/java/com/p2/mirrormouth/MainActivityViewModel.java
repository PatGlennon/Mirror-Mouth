package com.p2.mirrormouth;

import com.p2.mirrormouth.classes.WordRowItem;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel {

    private final MutableLiveData<ArrayList<WordRowItem>> wordItemList;

    public MainActivityViewModel() {
        wordItemList = new MutableLiveData<>();
        wordItemList.setValue(new ArrayList<WordRowItem>());
    }

    public void setArray(ArrayList<WordRowItem> array) {
        wordItemList.setValue(array);
    }

    public LiveData<ArrayList<WordRowItem>> getArray() {
        return wordItemList;
    }
    public ArrayList<WordRowItem> getArrayList() {
        return wordItemList.getValue();
    }
}