package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityAllCoursesBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.ListHelper;
import ir.faez.gymapp.utils.Result;
import ir.faez.gymapp.utils.ResultListener;

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

        networkHelper.getAllCourses(new ResultListener<Course>() {
            @Override
            public void onResult(Result<Course> result) {
                Error error = (result != null) ? result.getError() : null;
               Course cs = (result != null) ? result.getItem() : null;
                if ((result == null) || (error != null) || (result == null)) {
                    String errMsg = (error != null) ? error.getMessage() : getString(R.string.cantSignInError);
                    Toast.makeText(AllCoursesActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }


                    Log.i("FAEZ_TEST", "onResult: " + cs);

            }
        });

    }
}
