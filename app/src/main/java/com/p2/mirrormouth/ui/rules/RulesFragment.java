package com.p2.mirrormouth.ui.rules;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p2.mirrormouth.databinding.FragmentRulesBinding;

public class RulesFragment extends Fragment {

    private FragmentRulesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RulesViewModel rulesViewModel =
                new ViewModelProvider(this).get(RulesViewModel.class);

        binding = FragmentRulesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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