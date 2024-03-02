package com.example.vocab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class VocabFragment  extends DialogFragment {


    Vocab vocab;
    VocabFragment(Vocab vocab){
        this.vocab = vocab;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vocab_layout, container, false);
        TextView defTextView = rootView.findViewById(R.id.defTextView);
        defTextView.setText(vocab.def);
        TextView ipaTextView = rootView.findViewById(R.id.ipaTextView);
        ipaTextView.setText(vocab.ipa);
        return rootView;
    }
}


