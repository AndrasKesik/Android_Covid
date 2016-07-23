package com.andraskesik.covid.main_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andraskesik.covid.MainActivity;
import com.andraskesik.covid.R;
import com.andraskesik.covid.model.Video;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

/**
 * Created by andra on 2016-07-22.
 */
public class UploadFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = UploadFragment.class.getSimpleName();
    public static final int REQUEST_PICK_FROM_GALLERY = 1;
    public static final int REQUEST_VIDEO_CAPTURE = 2;
    private static final int REQUEST_WATCH_VIDEO = 3;
    private static final String VIDEO_DIRECTORY_NAME = "Covid";
    private static final String UPLOADSTATE = "upload_state";
    private static final String CURRENT_FILE_PATH = "current_file_path";

    private MainActivity mAct;

    private StorageReference mStorageRef;
    public Uri mDownloadUrl = null;

    private Uri mCurrentUri;
    private CardView mUploadButton;
    private boolean isUploadVisible;
    private CardView cardTakeVideo;
    private CardView cardChooseFromGallery;
    private DatabaseReference mDatabase;
    private RelativeLayout mConainerView;
    private LinearLayout mUploadView;
    private TextInputLayout mDescription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_upload, container, false);
        mAct = ((MainActivity) getActivity());
        mAct.setTitle("Upload your video");
        mConainerView = (RelativeLayout) view.findViewById(R.id.container_uploadFragment);
        mUploadView = (LinearLayout) view.findViewById(R.id.linearLayout_upload);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDescription = (TextInputLayout) view.findViewById(R.id.field_description);
        mUploadButton = (CardView) view.findViewById(R.id.button_upload);

        hideUpload();

        if (savedInstanceState != null) {
            isUploadVisible = savedInstanceState.getBoolean(UPLOADSTATE);
            mCurrentUri = savedInstanceState.getParcelable(CURRENT_FILE_PATH);
            if (isUploadVisible) mUploadView.setVisibility(View.VISIBLE);
            else mUploadView.setVisibility(View.GONE);
        }


        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://covid-c897b.appspot.com/");


        mUploadButton.setOnClickListener(this);
        cardTakeVideo = (CardView) view.findViewById(R.id.button_makeVideo);
        cardChooseFromGallery = (CardView) view.findViewById(R.id.button_chooseFromGallery);
        cardTakeVideo.setOnClickListener(this);
        cardChooseFromGallery.setOnClickListener(this);
        view.findViewById(R.id.button_watch_your_video).setOnClickListener(this);



        return view;
    }

    @Override
    public void onResume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cardTakeVideo.setElevation(5f);
            cardChooseFromGallery.setElevation(5f);
        }
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(UPLOADSTATE, isUploadVisible);
        outState.putParcelable(CURRENT_FILE_PATH, mCurrentUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == Activity.RESULT_OK) {
            mAct.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            showUpload();
            mCurrentUri = data.getData();
            Log.w(TAG, "________________VIDEO TAKEN____________");
            Log.d(TAG, mCurrentUri.toString());
        } else  if (requestCode == REQUEST_PICK_FROM_GALLERY  && resultCode == Activity.RESULT_OK) {
            showUpload();
            mCurrentUri = data.getData();
            Log.d(TAG, "____________________________File picked: " + data.getData());
        }

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

    private void takeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(mAct.getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 500000L);
            takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void watchVideo() {
        Intent watchVideoIntent = new Intent(Intent.ACTION_VIEW, mCurrentUri);
        if (watchVideoIntent.resolveActivity(mAct.getPackageManager()) != null) {
            startActivityForResult(watchVideoIntent, REQUEST_WATCH_VIDEO);
        }
    }


    private void chooseFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickIntent, REQUEST_PICK_FROM_GALLERY);
    }


    private void upLoadFile(Uri file) {
        hideKeyboard();
        mAct.showProgressDialog();
        if (file == null) Log.d(TAG, "FILE IS NULL");
        StorageReference riversRef = mStorageRef.child("videos/" + mAct.getmFirebaseUser().getUid() + "/" + file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                mDownloadUrl = taskSnapshot.getDownloadUrl();
                Log.d(TAG, "fileUploadSucces: " + mDownloadUrl);
                writeVideoToDb(makeVideoObject(taskSnapshot));
                hideUpload();
                mAct.hideProgressDialog();
                Snackbar snackbar = Snackbar.make(mConainerView, "File uploaded :)", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        });

    }

    private void writeVideoToDb(Video video) {
        mDatabase.child("videos").push().setValue(video);
    }

    private Video makeVideoObject(UploadTask.TaskSnapshot taskSnapshot){
        Video video = new Video();
        video.setUserId(mAct.getmFirebaseUser().getUid());
        video.setDownloadLink(taskSnapshot.getDownloadUrl().toString());  //nembiztos hogy megy
        video.setDescription(mDescription.getEditText().getText().toString());
        return video;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mAct.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_makeVideo:
                takeVideo();
                break;
            case R.id.button_chooseFromGallery:
                chooseFromGallery();
                Toast.makeText(mAct, "choosefromGallery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_watch_your_video:
                watchVideo();
                break;
            case R.id.button_upload:
                upLoadFile(mCurrentUri);
                break;

        }
    }

    


    private void showUpload() {
        mUploadView.setVisibility(View.VISIBLE);
        isUploadVisible = true;
    }

    private void hideUpload() {
        mUploadView.setVisibility(View.GONE);
        isUploadVisible = false;
    }


}
