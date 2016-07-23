package com.andraskesik.covid.main_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andraskesik.covid.R;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by andra on 2016-07-22.
 */
public class VideoBoxFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_videobox, container, false);
        getActivity().setTitle("Watch Later");
        return view;
    }

    private ArrayList<String> extractFilesFromLocalStorage(final String fileExtension) {
        ArrayList<String> items = new ArrayList<>();
        File[] files = getContext().getFilesDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.getAbsolutePath().contains(fileExtension)) return true;
                else return false;
            }
        });
        for (File f : files) {
            items.add(f.getName());
        }
        return items;
    }
}
