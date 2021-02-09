package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityAllCoursesBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.ListHelper;

public class AllCoursesActivity extends AppCompatActivity {

    private static final String TAG = "EXPENSE_ACTIVITY";
    private static final int REQUEST_CODE = 1;
    private ActivityAllCoursesBinding binding;
    private ListHelper listHelper;
    private List<Course> currUserCourses;
    private NetworkHelper networkHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }


    private void init() {
        if (currUserCourses == null) {
            currUserCourses = new ArrayList<>();
        }

        // initializing network helper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        // initializing list helper
        listHelper = ListHelper.getInstance();

        // initializing binding
        binding = ActivityAllCoursesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getAllCourses();
        invokeOnClickListeners();


    }

    private void invokeOnClickListeners() {

    }

    private void getAllCourses() {

    }
}
