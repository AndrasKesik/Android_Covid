package com.andraskesik.covid.activities;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.andraskesik.covid.R;
import com.andraskesik.covid.VideoViewHolder;
import com.andraskesik.covid.model.Video;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CategoryActivity extends BaseActivity {

    public static final String CATEGORY = "CATEGORY";
    private static final String TAG = CategoryActivity.class.getSimpleName();
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private FirebaseRecyclerAdapter<Video, VideoViewHolder> mAdapter;
    private String mCategory;
    private String mUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mCategory = getIntent().getStringExtra(CATEGORY);
        mUserName = getIntent().getStringExtra(MainActivity.USERNAME);


        if (savedInstanceState != null) {
            mCategory = savedInstanceState.getString(CATEGORY);
        }

        setTitle(mCategory + " Videos");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = mDatabase.child("videos");

        Query categoryQuery = mDatabase.child("videos").orderByChild("category").equalTo(mCategory);


        RecyclerView recycler = (RecyclerView) findViewById(R.id.recyclerView_category);
//        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new FirebaseRecyclerAdapter<Video, VideoViewHolder>(Video.class, R.layout.listitem_video, VideoViewHolder.class, categoryQuery) {
            @Override
            public void populateViewHolder(VideoViewHolder videoViewHolder, final Video video, int position) {
                Log.d(TAG, "populateviewHolder___________________");
                videoViewHolder.setUploaderName("Uploaded by: " + video.getUserName());
                videoViewHolder.setDescription(video.getDescription());
                videoViewHolder.setListener(new VideoViewHolder.IViedoViewHolderClicks() {
                    @Override
                    public void openVideoPage() {
                        Intent openVideoPageIntent = new Intent(CategoryActivity.this, VideoActivity.class);
                        openVideoPageIntent.putExtra(VideoActivity.VIDEO,video.getDownloadLink());
                        openVideoPageIntent.putExtra(MainActivity.USERNAME, mUserName);
                        startActivity(openVideoPageIntent);
                    }
                });

            }
        };
        recycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");

        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onREstart");

        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");


        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(CATEGORY, mCategory);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

        mAdapter.cleanup();
    }
}
