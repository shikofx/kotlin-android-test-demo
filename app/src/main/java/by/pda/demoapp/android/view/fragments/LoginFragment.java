package by.pda.demoapp.android.view.fragments;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.databinding.DataBindingUtil;

import java.util.Objects;

import by.pda.demoapp.android.R;
import by.pda.demoapp.android.databinding.FragmentLoginBinding;
import by.pda.demoapp.android.utils.Constants;
import by.pda.demoapp.android.utils.base.BaseFragment;
import by.pda.demoapp.android.view.activities.MainActivity;


public class LoginFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "none";
    private FragmentLoginBinding binding;

    public static LoginFragment newInstance(String param1, String param2, int param3) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(Constants.ARG_PARAM1, param1);
        args.putString(Constants.ARG_PARAM2, param2);
        args.putInt(Constants.ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAct = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(Constants.ARG_PARAM1, "");
            mParam2 = getArguments().getString(Constants.ARG_PARAM2, "");
            mParam3 = getArguments().getInt(Constants.ARG_PARAM3, -1);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        init();
        setListeners();
        return binding.getRoot();
    }

    private void init() {
        binding.username1TV.setPaintFlags(binding.username1TV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.username2TV.setPaintFlags(binding.username2TV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (Constants.is_biometric) {
            binding.bioMetricIB.setVisibility(View.VISIBLE);
        } else {
            binding.bioMetricIB.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        binding.loginBtn.setOnClickListener(this);
        binding.username1TV.setOnClickListener(this);
        binding.bioMetricIB.setOnClickListener(this);
        binding.username2TV.setOnClickListener(this);
        binding.username3TV.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.equals(binding.loginBtn)) {
            loginWithEmail();
        } else if (view.equals(binding.bioMetricIB)) {
            loginWithBiometrics();
        } else if (view.equals(binding.username1TV)) {
            fillForm(binding.username1TV, binding.password1TV);
        } else if (view.equals(binding.username3TV)) {
            fillForm(binding.username3TV, binding.password1TV);
        } else if (view.equals(binding.username2TV)) {
            fillForm(binding.username2TV, binding.password1TV);
        }
    }

    private void fillForm(TextView username, TextView password) {
        binding.nameET.setText(username.getText().toString().trim());
        binding.passwordET.setText(password.getText().toString().trim());
        binding.nameRL.setBackground(requireActivity().getDrawable(R.drawable.edit_bg_grey));
        binding.passwordRL.setBackground(requireActivity().getDrawable(R.drawable.edit_bg_grey));
        binding.nameErrorTV.setVisibility(View.INVISIBLE);
        binding.usernameErrorIV.setVisibility(View.INVISIBLE);
        binding.passwordErrorTV.setVisibility(View.INVISIBLE);
        binding.passwordErrorIV.setVisibility(View.INVISIBLE);
    }

    private void loginWithBiometrics() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Login using you biometric credential")
                .setDescription("Unlock using fingerprint")
                .setAllowedAuthenticators(BIOMETRIC_WEAK)
                .setConfirmationRequired(true)
                .setNegativeButtonText("Nothing")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(this, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e(TAG, "onAuthenticationError: ");
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Log.e(TAG, "onAuthenticationSucceeded: ");

                ST.isLogin = true;
                ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_CLEAR_BACK_STACK);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e(TAG, "onAuthenticationFailed: ");
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }

    @SuppressLint("NewApi")
    private void loginWithEmail() {
        String username = binding.nameET.getText().toString().trim();
        String password = binding.passwordET.getText().toString().trim();

        if (username.isEmpty()) {
            binding.nameRL.setBackground(getActivity().getDrawable(R.drawable.edit_bg_red));
            binding.nameErrorTV.setVisibility(View.VISIBLE);
            binding.usernameErrorIV.setVisibility(View.VISIBLE);
            binding.passwordRL.setBackground(getActivity().getDrawable(R.drawable.edit_bg_grey));
            binding.passwordErrorTV.setVisibility(View.INVISIBLE);
            binding.passwordErrorIV.setVisibility(View.INVISIBLE);
        } else if (password.isEmpty()) {
            binding.nameRL.setBackground(getActivity().getDrawable(R.drawable.edit_bg_grey));
            binding.nameErrorTV.setVisibility(View.INVISIBLE);
            binding.usernameErrorIV.setVisibility(View.INVISIBLE);
            binding.passwordRL.setBackground(getActivity().getDrawable(R.drawable.edit_bg_red));
            binding.passwordErrorTV.setVisibility(View.VISIBLE);
            binding.passwordErrorIV.setVisibility(View.VISIBLE);
            binding.passwordErrorTV.setText(getString(R.string.enter_password));
        } else if (username.contains("alice@example.com")) {
            binding.passwordErrorTV.setText(getString(R.string.soory_this_user_has_been_locked_out));
            binding.passwordErrorTV.setVisibility(View.VISIBLE);
        } else {

            ST.isLogin = true;
            ST.setHasVisualChanges(username.equals("visual@example.com"));

            if (mParam1.equals(ST.CHECKOUT)) {
                Bundle bundle = ST.getBundle(MainActivity.FRAGMENT_CHECKOUT_INFO, 1);
                ST.startActivityWithDataBundle(mAct, MainActivity.class, bundle, ST.START_ACTIVITY_WITH_FINISH);
            } else {
                ST.startActivity(mAct, MainActivity.class, ST.START_ACTIVITY_WITH_CLEAR_BACK_STACK);
            }
        }
    }
}