package com.andraskesik.covid.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andraskesik.covid.R;
import com.andraskesik.covid.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    public static final String NAME = "Name";
    public static final String AGE = "Age";
    public static final String CITY = "City";
    public static final String COUNTRY = "Country";
    public static final String INTRODUCTION = "Introduction";
    public static final String PASSWORD = "Password";
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase ;
    private User mUser;
    private TextView mProfileName;
    private TextView mProfilePremium;
    private TextView mName;
    private TextView mAge;
    private TextView mCity;
    private TextView mCountry;
    private TextView mIntroduction;
    private TextView mProfileEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Views
        mProfileName = (TextView) findViewById(R.id.profile_name);
        mProfilePremium = (TextView) findViewById(R.id.profile_premium);
        mProfileEmail = (TextView) findViewById(R.id.profile_email);
        mName = (TextView) findViewById(R.id.settings_name);
        mAge = (TextView) findViewById(R.id.settings_age);
        mCity = (TextView) findViewById(R.id.settings_city);
        mCountry = (TextView) findViewById(R.id.settings_country);
        mIntroduction = (TextView) findViewById(R.id.settings_introduction);

        //Buttons
        findViewById(R.id.settings_changePassword).setOnClickListener(this);
        findViewById(R.id.settings_premium).setOnClickListener(this);
        findViewById(R.id.settings_change_name).setOnClickListener(this);
        findViewById(R.id.settings_change_age).setOnClickListener(this);
        findViewById(R.id.settings_change_city).setOnClickListener(this);
        findViewById(R.id.settings_change_country).setOnClickListener(this);
        findViewById(R.id.settings_change_introduction).setOnClickListener(this);

        getUserFromDb();





    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_changePassword:
                showChangeDialog(PASSWORD);
                break;
            case R.id.settings_premium:
                changePremium();
                break;
            case R.id.settings_change_name:
                showChangeDialog(NAME);
                break;
            case R.id.settings_change_age:
                showChangeDialog(AGE);
                break;
            case R.id.settings_change_city:
                showChangeDialog(CITY);
                break;
            case R.id.settings_change_country:
                showChangeDialog(COUNTRY);
                break;
            case R.id.settings_change_introduction:
                showChangeDialog(INTRODUCTION);
                break;


        }
    }

    private void changePremium() {
        if (mUser.isPremium()) {
            mUser.setPremium(false);
            mProfilePremium.setText("Normal user");
            Toast.makeText(SettingsActivity.this, "Your are now Normal User", Toast.LENGTH_SHORT).show();
        } else {
            mUser.setPremium(true);
            mProfilePremium.setText("Premium user");
            Toast.makeText(SettingsActivity.this, "Your are now Premium User", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChangeDialog(final String data) {
        final EditText input = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Change your "+data)
                .setView(input)
//                .setCancelable(false)
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        if
//                    }
//                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String inputString = input.getText().toString();
                        switch (data) {
                            case PASSWORD:
                                if(inputString.length()<6){
                                    Toast.makeText(SettingsActivity.this, "Password must be at least 6 character long", Toast.LENGTH_SHORT).show();
                                    break;
                                  }
                                mFirebaseUser.updatePassword(inputString);
                                break;
                            case NAME:
                                mUser.setName(inputString);
                                break;
                            case AGE:
                                mUser.setAge(inputString);
                                break;
                            case CITY:
                                mUser.setCity(inputString);
                                break;
                            case COUNTRY:
                                mUser.setCountry(inputString);
                                break;
                            case INTRODUCTION:
                                mUser.setIntroduction(inputString);
                                break;


                        }
                        setViews();
                    }
                });
              

        builder.show();
    }

    private void getUserFromDb() {
        mDatabase.child("users").child(mFirebaseUser.getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mUser = dataSnapshot.getValue(User.class);

                                if (mUser == null) {
                                    Log.e(TAG, "User is unexpectedly null");
                                    Toast.makeText(SettingsActivity.this,
                                            "Error: SettingsActivity could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    setViews();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
    }

    private void setViews() {
        if (mUser.isPremium()) mProfilePremium.setText("Premium user");
        else mProfilePremium.setText("Normal user");
        mProfileName.setText(mUser.getName());
        mProfileEmail.setText(mFirebaseUser.getEmail());
        mName.setText(mUser.getName());
        mAge.setText(mUser.getAge());
        mCity.setText(mUser.getCity());
        mCountry.setText(mUser.getCountry());
        mIntroduction.setText(mUser.getIntroduction());
    }


    private void refreshUserData() {
        mDatabase.child("users").child(mFirebaseUser.getUid()).setValue(mUser);
    }

    private void deleteUser() {
//        if(mUser.isPremium()) {
                // TODO This statement removes all videos from the db, change it to remove only the user's video
//            Query videosQuery = mDatabase.child("videos").orderByChild("userId").equalTo(mFirebaseUser.getUid());
//            Toast.makeText(SettingsActivity.this, "Premium user deletion", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "Removed videos: " + videosQuery.toString());
//            videosQuery.getRef();
//        }
            // TODO FIrebaseUser.delete() does not remove the user
//        mFirebaseUser.delete();
//        mDatabase.child("users").child(mFirebaseUser.getUid()).removeValue();
//        Log.d(TAG, "Removed user: " + mUser.toString());
        Toast.makeText(SettingsActivity.this, "User can't be deleted yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_user:
                deleteUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if(mFirebaseUser != null && mUser!=null) {
            refreshUserData();
        }
    }
}
