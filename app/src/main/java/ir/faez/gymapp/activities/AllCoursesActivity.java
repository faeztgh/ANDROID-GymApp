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

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
import ir.faez.gymapp.databinding.ActivityAllCoursesBinding;
import ir.faez.gymapp.network.NetworkHelper;
import ir.faez.gymapp.utils.Action;
import ir.faez.gymapp.utils.CourseAdapter;
import ir.faez.gymapp.utils.OnCourseClickListener;

public class AllCoursesActivity extends AppCompatActivity implements OnCourseClickListener {

    private static final String EXTRA_ACTIVITY_NAME = "EXTRA_ACTIVITY_NAME";
    private static final String ACTIVITY_NAME = "ALL_COURSES";
    private static final String EXTRA_COURSE = "EXTRA_COURSE";
    private static final String EXTRA_STATUS = "EXTRA_STATUS";
    private static final String TAG = "ALL_COURSES_ACTIVITY";
    private static final int REQUEST_CODE = 1;

    private List<CourseReservation> courseReservationsList;
    private LinkedHashMap<Course, String> allCoursesAndStatus;
    private List<Course> allCourses;
    private ActivityAllCoursesBinding binding;
    private NetworkHelper networkHelper;
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
        binding = ActivityAllCoursesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // init app data
        appData = (AppData) getApplication();

        // getting courses and courseReservations from server on start
        loadAllData();

        // implementing SwipeToRefresh
        swipeToRefreshImp();

    }

    private void loadAllData() {
        getAllCoursesFromServerToDb();
        getAllCoursesFromDb();
        loadCourseReservationsFromServerToDb();
        loadCourseReservationsFromDb();
    }

    private void swipeToRefreshImp() {

        binding.allCoursesSwipeToRefreshLayout.setOnRefreshListener(
                this::loadAllData);
    }


    private void makeMyCoursesList() {
        allCoursesAndStatus = new LinkedHashMap<>();
        if (allCourses != null && allCourses.size() != 0) {
            if (courseReservationsList != null && courseReservationsList.size() != 0) {

                for (Course c : allCourses) {
                    for (CourseReservation cr : courseReservationsList) {
                        if (cr.getCourseId().equals(c.getId())) {
                            allCoursesAndStatus.put(c, cr.getStatus());
                            break;
                        } else {
                            allCoursesAndStatus.put(c, "");
                        }
                    }
                }

            }
            recyclerViewInit();
        } else {
            getAllCoursesFromDb();
        }
    }

    // ***************************** Load CourseReservations from Server****************************************

    private void loadCourseReservationsFromServerToDb() {
        networkHelper.getSpecificCourseReservation(appData.getCurrentUser(),
                result -> {
                    Log.d(TAG, "Result of getting user course reservation from server" + result);
                    Error error = (result != null) ? result.getError() : null;
                    List<CourseReservation> resultCourseReservation = result != null ? result.getItems() : null;

                    if ((result == null) || (error != null)) {
                        String errMsg = (error != null) ? error.getMessage() : getString(R.string.cannotGetUserCourseReservationFromServer);
                        Toast.makeText(AllCoursesActivity.this, errMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    for (CourseReservation cr : resultCourseReservation) {
                        CourseReservationCudAsyncTask courseReservationCudAsyncTask =
                                new CourseReservationCudAsyncTask(getApplicationContext(),
                                        Action.INSERT_ACTION, new DbResponse<CourseReservation>() {
                                    @Override
                                    public void onSuccess(CourseReservation courseReservation) {
                                        loadCourseReservationsFromDb();
                                        binding.allCoursesSwipeToRefreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onError(Error error) {
                                        Toast.makeText(AllCoursesActivity.this,
                                                R.string.somethingWentWrongOnInsert,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        courseReservationCudAsyncTask.execute(cr);

                    }
                });
    }


    // ***************************** Load CourseReservations from DB ****************************************


    private void loadCourseReservationsFromDb() {
        GetSpecificCourseReservationAsyncTask getSpecificCourseReservationAsyncTask =
                new GetSpecificCourseReservationAsyncTask(getApplicationContext(),
                        Action.GET_BY_COURSE_RESERVATION_OWNER_ID,
                        new DbResponse<List<CourseReservation>>() {
                            @Override
                            public void onSuccess(List<CourseReservation> courseReservations) {
                                if (allCourses == null) {
                                    getAllCoursesFromServerToDb();
                                }

                                assert courseReservations != null;
                                if (courseReservations.size() == 0) {
                                    loadCourseReservationsFromServerToDb();
                                }

                                courseReservationsList = courseReservations;
                                appData.setAllCourseReservations(courseReservationsList);
                                makeMyCoursesList();
                            }

                            @Override
                            public void onError(Error error) {
                                Toast.makeText(AllCoursesActivity.this,
                                        R.string.cannotGetUserCourseReservationFromDb,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
        if (appData.getCurrentUser() != null) {
            getSpecificCourseReservationAsyncTask.execute(appData.getCurrentUser().getId());
        }
    }


    // ***************************** Load All Courses from Server ****************************************

    private void getAllCoursesFromServerToDb() {
        networkHelper.getAllCourses(result -> {

            Error error = (result != null) ? result.getError() : null;
            List<Course> courseList = (result != null) ? result.getItems() : null;
            if ((result == null) || (error != null)) {
                String errMsg = (error != null) ? error.getMessage() : getString(R.string.cannotGetCoursesFromServer);
                Toast.makeText(AllCoursesActivity.this, errMsg, Toast.LENGTH_SHORT).show();
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
                                    binding.allCoursesSwipeToRefreshLayout.setRefreshing(false);
                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(AllCoursesActivity.this,
                                            R.string.somethingWentWrongOnInsert, Toast.LENGTH_SHORT).show();
                                }
                            });
                    courseCudAsyncTask.execute(cs);
                }
            }
        });
    }


    // ***************************** Load All Courses from DB ****************************************

    private void getAllCoursesFromDb() {

        GetCoursesAsyncTask getCoursesAsyncTask = new GetCoursesAsyncTask(this,
                new DbResponse<List<Course>>() {
                    @Override
                    public void onSuccess(List<Course> courses) {
                        if (courses.size() == 0) {
                            getAllCoursesFromServerToDb();
                        } else {
                            allCourses = courses;
                        }
                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(AllCoursesActivity.this, R.string.cannotGetCoursesFromDb,
                                Toast.LENGTH_SHORT).show();
                    }
                });
        getCoursesAsyncTask.execute();
    }


    // initializing RecyclerView
    private void recyclerViewInit() {
        RecyclerView recyclerView = findViewById(R.id.courses_recycler_view);
        if (courseReservationsList == null || courseReservationsList.size() == 0) {
            courseAdapter = new CourseAdapter(this, allCourses, this);
        } else {
            courseAdapter = new CourseAdapter(this, allCoursesAndStatus, this);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //check for request code to be same.
        if (requestCode == REQUEST_CODE) {
            //check for result code to be OK.
            if (resultCode == RESULT_OK) {
                courseAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onCourseClicked(Course course, int position, String status) {
        if (allCoursesAndStatus != null && allCoursesAndStatus.size() != 0) {
            course = (new ArrayList<>(allCoursesAndStatus.keySet())).get(position);
            status = (new ArrayList<>(allCoursesAndStatus.values())).get(position);
        } else {
            course = allCourses.get(position);
        }


        if (course != null) {
            Intent intent = new Intent(this, CourseActivity.class);
            intent.putExtra(EXTRA_COURSE, course);
            intent.putExtra(EXTRA_STATUS, status);
            intent.putExtra(EXTRA_ACTIVITY_NAME, ACTIVITY_NAME);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }
}