package ir.faez.gymapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import ir.faez.gymapp.R;
import ir.faez.gymapp.data.AppData;
import ir.faez.gymapp.data.async.CourseCudAsyncTask;
import ir.faez.gymapp.data.async.CourseReservationCudAsyncTask;
import ir.faez.gymapp.data.async.GetCoursesAsyncTask;
import ir.faez.gymapp.data.async.GetSpecificCourseReservationAsyncTask;
import ir.faez.gymapp.data.db.DAO.DbResponse;
import ir.faez.gymapp.data.model.Course;
import ir.faez.gymapp.data.model.CourseReservation;
import ir.faez.gymapp.databinding.ActivityMyCourseBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.Action;
import ir.faez.gymapp.utils.CourseAdapter;
import ir.faez.gymapp.utils.OnCourseClickListener;
import ir.faez.gymapp.utils.Result;
import ir.faez.gymapp.utils.ResultListener;

public class MyCourseActivity extends AppCompatActivity implements OnCourseClickListener {
    private static final String TAG = "MY_COURSE_ACTIVITY";
    private static final String EXTRA_COURSE = "EXTRA_COURSE";
    private static final String EXTRA_STATUS = "EXTRA_STATUS";
    private static final int REQUEST_CODE = 1;

    private ActivityMyCourseBinding binding;
    private NetworkHelper networkHelper;
    private HashMap<Course, String> myCoursesAndStatus;
    private LinkedHashSet<Course> myCoursesList;
    private List<Course> allCoursesList;
    private List<CourseReservation> courseReservationsList;
    private CourseAdapter courseAdapter;
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

        // load allCourses
        getAllCoursesFromDb();

        // get user specific course reservation from server
        loadCourseReservationsFromDb();

        // implementing SwipeToRefresh
        swipeToRefreshImp();
    }

    private void swipeToRefreshImp() {

        binding.myCoursesSwipeToRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getAllCoursesFromServerToDb();
                        loadCourseReservationsFromDb();
                    }
                });
    }

    private void makeMyCoursesList() {
        myCoursesAndStatus = new HashMap<>();
        myCoursesList = new LinkedHashSet<>();

        if (allCoursesList != null) {
            if (courseReservationsList != null) {

                for (CourseReservation cr : courseReservationsList) {
                    for (Course c : allCoursesList) {
                        if (cr.getCourseId().equals(c.getId())) {
                            myCoursesAndStatus.put(c, cr.getStatus());
                            myCoursesList.add(c);
                        }
                    }
                }

                recyclerViewInit();
            } else {
                loadCourseReservationsFromDb();
            }
        } else {
            getAllCoursesFromDb();
        }
    }


    // ***************************** Load CourseReservations ****************************************

    private void loadCourseReservationsFromServerToDb() {
        networkHelper.getSpecificCourseReservation(appData.getCurrentUser(),
                new ResultListener<CourseReservation>() {
                    @Override
                    public void onResult(Result<CourseReservation> result) {
                        Log.d(TAG, "Result of getting user course reservation from server" + result);
                        Error error = (result != null) ? result.getError() : null;
                        List<CourseReservation> resultCourseReservation = result != null ? result.getItems() : null;

                        if ((result == null) || (error != null)) {
                            String errMsg = (error != null) ? error.getMessage() : getString(R.string.cannotGetUserCourseReservationFromServer);
                            Toast.makeText(MyCourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (CourseReservation cr : resultCourseReservation) {

                            CourseReservationCudAsyncTask courseReservationCudAsyncTask =
                                    new CourseReservationCudAsyncTask(getApplicationContext(), Action.INSERT_ACTION, new DbResponse<CourseReservation>() {
                                        @Override
                                        public void onSuccess(CourseReservation courseReservation) {
                                            binding.myCoursesSwipeToRefreshLayout.setRefreshing(false);
                                            loadCourseReservationsFromDb();
                                        }

                                        @Override
                                        public void onError(Error error) {
                                            Toast.makeText(MyCourseActivity.this, R.string.somethingWentWrongOnInsert, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            courseReservationCudAsyncTask.execute(cr);

                        }
                    }
                });
    }

    private void loadCourseReservationsFromDb() {
        GetSpecificCourseReservationAsyncTask getSpecificCourseReservationAsyncTask =
                new GetSpecificCourseReservationAsyncTask(getApplicationContext(),
                        Action.GET_BY_COURSE_RESERVATION_OWNER_ID,
                        new DbResponse<List<CourseReservation>>() {
                            @Override
                            public void onSuccess(List<CourseReservation> courseReservations) {
                                if (allCoursesList == null) {
                                    getAllCoursesFromServerToDb();
                                }

                                if (courseReservations != null && courseReservations.size() != 0) {
                                    courseReservationsList = courseReservations;
                                    appData.setAllCourseReservations(courseReservationsList);
                                    makeMyCoursesList();
                                } else {
                                    loadCourseReservationsFromServerToDb();
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(MyCourseActivity.this,
                                        R.string.cannotGetUserCourseReservationFromDb,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
        getSpecificCourseReservationAsyncTask.execute(appData.getCurrentUser().getId());
    }


    //********************************* Loading All Courses **************************************
    private void getAllCoursesFromServerToDb() {

        networkHelper.getAllCourses(new ResultListener<Course>() {
            @Override
            public void onResult(Result<Course> result) {

                Error error = (result != null) ? result.getError() : null;
                List<Course> courseList = (result != null) ? result.getItems() : null;
                if ((result == null) || (error != null) || (result == null)) {
                    String errMsg = (error != null) ? error.getMessage() : getString(R.string.cannotGetCoursesFromServer);
                    Toast.makeText(MyCourseActivity.this, errMsg, Toast.LENGTH_SHORT).show();
                    return;
                }


                if (courseList != null) {
                    for (Course cs : courseList) {

                        CourseCudAsyncTask courseCudAsyncTask = new
                                CourseCudAsyncTask(getApplicationContext(), Action.INSERT_ACTION,
                                new DbResponse<Course>() {
                                    @Override
                                    public void onSuccess(Course course) {
                                        getAllCoursesFromDb();
                                        binding.myCoursesSwipeToRefreshLayout.setRefreshing(false);

                                    }

                                    @Override
                                    public void onError(Error error) {
                                        Toast.makeText(MyCourseActivity.this,
                                                R.string.somethingWentWrongOnInsert, Toast.LENGTH_SHORT).show();
                                    }
                                });
                        courseCudAsyncTask.execute(cs);
                    }
                }
            }
        });
    }

    private void getAllCoursesFromDb() {
        GetCoursesAsyncTask getCoursesAsyncTask = new GetCoursesAsyncTask(this,
                new DbResponse<List<Course>>() {
                    @Override
                    public void onSuccess(List<Course> courses) {
                        if (courses.size() == 0 || courses == null) {
                            getAllCoursesFromServerToDb();
                        } else {
                            allCoursesList = courses;
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(MyCourseActivity.this, R.string.cannotGetCoursesFromDb,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        getCoursesAsyncTask.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check for request code to be same.
        if (requestCode == REQUEST_CODE) {
            //check for result code to be OK.
            if (resultCode == RESULT_OK) {
                CourseReservation returnCourseReservation = (CourseReservation) data.getSerializableExtra("DELETED_COURSE");

                try {
                    // delete the course from list
                    for (Course c : myCoursesList) {
                        if (returnCourseReservation.getCourseId().equals(c.getId())) {
                            myCoursesAndStatus.remove(c);
                            courseAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.my_courses_recycler_view);
        courseAdapter = new CourseAdapter(this, myCoursesAndStatus, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    public void onCourseClicked(Course course, int position, String status) {
        course = (new ArrayList<Course>(myCoursesAndStatus.keySet())).get(position);
        status = (new ArrayList<String>(myCoursesAndStatus.values())).get(position);

        if (course != null) {
            Intent intent = new Intent(this, CourseActivity.class);
            intent.putExtra(EXTRA_COURSE, course);
            intent.putExtra(EXTRA_STATUS, status);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

}