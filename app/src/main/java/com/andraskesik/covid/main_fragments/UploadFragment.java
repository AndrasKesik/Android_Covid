package com.andraskesik.covid.main_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.andraskesik.covid.MainActivity;
import com.andraskesik.covid.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private static final String VIDEO_DIRECTORY_NAME = "Covid";
    private static final String UPLOADSTATE = "upload_state";
    private static final String CURRENT_FILE_PATH = "current_file_path";

    private MainActivity mAct;

    private StorageReference mStorageRef;
    public Uri mDownloadUrl = null;

    private Uri mCurrentUri;
    private Button mUploadButton;
    private boolean isUploadVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_upload, container, false);
        mAct = ((MainActivity) getActivity());
        mAct.setTitle("Upload your video");

        mUploadButton = (Button) view.findViewById(R.id.button_upload);
        hideUpload();

        if (savedInstanceState != null) {
            isUploadVisible = savedInstanceState.getBoolean(UPLOADSTATE);
            mCurrentUri = savedInstanceState.getParcelable(CURRENT_FILE_PATH);
            if (isUploadVisible) mUploadButton.setVisibility(View.VISIBLE);
            else mUploadButton.setVisibility(View.GONE);
        }


        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://covid-c897b.appspot.com/");


        mUploadButton.setOnClickListener(this);
        view.findViewById(R.id.button_makeVideo).setOnClickListener(this);
        view.findViewById(R.id.button_chooseFromGallery).setOnClickListener(this);


        return view;
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

    private void makeVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(mAct.getPackageManager()) != null) {
            takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 500000L);
            takeVideoIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 0);
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }


    private void chooseFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickIntent, REQUEST_PICK_FROM_GALLERY);
    }

    private void showFromGallery() {
        Intent pickIntent = new Intent(Intent.ACTION_VIEW, mCurrentUri);
        startActivityForResult(pickIntent, REQUEST_PICK_FROM_GALLERY);
    }


    private void upLoadFile(Uri file) {
        mAct.showProgressDialog();
        if (file == null) Log.d(TAG, "FILE IS NULL");
        StorageReference riversRef = mStorageRef.child("videos/" + file.getLastPathSegment());
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
                hideUpload();
                mAct.hideProgressDialog();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_makeVideo:
                makeVideo();
                break;
            case R.id.button_chooseFromGallery:
                chooseFromGallery();
                Toast.makeText(mAct, "choosefromGallery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_upload:
                showFromGallery();
//                upLoadFile(mCurrentUri);
//                Toast.makeText(mAct, "Uploaded", Toast.LENGTH_SHORT).show();
                break;

        }
    }



    private void showUpload() {
        mUploadButton.setVisibility(View.VISIBLE);
        isUploadVisible = true;
    }

    private void hideUpload() {
        mUploadButton.setVisibility(View.GONE);
        isUploadVisible = false;
    }


}
