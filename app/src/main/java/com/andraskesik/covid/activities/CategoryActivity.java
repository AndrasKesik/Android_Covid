package com.andraskesik.covid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.andraskesik.covid.R;
import com.andraskesik.covid.VideoViewHolder;
import com.andraskesik.covid.model.Video;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class CategoryActivity extends AppCompatActivity {

    public static final String CATEGORY = "CATEGORY";
    private static final String TAG = CategoryActivity.class.getSimpleName();
    public static final String USERNAME = "USERNAME";
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<Video, VideoViewHolder> mAdapter;
    private String mCategory;
    private String mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mCategory = getIntent().getStringExtra(CATEGORY);
        mUserName = getIntent().getStringExtra(USERNAME);


        setTitle(mCategory + " Videos");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.child("videos");

        Query categoryQuery = mDatabase.child("videos").orderByChild("category").equalTo(mCategory);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView_category);
//        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Video, VideoViewHolder>(Video.class, R.layout.listitem_video, VideoViewHolder.class, categoryQuery) {
            @Override
            public void populateViewHolder(VideoViewHolder videoViewHolder, Video video, int position) {
                Log.d(TAG, "populateviewHolder___________________");
                videoViewHolder.setUserName(mUserName);
                videoViewHolder.setDescription(video.getDescription());
                videoViewHolder.setDownloadLink(video.getDownloadLink());
            }
        };
        recycler.setAdapter(mAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
