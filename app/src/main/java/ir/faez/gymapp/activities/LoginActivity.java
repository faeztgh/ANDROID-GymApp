package ir.faez.gymapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.gymapp.R;
import ir.faez.gymapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        // hide actionbar
        getSupportActionBar().hide();

        // initializing binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // invoke Listeners
        invokeOnClickListeners();
    }

    private void invokeOnClickListeners() {
    binding.signupBtn.setOnClickListener(this);
    binding.loginBtn.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_btn:
                signUpBtnHandler();
                break;
            case R.id.login_btn:
                loginBtnHandler();
                break;
        }
    }

    private void loginBtnHandler() {
    }

    private void signUpBtnHandler() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
        finish();
    }
}
