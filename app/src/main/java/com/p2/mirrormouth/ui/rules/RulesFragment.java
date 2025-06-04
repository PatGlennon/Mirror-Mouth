package com.p2.mirrormouth.ui.rules;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p2.mirrormouth.MainActivityViewModel;
import com.p2.mirrormouth.classes.WordRowItem;
import com.p2.mirrormouth.databinding.FragmentRulesBinding;

import java.util.ArrayList;

public class RulesFragment extends Fragment {

    private FragmentRulesBinding binding;
    private ArrayList<WordRowItem> wordItemList = new ArrayList<WordRowItem>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RulesViewModel rulesViewModel =
                new ViewModelProvider(this).get(RulesViewModel.class);
        MainActivityViewModel gameViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        binding = FragmentRulesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WordRowItem currentItem = new WordRowItem(1, 2, 3, 4, 5, 6, "word");

        wordItemList.add(currentItem);

        rulesViewModel.addItemToArray(wordItemList);
//
//        for (WordRowItem item : gameViewModel.getArray().observe(getViewLifecycleOwner(),wordItemList::)){
//            Log.println(Log.ERROR,"Persist to Rules?", gameViewModel.getArray().toString());
//        }


        final TextView textView = binding.textRules;
        rulesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}