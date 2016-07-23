package com.andraskesik.covid.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.andraskesik.covid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends BaseActivity implements View.OnClickListener  {

    private static final String TAG = StartActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle("Welcome to Covid");



        findViewById(R.id.button_start_signin).setOnClickListener(this);
        findViewById(R.id.button_start_signup).setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_signin:
                startActivity(new Intent(this, SignInActivity.class));
                return;
            case R.id.button_start_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                return;
        }
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Log.d(TAG, "setStatusBarTranslucent:current_version_is_below_API19");
            return;
        }
        if (makeTranslucent) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
