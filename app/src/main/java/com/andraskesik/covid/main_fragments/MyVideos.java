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

import com.andraskesik.covid.R;
import com.andraskesik.covid.VideoViewHolder;
import com.andraskesik.covid.activities.MainActivity;
import com.andraskesik.covid.model.Video;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by andra on 2016-07-22.
 */
public class MyVideos extends Fragment {

    private static final String TAG = MyVideos.class.getSimpleName();
    private DatabaseReference mDatabase;
    private MainActivity mAct;
    private DatabaseReference mRef;
    private ArrayList<Video> mVideoList;
    private RecyclerView.Adapter<VideoViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_myvideos, container, false);
        mAct = (MainActivity) getActivity();
        mAct.setTitle("Your videos");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.child("videos");

        String myUserId = mAct.getmFirebaseUser().getUid();
        Query myVideosQuery = mDatabase.child("videos").orderByChild("userId").equalTo(myUserId);
        myVideosQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mVideoList = new ArrayList<>();
                for(DataSnapshot shot : dataSnapshot.getChildren()) {
                    Video video = shot.getValue(Video.class);
                    mVideoList.add(video);
                    Log.d(TAG, video.toString());
                }
                makeRecyclerView(view);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        return view;
    }

    private void makeRecyclerView(View view) {
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recyclerView_myVideos);
//        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(mAct));
        mAdapter = new RecyclerView.Adapter<VideoViewHolder>() {
            @Override
            public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.listitem_video, parent, false);

                return new VideoViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(VideoViewHolder holder, int position) {
                Video video = mVideoList.get(position);
                Log.d(TAG, "populateviewHolder___________________");
                holder.setDescription(video.getDescription());
                holder.setUploaderName(video.getUserName());
//                holder.setDownloadLink(video.getDownloadLink());
            }

            @Override
            public int getItemCount() {
                return mVideoList.size();
            }
        };
        recycler.setAdapter(mAdapter);
    }

}
