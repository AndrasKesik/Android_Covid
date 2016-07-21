package com.andraskesik.covid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andraskesik.covid.model.User;
import com.andraskesik.covid.registration_fragments.PersonalInfoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends BaseActivity{

    private static final String TAG = SignUpActivity.class.getSimpleName();
    public static final String USER = "USER";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FragmentManager mFragmentManager;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUser = new User();

        PersonalInfoFragment personalInfoFragment= new PersonalInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(USER,mUser);
        personalInfoFragment.setArguments(bundle);
        //Views
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .replace(R.id.placeHolder_signup, personalInfoFragment)
                .commit();



        //Firrebase Database init
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        //Firebase auth init
        mAuth = FirebaseAuth.getInstance();
        //Firebase auth_state_listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };




    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



    public void createUser(String email, String password) {
        showProgressDialog();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                        // ...
                    }
                });

    }



}
