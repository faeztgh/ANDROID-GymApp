package ir.faez.gymapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
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
import ir.faez.gymapp.utils.ReviewAdapter;
import ir.faez.gymapp.utils.Status;


public class CourseActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String DELETE_EXTRA_MESSAGE = "DELETED_COURSE";
    public static final String RESERVE_EXTRA_MESSAGE = "RESERVE_EXTRA_MESSAGE";
    private static final String TAG = "COURSE_RESERVATION";
    private CourseReservation courseReservation;
    private MediaController mediaController;
    private ActivityCourseBinding binding;
    private NetworkHelper networkHelper;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;
    private String activityName;
    private AppData appData;
    private Course course;
    private String status;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
        // hide action bar for this activity
        getSupportActionBar().hide();

        // initializing binding
        binding = ActivityCourseBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // init course obj
        course = (Course) getIntent().getSerializableExtra("EXTRA_COURSE");
        //init course status
        status = getIntent().getStringExtra("EXTRA_STATUS");
        //init activity name that intent come from
        activityName = getIntent().getStringExtra("EXTRA_ACTIVITY_NAME");

        // init app data
        appData = (AppData) getApplication();

        // init networkHelper
        networkHelper = NetworkHelper.getInstance(getApplicationContext());

        // invoke Listeners
        invokeOnClickListeners();

        // init review list
        reviewList = new ArrayList<>();

        // getting all reviews from db
        getAllReviewsFromDb();

        for (CourseReservation cr : appData.getAllCourseReservations()) {
            if (cr.getCourseId().equals(course.getId())) {
                courseReservation = cr;
            }
        }

        settingUiElements();
    }


    private void invokeOnClickListeners() {
        binding.reserveOrunReserveBtn.setOnClickListener(this);
        binding.submitReviewBtn.setOnClickListener(this);
        binding.reloadReviewsBtn.setOnClickListener(this);
    }

    /**
     * get the course reviews from server and insert to db
     */
    private void getAllReviewsFromServerToDb() {
        networkHelper.getAllReviews(result -> {
            Error error = (result != null) ? result.getError() : null;
            List<Review> reviews = (result != null) ? result.getItems() : null;
            if ((result == null) || (error != null)) {
                String errMsg = (error != null) ? error.getMessage() : getString(R.string.cannotGetReviewsFromServer);
                Toast.makeText(CourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            if (reviews != null) {
                // inserting to DB
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
            } else {
                Toast.makeText(appData, R.string.noReviewFound, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * get all the reviews from db
     */
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


    /**
     * setting UI elements
     */
    @SuppressLint({"SetTextI18n", "UseCompatLoadingForColorStateLists"})
    private void settingUiElements() {

        // some UI setup depend on status of the course
        if (status != null) {
            if (status.equals(Status.PENDING)) {
                binding.reserveOrunReserveBtn.setVisibility(View.GONE);
            } else if (status.equals(Status.RESERVED)) {
                binding.reserveOrunReserveBtn.setText("Delete Course");
                binding.reserveOrunReserveBtn.setBackgroundTintList(getApplicationContext()
                        .getResources().getColorStateList(R.color.Hollywood_Cerise));
            }
        }

        // some UI setup depend on the activity
        if (activityName != null && activityName.equals("ALL_COURSES")) {
            if (binding.reserveOrunReserveBtn.getText().toString().trim()
                    .equalsIgnoreCase("Delete Course")) {
                binding.reserveOrunReserveBtn.setVisibility(View.GONE);
                binding.courseConfirmCodeTv.setVisibility(View.GONE);
            }
        }

        // some UI setup depend on the activity
        binding.courseConfirmCodeTv.setVisibility(View.GONE);
        if (activityName != null && activityName.equals("MY_COURSES")) {
            if (courseReservation != null) {
                if (courseReservation.getReservationCode() != null && status.equals(Status.RESERVED)) {
                    binding.courseConfirmCodeTv.setVisibility(View.VISIBLE);
                    binding.courseConfirmCodeTv.setText(courseReservation.getReservationCode());
                }
            }
        }

        // General UI setup
        binding.collapsingToolbar.setTitle(course.getCourseTitle());
        Glide.with(this).load(course.getPosterUrl()).into(binding.coursePosterIv);
        binding.courseDescTv.setText("About " + course.getCourseTitle() + " : " + course.getCourseDesc());
        binding.courseTitleTv.setText("Title: " + course.getCourseTitle());
        binding.coursePriceTv.setText("Price: " + course.getPrice() + "$");
        binding.classDateTimeTv.setText("Date/Time : " + course.getDateTime());

        String randomVariantNumber = Integer.toString((int) Math.round(Math.random() * (100 - 20 + 1) + 20));
        binding.exerciseVariantTv.setText(randomVariantNumber + " Workouts");

        String randomHourNumber = Double.toString(Math.round(Math.random() * (150 - 30 + 1) + 20));
        binding.exerciseTimeTv.setText(randomHourNumber + " Hour");

        // setting up video
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
            case R.id.reload_reviews_btn:
                getAllReviewsFromServerToDb();
                getAllReviewsFromDb();
                // animate the reload button
                binding.reloadReviewsBtn.animate().rotationBy(360).setDuration(1000)
                        .setInterpolator(new LinearInterpolator()).start();
                break;

            default:
                Toast.makeText(this, R.string.somethingWentWrong, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * add review to the server
     */
    private void addReviewBtnHandler() {
        Date dateTime = Calendar.getInstance().getTime();

        Review review = new Review(dateTime.toString(),
                binding.addReviewEt.getText().toString().trim(),
                course.getId(), appData.getCurrentUser().getId(),
                appData.getCurrentUser().getFullName());

        networkHelper.insertReview(review, appData.getCurrentUser(), result -> {
            Error error = (result != null) ? result.getError() : null;
            if ((result == null) || (error != null)) {
                String errMsg = (error != null) ? error.getMessage() : getString(R.string.somethingWentWrongOnInsert);
                Toast.makeText(CourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                return;
            }

            // after insert, load them from server to db and read them again
            getAllReviewsFromDb();
            Toast.makeText(CourseActivity.this, R.string.successfullAddingReview,
                    Toast.LENGTH_SHORT).show();
            binding.addReviewEt.setText("");
        });
    }


    /**
     * handle delete or reserve depend on the activity
     */
    private void reserveOrDeleteCourseBtnHandler() {
        if (courseReservation != null) {
            if (binding.reserveOrunReserveBtn.getText().toString().trim().
                    equalsIgnoreCase("Delete Course")) {

                deleteCourseReservation();
            }
        }


        if (binding.reserveOrunReserveBtn.getText().toString().trim().
                equalsIgnoreCase("Reserve Course")) {
            reserveCourse();
        }
    }

    private void reserveCourse() {

        CourseReservation courseReservation = new CourseReservation(Status.PENDING, course.getId(),
                appData.getCurrentUser().getId());

        // insert courseReservation to server
        networkHelper.insertCourseReservation(courseReservation, appData.getCurrentUser(),
                result -> {
                    Error error = (result != null) ? result.getError() : null;
                    CourseReservation crResult = result != null ? result.getItem() : null;
                    if ((result == null) || (error != null)) {
                        String errMsg = (error != null) ? error.getMessage() : getString(R.string.somethingWentWrongOnInsert);
                        Toast.makeText(CourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //setting id, reserveCode and status
                    courseReservation.setId(crResult.getId());
                    courseReservation.setReservationCode(crResult.getReservationCode());
                    courseReservation.setStatus(crResult.getStatus());

                    // insert it to DB
                    CourseReservationCudAsyncTask courseReservationCudAsyncTask =
                            new CourseReservationCudAsyncTask(getApplicationContext(),
                                    Action.INSERT_ACTION, new DbResponse<CourseReservation>() {
                                @Override
                                public void onSuccess(CourseReservation courseReservation1) {
                                    if (courseReservation1 == null) {
                                        return;
                                    }

                                    Toast.makeText(CourseActivity.this, R.string.courseReservationAddedSuccessfully, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra(RESERVE_EXTRA_MESSAGE, courseReservation1);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(CourseActivity.this, R.string.sthWentWrongOnAddingCourseReservation, Toast.LENGTH_SHORT).show();
                                }
                            });
                    courseReservationCudAsyncTask.execute(courseReservation);
                });
    }


    private void deleteCourseReservation() {
        // delete from server
        networkHelper.deleteCourseReservation(courseReservation, appData.getCurrentUser(),
                result -> {
                    Error error = result != null ? result.getError() : null;
                    CourseReservation courseReservationResult = result != null ? result.getItem() : null;

                    if (result == null || courseReservationResult == null || error != null) {
                        String errorMsg = error != null ? error.getMessage() : getString(R.string.somethingWentWrongOnDelete);
                        Toast.makeText(CourseActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }


                    // Removing from DB
                    CourseReservationCudAsyncTask courseReservationCudAsyncTask =
                            new CourseReservationCudAsyncTask(CourseActivity.this,
                                    Action.DELETE_ACTION, new DbResponse<CourseReservation>() {
                                @Override
                                public void onSuccess(CourseReservation courseReservation) {
                                    Toast.makeText(CourseActivity.this, R.string.courseRemovedSuccessfully, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra(DELETE_EXTRA_MESSAGE, courseReservation);
                                    setResult(RESULT_OK, intent);
                                    finish();

                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(CourseActivity.this, error.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                    if (courseReservation != null) {
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
