package com.andraskesik.covid.registration_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andraskesik.covid.R;
import com.andraskesik.covid.SignUpActivity;
import com.andraskesik.covid.model.User;

public class PersonalInfoFragment extends Fragment implements View.OnClickListener {
    private User mUser;
    private TextInputLayout mName;
    private TextInputLayout mAge;
    private TextInputLayout mCity;
    private TextInputLayout mCountry;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUser = getArguments().getParcelable(SignUpActivity.USER);
        View view = inflater.inflate(R.layout.fragment_register_personalinfo, container, false);
        getActivity().setTitle("Personal Info");

        //Views
        mName =  (TextInputLayout) view.findViewById(R.id.field_register_name);
        mAge = (TextInputLayout) view.findViewById(R.id.field_register_age);
        mCity = (TextInputLayout) view.findViewById(R.id.field_register_city);
        mCountry = (TextInputLayout) view.findViewById(R.id.field_register_country);

        //Next Button
        view.findViewById(R.id.button_next_register_personalinfo).setOnClickListener(this);

        return view;
    }

    private void callIntroductionFragment() {
        IntroductionFragment introductionFragment = new IntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SignUpActivity.USER,mUser);
        introductionFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.placeHolder_signup, introductionFragment)
                .commit();
    }

    private void saveDatatoUser() {
        mUser.setName(mName.getEditText().getText().toString());
        mUser.setAge(mAge.getEditText().getText().toString());
        mUser.setCity(mCity.getEditText().getText().toString());
        mUser.setCountry(mCountry.getEditText().getText().toString());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_next_register_personalinfo:
                saveDatatoUser();
                callIntroductionFragment();
                return;
        }
    }
}
