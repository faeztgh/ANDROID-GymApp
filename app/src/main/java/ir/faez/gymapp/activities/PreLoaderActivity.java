package ir.faez.gymapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.GetSpecificUserAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.User;
import ir.faez.gymapp.databinding.ActivityIntroBinding;
import ir.faez.gymapp.utils.Action;

public class PreLoaderActivity extends AppCompatActivity {
    private ActivityIntroBinding binding;
    private AppData appData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        // initializing AppData
        appData = (AppData) getApplication();

        // initializing binding
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();

        // check if user loggedin
        getUserByState();

    }


    private void navigateToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void navigateToDashboardActivity() {
        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
    }


    private void getUserByState() {

        GetSpecificUserAsyncTask getSpecificUserAsyncTask = new GetSpecificUserAsyncTask(this,
                Action.GET_BY_STATE_ACTION, new DbResponse<User>() {
            @Override
            public void onSuccess(User user) {
                if (user != null && user.getIsLoggedIn().equals("true")) {
                    appData.setCurrentUser(user);
                    navigateToDashboardActivity();
                } else {
                    appData.setCurrentUser(null);
                }
            }

            @Override
            public void onError(Error error) {
                navigateToLoginActivity();
            }
        });
        getSpecificUserAsyncTask.execute("true");

    }

}
