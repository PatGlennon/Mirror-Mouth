package com.p2.mirrormouth.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p2.mirrormouth.MainActivityViewModel;
import com.p2.mirrormouth.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private MainActivityViewModel mainActivityViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //SettingsViewModel settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);



        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RadioGroup numOfWordsGroup = binding.wordsRadioGroup;

        int numOfWords = mainActivityViewModel.getNumOfWords();

        switch (numOfWords){
            case 1:
                binding.radio1.setEnabled(true);
                break;
            case 2:
                binding.radio2.setEnabled(true);
                break;
            case 3:
                binding.radio3.setEnabled(true);
                break;
            case 4:
                binding.radio4.setEnabled(true);
                break;
            case 5:
                binding.radio5.setEnabled(true);
                break;
        }

        numOfWordsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = requireActivity().findViewById(checkedId);

            mainActivityViewModel.setNumOfWords(Integer.parseInt(rb.getText().toString()));
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        //mainActivityViewModel.setNumOfWords();

        binding = null;
    }
}