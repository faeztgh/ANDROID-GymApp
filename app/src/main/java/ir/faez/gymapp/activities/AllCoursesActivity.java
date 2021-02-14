package ir.faez.gymapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityAllCoursesBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.CourseAdapter;
import ir.faez.gymapp.utils.ListHelper;
import ir.faez.gymapp.utils.OnCourseClickListener;
import ir.faez.gymapp.utils.Result;
import ir.faez.gymapp.utils.ResultListener;

public class AllCoursesActivity extends AppCompatActivity implements OnCourseClickListener {

    private static final String TAG = "EXPENSE_ACTIVITY";
    private static final int REQUEST_CODE = 1;
    private static final String EXTRA_COURSE = "EXTRA_COURSE";
    private ActivityAllCoursesBinding binding;
    private ListHelper listHelper;
    private List<Course> allCourses;
    private NetworkHelper networkHelper;
    private CourseAdapter courseAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }


    private void init() {


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
                List<Course> cs = (result != null) ? result.getItems() : null;
                if ((result == null) || (error != null) || (result == null)) {
                    String errMsg = (error != null) ? error.getMessage() : getString(R.string.cantSignInError);
                    Toast.makeText(AllCoursesActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                allCourses = cs;

                recyclerViewInit();


            }
        });
    }


    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.courses_recycler_view);
        courseAdapter = new CourseAdapter(this, allCourses, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    public void onCourseClicked(Course course, int position) {
        course = allCourses.get(position);

        if (course != null) {
            Intent intent = new Intent(this, CourseActivity.class);
            intent.putExtra(EXTRA_COURSE, course);
            startActivity(intent);
        }


    }


}
