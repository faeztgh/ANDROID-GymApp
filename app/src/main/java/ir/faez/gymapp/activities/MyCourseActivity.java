package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityMyCourseBinding;
import ir.faez.gymapp.network.NetworkHelper;

public class MyCourseActivity extends AppCompatActivity {
    private static final String TAG = "MY_COURSE_ACTIVITY";
    private ActivityMyCourseBinding binding;
    private NetworkHelper networkHelper;
    private List<Course> myCourses;
    private AppData appData;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        init();
    }

    private void init() {
        // initializing network helper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        // initializing binding
        binding = ActivityMyCourseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // init app data
        appData = (AppData) getApplication();
    }
}
