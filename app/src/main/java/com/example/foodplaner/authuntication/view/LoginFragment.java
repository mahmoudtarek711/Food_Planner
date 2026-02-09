package com.example.foodplaner.authuntication.view;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.foodplaner.R;
import com.example.foodplaner.authuntication.presenter.LoginPresenter;
import com.example.foodplaner.authuntication.presenter.LoginPresenterInterface;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements LoginViewInterface {
    LoginPresenterInterface presenter;
    TextView signupButton;
    Button login;
    Button loginAsGuest;
    EditText emailEditText;
    EditText passwordEditText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        emailEditText = view.findViewById(R.id.email_input_login);
        passwordEditText = view.findViewById(R.id.password_input_login);

        presenter = new LoginPresenter(this);


        signupButton = view.findViewById(R.id.signup_now_txt_btn);
        signupButton.setOnClickListener(view1 -> {presenter.navigateToSignup();});

        requireActivity().getOnBackPressedDispatcher()
                .addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        //Make Sure That The User Want To Exit The App

                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigateUp();
                    }
                });

        login = view.findViewById(R.id.btn_login);
        login.setOnClickListener(view1 -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            presenter.login(email, password);
        });

        loginAsGuest =  view.findViewById(R.id.btn_login_as_guest);
        loginAsGuest.setOnClickListener(view1 -> {presenter.loginAsGuest();});
    }

    @Override
    public void navigateToSignup() {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_signupFragment);
    }

    @Override
    public void navigateToHome() {
        NavHostFragment.findNavController(LoginFragment.this)
                .navigate(R.id.action_loginFragment_to_homeFragment);
    }

    @Override
    public void showLoading() {
        // Show a ProgressBar or similar (e.g., login.setEnabled(false))
    }

    @Override
    public void hideLoading() {
        // Hide progress bar
    }

    @Override
    public void failLogin(String message) {
        // Using a Toast or setting errors on the InputLayouts
        emailEditText.setError(message);
        passwordEditText.setError(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Prevent memory leaks and crashes after the view is gone
        if (presenter instanceof LoginPresenter) {
            ((LoginPresenter) presenter).dispose();
        }
    }

}