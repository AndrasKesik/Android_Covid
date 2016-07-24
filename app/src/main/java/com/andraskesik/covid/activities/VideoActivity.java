package com.andraskesik.covid.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andraskesik.covid.CategoryViewHolder;
import com.andraskesik.covid.CommentViewHolder;
import com.andraskesik.covid.R;
import com.andraskesik.covid.VideoViewHolder;
import com.andraskesik.covid.model.Comment;
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

import java.util.List;

public class VideoActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = VideoActivity.class.getSimpleName();
    public static String VIDEO = "VIDEO";
    private TextView mDescription;
    private TextView mUploaderName;
    private String mVideoDownloadLink;

    private EditText mCommentText;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;
    private String mUserName;
    private Video mVideo;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoDownloadLink = getIntent().getStringExtra(VIDEO);
        mUserName = getIntent().getStringExtra(MainActivity.USERNAME);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUploaderName = (TextView) findViewById(R.id.videopage_uploadername);
        mDescription = (TextView) findViewById(R.id.videopage_description);
        mCommentText = (EditText) findViewById(R.id.comment_text);



        findViewById(R.id.videopage_button_watchVideo).setOnClickListener(this);
        findViewById(R.id.videopage_button_saveToVideoBox).setOnClickListener(this);
        findViewById(R.id.comment_button).setOnClickListener(this);
        findViewById(R.id.videopage_toolbar_backbutton).setOnClickListener(this);



        Log.d(TAG, "Downoadlink:" + mVideoDownloadLink);
        Query getVideoQuery = mDatabase.child("videos").orderByChild("downloadLink").equalTo(mVideoDownloadLink);

        getVideoQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot s : dataSnapshot.getChildren()) {
                    mVideo = s.getValue(Video.class);
                }
                mUploaderName.setText(mVideo.getUserName());
                mDescription.setText(mVideo.getDescription());
                Log.d(TAG, mVideo.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

//    private void addComment() {
//        Comment comment = new Comment();
//        comment.setDescription(mCommentText.getText().toString());
//        comment.setUserId(mFirebaseUser.getUid());
//        comment.setUserName(mUserName);
//        Log.d(TAG, "Comment added: " + comment.getDescription());
//        mCommentsQuery.getRef().push().setValue(comment);
//
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.videopage_button_watchVideo:
                Toast.makeText(VideoActivity.this, "Watch video is not implemented yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.videopage_button_saveToVideoBox:
                Toast.makeText(VideoActivity.this, "Save not implemented yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.comment_button:
//                addComment();
                Toast.makeText(VideoActivity.this, "Comments disabled", Toast.LENGTH_SHORT).show();
                break;
            case R.id.videopage_toolbar_backbutton:
                onBackPressed();
                break;
        }
    }


}
