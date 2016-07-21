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

import org.w3c.dom.Text;

/**
 * Created by andra on 2016-07-21.
 */
public class IntroductionFragment extends Fragment implements View.OnClickListener{
    private User mUser;
    private TextInputLayout mIntroduction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUser = getArguments().getParcelable(SignUpActivity.USER);
        View view = inflater.inflate(R.layout.fragment_register_introduction, container, false);
        getActivity().setTitle("Introdcution");

        mIntroduction = (TextInputLayout) view.findViewById(R.id.field_register_introduction);

        view.findViewById(R.id.button_next_register_introduction).setOnClickListener(this);

        return view;
    }

    private void callIntroductionFragment() {
        LoginDataFragment loginDataFragment = new LoginDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SignUpActivity.USER,mUser);
        loginDataFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.placeHolder_signup, loginDataFragment)
                .commit();
    }

    private void saveDatatoUser() {
        mUser.setIntroduction(mIntroduction.getEditText().getText().toString());

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
