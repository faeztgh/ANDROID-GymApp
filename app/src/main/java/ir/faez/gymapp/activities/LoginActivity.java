package ir.faez.gymapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.UserCudAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.databinding.ActivityLoginBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.Action;
import ir.faez.gymapp.utils.Result;
import ir.faez.gymapp.utils.ResultListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SIGNIN";
    private ActivityLoginBinding binding;
    private NetworkHelper networkHelper;
    private String userName;
    private String password;
    private AppData appData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        // initializing AppData
        appData = (AppData) getApplication();

        // hide actionbar
        getSupportActionBar().hide();

        // initializing binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // initializing NetworkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

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
        loadUserPass();

        if (appData.getCurrentUser() == null) {
            if (isAuth()) {
                final User finalUser = new User(userName, password);
                //Implementing Network
                networkHelper.signinUser(finalUser, result -> {
                    Error error = (result != null) ? result.getError() : null;
                    User resultUser = (result != null) ? result.getItem() : null;
                    if ((result == null) || (error != null) || (resultUser == null)) {
                        String errMsg = (error != null) ? error.getMessage() : getString(R.string.cantSignInError);
                        Toast.makeText(LoginActivity.this, errMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // setting user fields and insert to DB
                    finalUser.setId(resultUser.getId());
                    finalUser.setSessionToken(resultUser.getSessionToken());
                    finalUser.setFullName(resultUser.getFullName());
                    finalUser.setEmail(resultUser.getEmail());
                    finalUser.setUsername(resultUser.getUsername());
                    finalUser.setPassword(resultUser.getPassword());
                    finalUser.setIsLoggedIn("true");

                    // Implementing Insertion to DB
                    UserCudAsyncTask userCudAsyncTask = new UserCudAsyncTask(getApplicationContext(),
                            Action.INSERT_ACTION, new DbResponse<User>() {
                        @Override
                        public void onSuccess(User user) {
                            if (user != null) {
                                appData.setCurrentUser(resultUser);
//                                    currUser = resultUser;
                                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                startActivity(intent);
                                userName = "";
                                password = "";
                            }
                        }

                        @Override
                        public void onError(Error error) {
                            Toast.makeText(LoginActivity.this,
                                    R.string.cantSignInError, Toast.LENGTH_SHORT).show();
                        }
                    });
                    userCudAsyncTask.execute(finalUser);
                });
            }
        }
    }

    private boolean isAuth() {
        return isUsernameValid() && isPasswordValid();
    }

    private boolean isUsernameValid() {
        if (!binding.usernameEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            Toast.makeText(this, R.string.emptyUsernameField, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isPasswordValid() {
        if (!binding.passwordEdt.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            Toast.makeText(this, R.string.emptyPasswordField, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void signUpBtnHandler() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intent);
    }


    private void loadUserPass() {
        userName = binding.usernameEdt.getText().toString().trim();
        password = binding.passwordEdt.getText().toString().trim();
    }


}
