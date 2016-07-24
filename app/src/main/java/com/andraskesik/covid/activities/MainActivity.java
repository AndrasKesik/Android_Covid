package com.andraskesik.covid.activities;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.andraskesik.covid.R;
import com.andraskesik.covid.main_fragments.GalleryFragment;
import com.andraskesik.covid.main_fragments.ShareFragment;
import com.andraskesik.covid.main_fragments.UploadFragment;
import com.andraskesik.covid.main_fragments.MyVideos;
import com.andraskesik.covid.main_fragments.VideoBoxFragment;
import com.andraskesik.covid.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String RECENT_FRAGMENT = "RECENT_FRAGMENT";
    public static final String USERNAME = "USERNAME";
    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabase;

    private User mUser;

    //UI elements
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView drawerName;
    private TextView drawerEmail;
    private Fragment mContent;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setup UI
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupDrawer();
        setStatusBarTranslucent(true);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Check if user is logged in
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, StartActivity.class));
            finish();
            return;
        }

        getUserFromDb();

        //Get the content for the UI
        if (savedInstanceState != null ){
            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
            mFragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_main, mContent);

        } else {
            mContent = new GalleryFragment();
            mFragmentTransaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_main, mContent);

        }


        // Firebase Auth_state_listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, StartActivity.class));
                    finish();
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
    public void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_mainmenu_signout:
                FirebaseAuth.getInstance().signOut();
                return true;
            case R.id.item_mainmenu_about:
                Toast.makeText(MainActivity.this, "Made by Andras Kesik", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.user_settings:
                startActivity(new Intent(this, SettingsActivity.class).putExtra(USER, mUser));
//                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_gallery:
                mContent = new GalleryFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, mContent)
                        .commit();
                break;
            case R.id.nav_videobox:
                mContent = new MyVideos();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, mContent)
                        .commit();
                break;
            case R.id.nav_watchlater:
                mContent = new VideoBoxFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, mContent)
                        .commit();
                break;
            case R.id.nav_upload:
                mContent = new UploadFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, mContent)
                        .commit();

                break;
            case R.id.nav_share:
                mContent = new ShareFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_main, mContent)
                        .commit();
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserFromDb() {
        showProgressDialog();
        mDatabase.child("users").child(mFirebaseUser.getUid())
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                mUser = dataSnapshot.getValue(User.class);


                                if (mUser == null) {
                                    Log.e(TAG, "User is unexpectedly null");
                                    Toast.makeText(MainActivity.this,
                                            "Error: Mainactivity could not fetch user.",
                                            Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(MainActivity.this, StartActivity.class));
//                                    finish();
                                } else {
                                    drawerName.setText(mUser.getName());
                                    drawerEmail.setText(mFirebaseUser.getEmail());
                                    mFragmentTransaction.commit();
                                    hideProgressDialog();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                            }
                        });
    }

    private void setupDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);
        drawerName = (TextView) headerLayout.findViewById(R.id.nav_user_name);
        drawerEmail = (TextView) headerLayout.findViewById(R.id.nav_user_email);
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

    public FirebaseUser getmFirebaseUser() {
        return mFirebaseUser;
    }

    public User getmUser() {
        return mUser;
    }
}
