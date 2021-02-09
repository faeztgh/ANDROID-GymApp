package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private AppData appData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // initializing appData
        appData = (AppData) getApplication();

        // handling user info
        userInfoHandler();
    }

    private void userInfoHandler() {
        binding.profileFullnameTv.setText(appData.getCurrentUser().getFullName());
        binding.profileUsernameTv.setText(appData.getCurrentUser().getUsername());
        binding.profileEmailTv.setText(appData.getCurrentUser().getEmail());
    }
}
