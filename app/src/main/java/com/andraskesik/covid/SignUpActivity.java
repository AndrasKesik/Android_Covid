package com.andraskesik.covid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.andraskesik.covid.main_fragments.GalleryFragment;
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
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private FragmentManager mFragmentManager;
    private User mUser;
    private Fragment mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUser = new User();

        //Views
        if (savedInstanceState != null ){
            mUser = savedInstanceState.getParcelable(USER);
            Log.d(TAG, "User restored: " + mUser.toString());
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.placeHolder_signup, mContent)
//                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.placeHolder_signup, new PersonalInfoFragment())
                    .commit();
        }


        //Firrebase Database init
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Firebase auth init
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        outState.putParcelable(USER, mUser);
        super.onSaveInstanceState(outState);
    }


    public void createUser(final User user, final String email, String password) {
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
                        onAuthSuccess(task.getResult().getUser());
                        hideProgressDialog();
                        // ...
                    }
                });


    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        mDatabase.child("users").child(user.getUid()).setValue(mUser);

        Log.d(TAG, "User created: " + mUser.toString());
        // Go to MainActivity
        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    public void callNextFragment(){


    }

    public Fragment getmContent() {
        return mContent;
    }

    public void setmContent(Fragment mContent) {
        this.mContent = mContent;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }
}
