package com.andraskesik.covid.main_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.andraskesik.covid.R;
import com.andraskesik.covid.VideoViewHolder;
import com.andraskesik.covid.model.Video;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by andra on 2016-07-22.
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = GalleryFragment.class.getSimpleName();
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<Video, VideoViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_gallery, container, false);
        getActivity().setTitle("Gallery");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.child("videos");

        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recyclerView_gallery);
//        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FirebaseRecyclerAdapter<Video, VideoViewHolder>(Video.class, android.R.layout.two_line_list_item, VideoViewHolder.class, mRef) {
            @Override
            public void populateViewHolder(VideoViewHolder videoViewHolder, Video video, int position) {
                Log.d(TAG, "populateviewHolder___________________");
                videoViewHolder.setName(video.getDescription());
                videoViewHolder.setText(video.getDownloadLink());
            }
        };
        recycler.setAdapter(mAdapter);


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
