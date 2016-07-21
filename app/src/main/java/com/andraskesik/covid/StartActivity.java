package com.andraskesik.covid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends BaseActivity implements View.OnClickListener  {

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle("");

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


}
