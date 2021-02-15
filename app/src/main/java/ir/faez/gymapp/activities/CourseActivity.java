package ir.faez.gymapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.CourseReservationCudAsyncTask;
import ir.faez.gymapp.data.async.GetReviewsAsyncTask;
import ir.faez.gymapp.data.async.ReviewCudAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.data.model.Review;
import ir.faez.gymapp.databinding.ActivityCourseBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.Action;
import ir.faez.gymapp.utils.Result;
import ir.faez.gymapp.utils.ResultListener;
import ir.faez.gymapp.utils.ReviewAdapter;
import ir.faez.gymapp.utils.Status;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "COURSE_RESERVATION";

    private ActivityCourseBinding binding;
    private Course course;
    private MediaController mediaController;
    private AppData appData;
    private NetworkHelper networkHelper;
    private List<Review> reviewList;
    private ReviewAdapter reviewAdapter;

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

        // init app data
        appData = (AppData) getApplication();

        // init networkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        settingUiElements();

        // invoke Listeners
        invokeOnClickListeners();

        // init review list
        reviewList = new ArrayList<>();

        // getting all reviews from db
        getAllReviewsFromDb();

    }


    private void getAllReviewsFromServerToDb() {
        networkHelper.getAllReviews(new ResultListener<Review>() {
            @Override
            public void onResult(Result<Review> result) {
                Error error = (result != null) ? result.getError() : null;
                List<Review> reviews = (result != null) ? result.getItems() : null;
                if ((result == null) || (error != null) || (result == null)) {
                    String errMsg = (error != null) ? error.getMessage() : getString(R.string.cannotGetReviewsFromServer);
                    Toast.makeText(CourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (reviews != null) {
                    for (Review rv : reviews) {
                        ReviewCudAsyncTask reviewCudAsyncTask = new ReviewCudAsyncTask(getApplicationContext(),
                                Action.INSERT_ACTION, new DbResponse<Review>() {
                            @Override
                            public void onSuccess(Review review) {
                                getAllReviewsFromDb();
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(CourseActivity.this,
                                        R.string.somethingWentWrongOnInsert, Toast.LENGTH_SHORT).show();
                            }
                        });
                        reviewCudAsyncTask.execute(rv);
                    }
                }
            }
        });

    }

    private void getAllReviewsFromDb() {
        GetReviewsAsyncTask getReviewsAsyncTask = new GetReviewsAsyncTask(this,
                new DbResponse<List<Review>>() {
                    @Override
                    public void onSuccess(List<Review> reviews) {
                        if (reviews == null || reviews.size() == 0) {
                            getAllReviewsFromServerToDb();
                        } else {
                            reviewList = reviews;
                            recyclerViewInit();
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(CourseActivity.this, R.string.cannotGetReviewsFromDb,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        getReviewsAsyncTask.execute(course.getId());

    }

    private void invokeOnClickListeners() {
        binding.reserveOrunReserveBtn.setOnClickListener(this);
        binding.submitReviewBtn.setOnClickListener(this);
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

        try {

            binding.courseVideoVv.setVideoPath(course.getVideoUrl());
            binding.courseVideoVv.start();
            mediaController = new MediaController(this);
            mediaController.setMediaPlayer(binding.courseVideoVv);
            binding.courseVideoVv.setMediaController(mediaController);
            binding.courseVideoVv.requestFocus();
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.getMessage();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reserveOrunReserve_btn:
                reserveOrDeleteCourseBtnHandler();
                break;
            case R.id.submit_review_btn:
                addReviewBtnHandler();
                break;
            default:
                Toast.makeText(this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
        }
    }

    private void addReviewBtnHandler() {
        Date dateTime = Calendar.getInstance().getTime();
        Review review = new Review(dateTime.toString(), binding.addReviewEt.getText().toString().trim(),
                course.getId(), appData.getCurrentUser().getId(), appData.getCurrentUser().getFullName());

        networkHelper.insertReview(review, appData.getCurrentUser(), new ResultListener<Review>() {
            @Override
            public void onResult(Result<Review> result) {
                Log.d(TAG, "Result of add review in server" + result);
                Error error = (result != null) ? result.getError() : null;
                Review reviewResult = result != null ? result.getItem() : null;
                if ((result == null) || (error != null)) {
                    String errMsg = (error != null) ? error.getMessage() : getString(R.string.somethingWentWrongOnInsert);
                    Toast.makeText(CourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }
                getAllReviewsFromDb();
                Toast.makeText(CourseActivity.this, R.string.successfullAddingReview, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void reserveOrDeleteCourseBtnHandler() {
        CourseReservation courseReservation = new CourseReservation(Status.PENDING, course.getId(),
                appData.getCurrentUser().getId());


        networkHelper.insertCourseReservation(courseReservation, appData.getCurrentUser(),
                new ResultListener<CourseReservation>() {
                    @Override
                    public void onResult(Result<CourseReservation> result) {
                        Log.d(TAG, "Result of add course reservation in server" + result);
                        Error error = (result != null) ? result.getError() : null;
                        CourseReservation crResult = result != null ? result.getItem() : null;
                        if ((result == null) || (error != null)) {
                            String errMsg = (error != null) ? error.getMessage() : getString(R.string.somethingWentWrongOnInsert);
                            Toast.makeText(CourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        courseReservation.setId(crResult.getId());
                        courseReservation.setReservationCode(crResult.getReservationCode());
                        courseReservation.setStatus(crResult.getStatus());

                        CourseReservationCudAsyncTask courseReservationCudAsyncTask = new CourseReservationCudAsyncTask(getApplicationContext(), Action.INSERT_ACTION, new DbResponse<CourseReservation>() {
                            @Override
                            public void onSuccess(CourseReservation courseReservation) {
                                if (courseReservation == null) {
                                    return;
                                }
                                Toast.makeText(CourseActivity.this, R.string.courseReservationAddedSuccessfully, Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(CourseActivity.this, R.string.sthWentWrongOnAddingCourseReservation, Toast.LENGTH_SHORT).show();
                            }
                        });
                        courseReservationCudAsyncTask.execute(courseReservation);
                    }
                });

    }


    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.course_review_recycler_view);
        reviewAdapter = new ReviewAdapter(this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);
    }
}
