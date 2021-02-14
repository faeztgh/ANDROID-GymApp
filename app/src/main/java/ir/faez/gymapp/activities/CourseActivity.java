package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityCourseBinding;

public class CourseActivity extends AppCompatActivity {

    private ActivityCourseBinding binding;
    private Course course;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

    }

    private void init() {
        getSupportActionBar().hide();

        // initializing binding
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        // init course obj
        course = (Course) getIntent().getSerializableExtra("EXTRA_COURSE");

        settingUiElements();
    }

    // setting UI elements
    private void settingUiElements() {
        binding.collapsingToolbar.setTitle(course.getCourseTitle());
        Glide.with(this).load(course.getPosterUrl()).into(binding.coursePosterIv);
        binding.courseDescTv.setText(course.getCourseDesc());
        binding.courseTitleTv.setText("Title: " + course.getCourseTitle());
        binding.coursePriceTv.setText("Price: " + course.getPrice());

        binding.courseVideoVv.setVideoPath(course.getVideoUrl());
        binding.courseVideoVv.start();

    }


}
