package com.andraskesik.covid.registration_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andraskesik.covid.R;
import com.andraskesik.covid.activities.SignUpActivity;
import com.andraskesik.covid.model.User;

public class PersonalInfoFragment extends Fragment implements View.OnClickListener {
    private User mUser;
    private SignUpActivity mActivity;
    private TextInputLayout mName;
    private TextInputLayout mAge;
    private TextInputLayout mCity;
    private TextInputLayout mCountry;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = (SignUpActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_personalinfo, container, false);
        mActivity.setTitle("Personal Info");

        //Views
        mName =  (TextInputLayout) view.findViewById(R.id.field_register_name);
        mAge = (TextInputLayout) view.findViewById(R.id.field_register_age);
        mCity = (TextInputLayout) view.findViewById(R.id.field_register_city);
        mCountry = (TextInputLayout) view.findViewById(R.id.field_register_country);


        //Next Button
        view.findViewById(R.id.button_next_register_personalinfo).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        restoreData();
        super.onResume();
    }

    @Override
    public void onPause() {
        saveDatatoUser();
        super.onPause();
    }

    private void restoreData() {
        if (mActivity.getmUser().getName() != null) mName.getEditText().setText(mActivity.getmUser().getName());
        if (mActivity.getmUser().getAge() != null) mAge.getEditText().setText(mActivity.getmUser().getAge());
        if (mActivity.getmUser().getCity() != null) mCity.getEditText().setText(mActivity.getmUser().getCity());
        if (mActivity.getmUser().getCountry() != null) mCountry.getEditText().setText(mActivity.getmUser().getCountry());
    }

    private void callIntroductionFragment() {
//        mActivity.setmContent(new IntroductionFragment());

        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.placeHolder_signup, new IntroductionFragment())
                .commit();
    }

    private void saveDatatoUser() {
        mActivity.getmUser().setName(mName.getEditText().getText().toString());
        mActivity.getmUser().setAge(mAge.getEditText().getText().toString());
        mActivity.getmUser().setCity(mCity.getEditText().getText().toString());
        mActivity.getmUser().setCountry(mCountry.getEditText().getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next_register_personalinfo:
                if(mName.getEditText().getText().toString().length() < 3 ) {
                    Toast.makeText(mActivity, "Name must be at least 3 char long", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveDatatoUser();
                callIntroductionFragment();
                return;
        }
    }


}
