package com.andraskesik.covid.registration_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andraskesik.covid.R;
import com.andraskesik.covid.activities.SignUpActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by andra on 2016-07-21.
 */
public class LoginDataFragment extends Fragment implements View.OnClickListener {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final String TAG = LoginDataFragment.class.getSimpleName();
    private SignUpActivity mActivity;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (SignUpActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_register_logindata, container, false);
        mActivity.setTitle("Register & Login");

        view.findViewById(R.id.button_register_logindata).setOnClickListener(this);

        mEmail =  (TextInputLayout) view.findViewById(R.id.field_register_email);
        mPassword = (TextInputLayout) view.findViewById(R.id.field_register_password);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register_logindata:
                if (!validateEmail() || !validatePassword()){return;}
                Log.d(TAG, mActivity.getmUser().toString());
//                Toast.makeText(getActivity(), mActivity.getmUser().toString(), Toast.LENGTH_SHORT).show();
                mActivity.getmUser().setPremium(false);
                ((SignUpActivity) getActivity()).createUser(
                                                            mEmail.getEditText().getText().toString(),
                                                            mPassword.getEditText().getText().toString());
                break;

        }
    }

    private boolean validatePassword() {
        if (mPassword.getEditText().getText().toString().length() < 6){
            Toast.makeText(mActivity, "Password should be at least 6 char long", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private boolean validateEmail() {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(mEmail.getEditText().getText().toString());
        if (matcher.find()) return true;
        else{
            Toast.makeText(mActivity, "Email should be a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
