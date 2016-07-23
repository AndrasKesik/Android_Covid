package com.andraskesik.covid.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.andraskesik.covid.R;

/**
 * Created by andra on 2016-07-21.
 */
public class BaseActivity extends AppCompatActivity {

    public static final String USER = "USER";
    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}
