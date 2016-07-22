package com.andraskesik.covid.main_fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andra on 2016-07-22.
 */
public class UploadFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = UploadFragment.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private StorageReference mStorageRef;
    public Uri mDownloadUrl = null;

    private ImageView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_upload, container, false);
        getActivity().setTitle("Upload your video");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        mStorageRef = storage.getReferenceFromUrl("gs://covid-c897b.appspot.com/");


        view.findViewById(R.id.button_makePicture).setOnClickListener(this);
        view.findViewById(R.id.button_makeVideo).setOnClickListener(this);
        view.findViewById(R.id.button_chooseFromGallery).setOnClickListener(this);


        mImageView = (ImageView) view.findViewById(R.id.imageView_result);

        return view;
    }

    private ArrayList<String> extractFilesFromLocalStorage(final String fileExtension) {
        ArrayList<String> items = new ArrayList<>();
        File[] files = getContext().getFilesDir().listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getAbsolutePath().contains(fileExtension)) return true;
                else return false;
            }
        });
        for (File f : files){
            items.add(f.getName());
        }
        return items;
    }

    private void makePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    private void makeVideo() {
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void upLoadFile(String fileName){
        ((MainActivity) getActivity()).showProgressDialog();
        Uri file = Uri.fromFile(new File(fileName));
        StorageReference riversRef = mStorageRef.child("files/"+file.getLastPathSegment());
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
                ((MainActivity) getActivity()).hideProgressDialog();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_makePicture:
                makePicture();
                break;
            case R.id.button_makeVideo:
                makeVideo();
                break;
            case R.id.button_chooseFromGallery:
                Toast.makeText(getActivity(), "choosefromGallery", Toast.LENGTH_SHORT).show();
                break;


        }
    }



//    private void saveAsset(String assetName) throws IOException {
//        File fileDirectory = getContext().getFilesDir();
//        File fileToWrite = new File(fileDirectory, assetName);
//        Log.d(TAG, "fileToWrite: "+fileToWrite.toString());
//        Log.d(TAG, "fileDirectory:"+fileDirectory.toString());
//        AssetManager assetManager = getContext().getAssets();
//        try {
//            InputStream in = assetManager.open(assetName);
//            FileOutputStream out = new FileOutputStream(fileToWrite);
//            copyFile(in, out);
//            in.close();
//            out.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int read;
//        while((read = in.read(buffer)) != -1){
//            out.write(buffer, 0, read);
//        }
//    }




}
