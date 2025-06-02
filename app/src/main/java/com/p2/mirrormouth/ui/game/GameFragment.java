package com.p2.mirrormouth.ui.game;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.p2.mirrormouth.databinding.FragmentGameBinding;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    private int RECORDER_SAMPLE_RATE = 44100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel =
                new ViewModelProvider(this).get(GameViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textGame;

        textView.setText("Chicken");

        final ImageButton record = binding.recordButton;

        MediaRecorder recorder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recorder = new MediaRecorder(requireContext());
        } else{
            recorder = new MediaRecorder();
        }
        
        try{
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setAudioSamplingRate(RECORDER_SAMPLE_RATE);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setOutputFile("test");
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        MediaRecorder finalRecorder = recorder;
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    finalRecorder.prepare();
                    finalRecorder.start();
                }catch (Exception e){
                    Toast.makeText(getContext(),"Failure!",Toast.LENGTH_LONG).show();
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}