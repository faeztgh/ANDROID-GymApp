package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.databinding.ActivitySignupBinding;
import ir.faez.gymapp.network.NetworkHelper;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIGN_UP";
    private ActivitySignupBinding binding;
    private NetworkHelper networkHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        // initializing binding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // initializing NetworkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        // invoke Listeners
        invokeOnClickListeners();
    }


    private void invokeOnClickListeners() {
        binding.registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register_btn) {
            signUpBtnHandler();
        } else {
            Toast.makeText(this, R.string.wrongChoice, Toast.LENGTH_SHORT).show();
        }
    }


    //****************************** Implementing OnClick Methods*********************************
    private void signUpBtnHandler() {
        String fullName = binding.fullnameEdt.getText().toString().trim();
        String email = binding.emailEdt.getText().toString().trim();
        String userName = binding.usernameEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString().trim();


        // validate fields
        if (isInputsFilled()
                && isEmailValid()
                && isFullnameValid()
                && isPasswordValid()
                && isConfirmPasswordValid()) {

            User user = new User(fullName, email, userName, password);

            networkHelper.signupUser(user, result -> {
                Error error = (result != null) ? result.getError() : null;

                if ((result == null) || (error != null)) {
                    String errMsg = (error != null) ? error.getMessage() : getString(R.string.cantSignUpError);
                    Toast.makeText(SignupActivity.this, errMsg,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(SignupActivity.this,
                        R.string.successfulRegister, Toast.LENGTH_SHORT).show();
                finish();
            });
        }
    }


    //****************************** Validating inputs *********************************
    private boolean isFullnameValid() {
        String str = binding.fullnameEdt.getText().toString().trim();
        if (!str.matches("-?\\d+(\\.\\d+)?")) {
            return true;
        } else {
            Toast.makeText(this, R.string.cannotUseDigitAsName, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isEmailValid() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (binding.emailEdt.getText().toString().trim().matches(emailPattern)
                && !binding.emailEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            binding.emailEdt.setError(this.getResources().getString(R.string.wrongEmailFormat));
            return false;
        }
    }

    private boolean isPasswordValid() {
        if (binding.passwordEdt.getText().toString().length() >= 6
                && !binding.passwordEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            binding.passwordEdt.setError(this.getResources().getString(R.string.passwordLengthError));
            return false;
        }
    }


    private boolean isConfirmPasswordValid() {
        if (binding.passwordRepEdt.getText().toString()
                .equals(binding.passwordEdt.getText().toString())
                && !binding.passwordRepEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            binding.passwordRepEdt.setError(this.getResources().getString(R.string.passwordsNotMatch));
            Toast.makeText(SignupActivity.this,
                    R.string.passwordConfirmNotMatch, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private boolean isInputsFilled() {
        if (!binding.fullnameEdt.getText().toString().trim().isEmpty()
                && !binding.emailEdt.getText().toString().trim().isEmpty()
                && !binding.usernameEdt.getText().toString().trim().isEmpty()
                && !binding.passwordEdt.getText().toString().trim().isEmpty()
                && !binding.passwordRepEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            Toast.makeText(SignupActivity.this,
                    "Please Complete The Form",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
