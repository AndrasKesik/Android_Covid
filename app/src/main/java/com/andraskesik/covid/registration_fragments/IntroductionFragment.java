package com.andraskesik.covid.registration_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andraskesik.covid.R;
import com.andraskesik.covid.activities.SignUpActivity;
import com.andraskesik.covid.model.User;

/**
 * Created by andra on 2016-07-21.
 */
public class IntroductionFragment extends Fragment implements View.OnClickListener{
    private SignUpActivity mActivity;
    private User mUser;
    private TextInputLayout mIntroduction;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActivity = (SignUpActivity) getActivity();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_introduction, container, false);
        mActivity.setTitle("Introdcution");

        mIntroduction = (TextInputLayout) view.findViewById(R.id.field_register_introduction);

        view.findViewById(R.id.button_next_register_introduction).setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        if (mActivity.getmUser().getIntroduction() != null) mIntroduction.getEditText().setText(mActivity.getmUser().getIntroduction());
        super.onResume();
    }

    @Override
    public void onPause() {
        saveDatatoUser();
        super.onPause();
    }

    private void callIntroductionFragment() {
//        mActivity.setmContent(new LoginDataFragment());
        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.placeHolder_signup, new LoginDataFragment())
                .commit();
    }

    private void saveDatatoUser() {
        mActivity.getmUser().setIntroduction(mIntroduction.getEditText().getText().toString());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_next_register_introduction:
                saveDatatoUser();
                callIntroductionFragment();
        }
    }
}
