package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.databinding.ActivityCourseBinding;

public class CourseActivity extends AppCompatActivity {

    private ActivityCourseBinding binding;
    private Course course;
    private MediaController mediaController;

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
        binding.courseDescTv.setText("About " + course.getCourseTitle() + " : " + course.getCourseDesc());
        binding.courseTitleTv.setText("Title: " + course.getCourseTitle());
        binding.coursePriceTv.setText("Price: " + course.getPrice() + "$");

        String randomVariantNumber = Double.toString(Math.round(Math.random() * (100 - 20 + 1) + 20));
        binding.exerciseVariantTv.setText(randomVariantNumber + " Workouts");

        String randomHourNumber = Double.toString(Math.round(Math.random() * (150 - 30 + 1) + 20));
        binding.exerciseTimeTv.setText(randomHourNumber + " Hour");

        binding.courseVideoVv.setVideoPath(course.getVideoUrl());
        binding.courseVideoVv.start();
        mediaController = new MediaController(this);
        mediaController.setMediaPlayer(binding.courseVideoVv);
        binding.courseVideoVv.setMediaController(mediaController);
        binding.courseVideoVv.requestFocus();


    }


}
